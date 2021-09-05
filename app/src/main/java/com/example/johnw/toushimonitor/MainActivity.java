package com.example.johnw.toushimonitor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnw.toushimonitor.camera.CameraSurfaceHolder;
import com.example.johnw.toushimonitor.camera.HandleBitmap;


import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private ImageView mImageView;

    CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();
    public View mView;

    public String mStrFaceInfo; //百度api解析返回的文件
    private Button mSettingButton; //设置按钮
    boolean mSettingShow; //设置按钮是否展示

    public RelativeLayout mTitleLayout; //标题界面布局
    public RelativeLayout mLayout; //展示界面布局
    public RelativeLayout mMainLayout; //覆盖整个界面的布局

    private ImageView mKoImageView;
    private ImageView mScanView;

    private TextView mTitleView;
    private TextView mCompanyView;

    int mPlayIndex = -1; //播放扫描视频时间点
    int mChangeActivityIndex = -1; //切换Activity时间点
    boolean mStartScan;

    boolean mTouchScreen = false;

    AnimationDrawable mAnimationDrawable;

    String mAccessToken;
    private static final String AccessTokenUrl = "https://aip.baidubce.com/oauth/2.0/token";
    private static final String FeatureUrl = "https://aip.baidubce.com/rest/2.0/face/v3/detect";
    private static final String GrantType = "client_credentials";
    private static final String ClientId = "sfBcA4cVfkU5SbWr4p60KfyL";
    private static final String ClientSecret = "2iWw8k5FroYyGee80WTTwvnrQGDplG9H";

    private static final int REQUEST_CODE = 1024;
    private static final int CAMERA_CODE = 1;
    private static final int TAKE_PHOTO = 2;

    private Timer mTimer;
    private TimerTask mTimerTask;
    int mIndex = 0;

    HandleBitmap handleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean b = requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //多线程模式
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //程序权限申请
        requestPermission();

        //初始化界面信息
        String titleName = sharedPreferences.getString("titleText", "颜值机");
        String companyName = sharedPreferences.getString("companyText", "TOUSHI Tech");
        int titleSize = sharedPreferences.getInt("titleSize", 80);
        int companySize = sharedPreferences.getInt("companySize", 50);

        mTitleView = (TextView) findViewById(R.id.deviceName);
        mCompanyView = (TextView) findViewById(R.id.companyView);
        mTitleView.setText(titleName);
        mTitleView.setTextSize(titleSize);
        mCompanyView.setText(companyName);
        mCompanyView.setTextSize(companySize);

        mMainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mTitleLayout = (RelativeLayout) findViewById(R.id.mainTotalLayout);

        mKoImageView = (ImageView) findViewById(R.id.koTitle);
        mScanView = (ImageView)findViewById(R.id.scanVideo);
        mTitleLayout.setVisibility(View.INVISIBLE);
        mKoImageView.setVisibility(View.INVISIBLE);
        mScanView.setVisibility(View.INVISIBLE);
        mMainLayout.setVisibility(View.VISIBLE); //设置主界面可见
    }

    //Android11申请权限
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //先判断有没有权限
            if (Environment.isExternalStorageManager()) {
                writeFile();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent, REQUEST_CODE);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //先判断有没有权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            }
        } else {
            writeFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                writeFile();
            } else {
                Toast.makeText(getApplicationContext(), "存储权限获取失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) if (Environment.isExternalStorageManager()) {
                    writeFile();
                } else {
                    Toast.makeText(getApplicationContext(), "存储权限获取失败", Toast.LENGTH_SHORT).show();
                }
            case CAMERA_CODE:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                }
            case TAKE_PHOTO:
                Toast.makeText(getApplicationContext(), "TAKE_PHOTO", Toast.LENGTH_SHORT).show();
                if (requestCode == RESULT_OK) {
                    try {
                        Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    private void writeFile() {
        String imageFileName = getExternalFilesDir(null).getAbsolutePath() + "/image";
        String manRankFile = getExternalFilesDir(null).getAbsolutePath() + "/image/man";
        String womanRankFile = getExternalFilesDir(null).getAbsolutePath() + "/image/woman";
        String pkRankFile = getExternalFilesDir(null).getAbsolutePath() + "/image/pk";
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        new File(imageFileName).mkdir();
        new File(manRankFile).mkdir();
        new File(womanRankFile).mkdir();
        new File(pkRankFile).mkdir();
        Toast.makeText(getApplicationContext(), "写入文件成功", Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        //startCamera(imageFileName);
        mCameraSurfaceHolder.setCameraSurfaceHolder(this, mSurfaceView);
        //mCameraSurfaceHolder.SetImagePath(imageFileName);
        //initCamera();
    }
}
