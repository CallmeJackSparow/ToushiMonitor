package com.example.johnw.toushimonitor;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.media.Image;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnw.toushimonitor.camera.CameraSurfaceHolder;
import com.example.johnw.toushimonitor.camera.HandleBitmap;


import org.json.JSONArray;
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

    CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();
    public View mView;

    public String mStrFaceInfo; //百度api解析返回的文件
    private Button mSettingButton; //设置按钮
    boolean mSettingShow; //设置按钮是否展示

    public RelativeLayout mTitleLayout; //标题界面布局
    public RelativeLayout mLayout; //展示界面布局
    public RelativeLayout mMainLayout; //覆盖整个界面的布局

    //主界面1控件
    private TextView mTitleView;
    private TextView mCompanyView;
    private ImageView mBack1View;
    private ImageView mBack2View;

    //主界面2控件
    private SurfaceView mSurfaceView;
    private ImageView mLogoImage;
    private ImageView mKoImageView;
    private ImageView mScanView;
    private ImageView mScanView1;
    private ImageView mFigureView;
    private TextView mUserCount;

    int mFaceCount = 0; //人脸数量
    int mFaceCountInsistTime = 0; //持续次数
    int mPlayIndex = -1; //播放扫描视频时间点
    int mChangeActivityIndex = -1; //切换Activity时间点

    int mPlayer1Left;
    int mPlayer1Top;
    int mPlayer1Width;
    int mPlayer1Height;

    int mPlayer2Left;
    int mPlayer2Top;
    int mPlayer2Width;
    int mPlayer2Height;

    boolean mIsScan;

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
        Window window = getWindow();
        window.setStatusBarColor(Color.TRANSPARENT);
        window.setNavigationBarColor(Color.TRANSPARENT);

        setContentView(R.layout.activity_main);

        //多线程模式
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //在运行之前先获取百度接口的accessToken值
        PostAccessToken();
        //程序权限申请
        requestPermission();

        //初始化主界面信息
        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String titleName = sharedPreferences.getString("titleText", "颜值机"); //标题信息
        String companyName = sharedPreferences.getString("companyText", "TOUSHI Tech"); //公司信息
        int titleSize = sharedPreferences.getInt("titleSize", 80);
        int companySize = sharedPreferences.getInt("companySize", 50);

        String logoPath = sharedPreferences.getString("logoPath", "");
        int mCount = sharedPreferences.getInt("count", 0);

        //主界面1控件
        mMainLayout = (RelativeLayout) findViewById(R.id.mainLayout); //主界面1布局
        mMainLayout.setVisibility(View.VISIBLE); //设置主界面可见
        mTitleView = (TextView) findViewById(R.id.deviceName);
        mCompanyView = (TextView) findViewById(R.id.companyView);
        mTitleView.setText(titleName);
        mTitleView.setTextSize(titleSize);
        mCompanyView.setText(companyName);
        mCompanyView.setTextSize(companySize);

        //主界面2控件
        mTitleLayout = (RelativeLayout) findViewById(R.id.mainTotalLayout); //界面2人数显示布局
        mTitleLayout.setVisibility(View.INVISIBLE);

        mKoImageView = (ImageView) findViewById(R.id.koTitle);
        mKoImageView.setVisibility(View.INVISIBLE);

        mScanView = (ImageView)findViewById(R.id.scanVideo);
        mScanView.setVisibility(View.INVISIBLE);

        mScanView1 = (ImageView)findViewById(R.id.scanVideo2);
        mScanView1.setVisibility(View.INVISIBLE);

        mFigureView = (ImageView) findViewById(R.id.figure); //头像展示
        mFigureView.setVisibility(View.INVISIBLE);

        mUserCount = (TextView) findViewById(R.id.totalNum);
        mUserCount.setText(Integer.toString(mCount));

        //主界面1图片旋转效果
        mBack1View = (ImageView) findViewById(R.id.back1);
        mBack2View = (ImageView) findViewById(R.id.back2);

        mSettingButton = (Button) findViewById(R.id.btnSettings);

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex < 180) {
                    if (mMainLayout.getVisibility() == View.VISIBLE) { //只有当主界面1存在时才会走该分支
                        mBack1View.setRotation(mIndex * -2);
                        mBack2View.setRotation(mIndex * 2);
                    }
                    mIndex++;
                }
                else {
                    mIndex = 0;
                }

                if (mPlayIndex != -1) {
                    if (mPlayIndex - mIndex == -60 || mPlayIndex - mIndex == 60) {
                        ChangeActivity();
                        mPlayIndex = -1;
                    }
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 100);
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
        //mSurfaceView.setVisibility(View.INVISIBLE);
        mCameraSurfaceHolder.setCameraSurfaceHolder(this, mSurfaceView);
    }

    //请求百度AccessToken
    public String PostAccessToken() {
        HttpURLConnection httpURLConnection = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("grant_type=").append(GrantType).append("&");
        buffer.append("client_id=").append(ClientId).append("&");
        buffer.append("client_secret=").append(ClientSecret);
        byte[] mydata = buffer.toString().getBytes();
        try {

            URL url = new URL(AccessTokenUrl + "?" + buffer);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(7000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream is = httpURLConnection.getInputStream();
                String state = ParseAccessToken(is);
                //RequestFaceInfo(state);
                return state;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    //解析AccessToken信息
    private String ParseAccessToken(InputStream is) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = is.read(buff))!=-1) {
            outputStream.write(buff, 0, len);
        }
        is.close();

        String html = outputStream.toString();
        JSONObject object = new JSONObject(html);
        mAccessToken = object.getString("access_token");
        outputStream.close();
        return html;
    }

    public String GetAccessToken()  {
        return mAccessToken;
    }

    public boolean GetIsScan() { return mIsScan; }

    //触碰屏幕
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: //触碰屏幕
                if (mSettingButton.getVisibility() == View.INVISIBLE) {
                    if (mTouchScreen == true) {
                        mSettingButton.setVisibility(View.VISIBLE);
                    } else {
                        mTouchScreen = true;
                    }
                }
                else if (mSettingButton.getVisibility() == View.VISIBLE) {
                    mSettingButton.setVisibility(View.INVISIBLE);
                }

                mSettingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                });
                break;
        }
        return true;
    }


    //转换模式----连续三次是同一个模式则进入扫描阶段
    public void ChangeViewMode(String faceInfo) { //解析图片完成进行等待两秒之后
        if (mIsScan) {
            return;
        }
        mStrFaceInfo = faceInfo;
        mMainLayout.setVisibility(View.INVISIBLE);
        mSurfaceView.setVisibility(View.VISIBLE);
        int faceNum = GetFaceNum(faceInfo);

        if (faceNum == 1) {
            mTitleLayout.setVisibility(View.VISIBLE);
            mKoImageView.setVisibility(View.INVISIBLE);
        } else if (faceNum == 2) {
            mKoImageView.setVisibility(View.VISIBLE);
            mTitleLayout.setVisibility(View.INVISIBLE);
        }

        if (mFaceCount != faceNum) {
            mFaceCount = faceNum;
        }
        else {
            mFaceCountInsistTime++;
        }

        if (mFaceCountInsistTime > 2) { // 进入扫描阶段
            ScanFigure(faceInfo);
            mIsScan = true;
        }

    }

    //扫描人像
    public void ScanFigure(String faceinfo) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image/face.jpg";
        mFigureView.setImageBitmap(handleBitmap.GetNormalBitmap(path));

        mFigureView.setVisibility(View.VISIBLE);
        mSurfaceView.setVisibility(View.INVISIBLE);

        SetUserCount();

        mPlayIndex = mIndex;

        try {
            JSONObject object = new JSONObject(faceinfo);
            String resultMsg = object.getString("result");
            JSONObject objFaceInfo = new JSONObject(resultMsg);
            String listMsg = objFaceInfo.getString("face_list");
            JSONArray arrayFaceList = new JSONArray(listMsg);
            if (mFaceCount == 1) { //单人模式
                JSONObject faceInfo = arrayFaceList.getJSONObject(0);
                String location = faceInfo.getString("location");
                Toast.makeText(getApplicationContext(), location, Toast.LENGTH_LONG).show();
                JSONObject objLocation = new JSONObject(location);
                mPlayer1Left = objLocation.getInt("left");
                mPlayer1Top = objLocation.getInt("top");
                mPlayer1Width = objLocation.getInt("width");
                mPlayer1Height = objLocation.getInt("height");
            }
            else if (mFaceCount == 2) { //双人模式
                JSONObject player1Info = arrayFaceList.getJSONObject(0);
                JSONObject player2Info = arrayFaceList.getJSONObject(1);
                String location1 = player1Info.getString("location");
                String location2 = player2Info.getString("location");
                JSONObject objLocation1 = new JSONObject(location1);
                JSONObject objLocation2 = new JSONObject(location2);
                mPlayer1Left = objLocation1.getInt("left");
                mPlayer1Top = objLocation1.getInt("top");
                mPlayer1Width = objLocation1.getInt("width");
                mPlayer1Height = objLocation1.getInt("height");
                mPlayer2Left = objLocation2.getInt("left");
                mPlayer2Top = objLocation2.getInt("top");
                mPlayer2Width = objLocation2.getInt("width");
                mPlayer2Height = objLocation2.getInt("height");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        RelativeLayout.LayoutParams params;
        params = (RelativeLayout.LayoutParams) mScanView.getLayoutParams();
        params.leftMargin = mPlayer1Left;
        params.topMargin = mPlayer1Top;
        params.height = mPlayer1Height;
        params.width = mPlayer1Width;
        mScanView.setVisibility(View.VISIBLE);
        ((AnimationDrawable)mScanView.getBackground()).start();

        if (mFaceCount == 2) { //双人模式添加第二个扫描画面
            RelativeLayout.LayoutParams params1;
            params1 = (RelativeLayout.LayoutParams) mScanView1.getLayoutParams();
            params1.leftMargin = mPlayer2Left;
            params1.topMargin = mPlayer2Top;
            params1.height = mPlayer2Height;
            params1.width = mPlayer2Width;
            mScanView1.setVisibility(View.VISIBLE);
            ((AnimationDrawable)mScanView1.getBackground()).start();
        }
    }

    //切换界面信息
    public void ChangeActivity(){
        if (mFaceCount == 1) {
            Intent intent = new Intent(MainActivity.this, faceAnalysisActivity.class);
           // Intent intent = new Intent(MainActivity.this, QRCodeActivity.class);
            intent.putExtra("faceInfo", mStrFaceInfo);
            intent.putExtra("player1Left", Integer.toString(mPlayer1Left));
            intent.putExtra("player1Top", Integer.toString(mPlayer1Top));
            intent.putExtra("player1Width", Integer.toString(mPlayer1Width));
            intent.putExtra("player1Height", Integer.toString(mPlayer1Height));
            startActivity(intent);
        } else if (mFaceCount == 2) {
            Intent intent = new Intent(MainActivity.this, PKActivity.class);
            intent.putExtra("faceInfo", mStrFaceInfo);
            startActivity(intent);
        }
    }


    public int GetFaceNum(String info) {
        int faceNum = 0;
        try {
            JSONObject object = new JSONObject(info);
            String resultMsg = object.getString("result");
            JSONObject objFaceInfo = new JSONObject(resultMsg);
            faceNum = objFaceInfo.getInt("face_num");

            String listMsg = objFaceInfo.getString("face_list");
            JSONArray arrayFaceList = new JSONArray(listMsg);
            if (faceNum == 1) { //单人模式
                JSONObject faceInfo = arrayFaceList.getJSONObject(0);
                String location = faceInfo.getString("location");
                JSONObject objLocation = new JSONObject(location);
                mPlayer1Left = objLocation.getInt("left");
                mPlayer1Top = objLocation.getInt("top");
                mPlayer1Width = objLocation.getInt("width");
                mPlayer1Height = objLocation.getInt("height");
            }
            else if (faceNum == 2) { //双人模式
                JSONObject player1Info = arrayFaceList.getJSONObject(0);
                JSONObject player2Info = arrayFaceList.getJSONObject(1);
                String location1 = player1Info.getString("location");
                String location2 = player1Info.getString("location");
                JSONObject objLocation1 = new JSONObject(location1);
                JSONObject objLocation2 = new JSONObject(location1);
                mPlayer1Left = objLocation1.getInt("left");
                mPlayer1Top = objLocation1.getInt("top");
                mPlayer1Width = objLocation1.getInt("width");
                mPlayer1Height = objLocation1.getInt("height");
                mPlayer2Left = objLocation2.getInt("left");
                mPlayer2Top = objLocation2.getInt("top");
                mPlayer2Width = objLocation2.getInt("width");
                mPlayer2Height = objLocation2.getInt("height");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceNum;
    }

    //计算使用人次
    public void SetUserCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        int count = 0;
        if (mFaceCount == 1) {
            count = sharedPreferences.getInt("count", 0);
            editor.putInt("count", count + 1);
        } else if (mFaceCount == 2) {
            count = sharedPreferences.getInt("round", 0);
            editor.putInt("round", count + 1);
        }
        editor.apply();
        //mUserCount.setText(String.valueOf(mCount + 1));
    }
}
