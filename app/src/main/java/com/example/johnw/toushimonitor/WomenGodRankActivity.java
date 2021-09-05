package com.example.johnw.toushimonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.johnw.toushimonitor.camera.HandleBitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class WomenGodRankActivity extends AppCompatActivity {

    private LinearLayout mRankLayout;
    private Timer mTimer;
    private TimerTask mTimerTask;
    int mIndex = 0;
    HandleBitmap handleBitmap;

    private ImageView mRank1Head;
    private ImageView mRank2Head;
    private ImageView mRank3Head;
    private TextView mRank1LabelScore;
    private TextView mRank2LabelScore;
    private TextView mRank3LabelScore;

    private ImageView mReward1Head;
    private ImageView mReward2Head;
    private ImageView mReward3Head;
    private ImageView mReward4Head;
    private ImageView mReward5Head;

    private ImageView mReward1ProgreessBar;
    private ImageView mReward2ProgreessBar;
    private ImageView mReward3ProgreessBar;
    private ImageView mReward4ProgreessBar;
    private ImageView mReward5ProgreessBar;

    private TextView mRank1Score;
    private TextView mRank2Score;
    private TextView mRank3Score;
    private TextView mRank4Score;
    private TextView mRank5Score;

    int mDeviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_women_god_rank);

        mRankLayout = (LinearLayout) findViewById(R.id.popupWindow);
        Animation animation = AnimationUtils.loadAnimation(WomenGodRankActivity.this, R.anim.animation);
        mRankLayout.startAnimation(animation);
        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int mCount = sharedPreferences.getInt("count", 0);
        mDeviceId = sharedPreferences.getInt("deviceId",1);

        int mRank1 = sharedPreferences.getInt("womanRank1", 0);
        int mRank2 = sharedPreferences.getInt("womanRank2", 0);
        int mRank3 = sharedPreferences.getInt("womanRank3", 0);
        int mRank4 = sharedPreferences.getInt("womanRank4", 0);
        int mRank5 = sharedPreferences.getInt("womanRank5", 0);

        TextView totalPeopleCount = (TextView) findViewById(R.id.totalNum2);
        totalPeopleCount.setText(String.valueOf(mCount));

        mRank1Head = (ImageView) findViewById(R.id.womenRank1Image);
        mRank2Head = (ImageView) findViewById(R.id.womenRank2Image);
        mRank3Head = (ImageView) findViewById(R.id.womenRank3Image);
        mRank1LabelScore = (TextView) findViewById(R.id.womanRank1ScoreLabel);
        mRank2LabelScore = (TextView) findViewById(R.id.womanRank2ScoreLabel);
        mRank3LabelScore = (TextView) findViewById(R.id.womanRank3ScoreLabel);

        mReward1Head = (ImageView) findViewById(R.id.womenReward1head);
        mReward2Head = (ImageView) findViewById(R.id.womenReward2head);
        mReward3Head = (ImageView) findViewById(R.id.womenReward3head);
        mReward4Head = (ImageView) findViewById(R.id.womenReward4head);
        mReward5Head = (ImageView) findViewById(R.id.womenReward5head);

        mReward1ProgreessBar = (ImageView) findViewById(R.id.womanReward1ProcessBar);
        mReward2ProgreessBar = (ImageView) findViewById(R.id.womanReward2ProcessBar);
        mReward3ProgreessBar = (ImageView) findViewById(R.id.womanReward3ProcessBar);
        mReward4ProgreessBar = (ImageView) findViewById(R.id.womanReward4ProcessBar);
        mReward5ProgreessBar = (ImageView) findViewById(R.id.womanReward5ProcessBar);

        ViewGroup.LayoutParams params;
        params = mReward1ProgreessBar.getLayoutParams();
        params.width = params.width * mRank1 / 100;
        mReward1ProgreessBar.setLayoutParams(params);
        params = mReward2ProgreessBar.getLayoutParams();
        params.width = params.width * mRank2 / 100;
        mReward2ProgreessBar.setLayoutParams(params);
        params = mReward3ProgreessBar.getLayoutParams();
        params.width = params.width  * mRank3 / 100;
        mReward3ProgreessBar.setLayoutParams(params);
        params = mReward4ProgreessBar.getLayoutParams();
        params.width = params.width  * mRank4 / 100;
        mReward4ProgreessBar.setLayoutParams(params);
        params = mReward5ProgreessBar.getLayoutParams();
        params.width = params.width * mRank5 / 100;
        mReward5ProgreessBar.setLayoutParams(params);

        mRank1Score = (TextView) findViewById(R.id.womanRank1Score);
        mRank2Score = (TextView) findViewById(R.id.womanRank2Score);
        mRank3Score = (TextView) findViewById(R.id.womanRank3Score);
        mRank4Score = (TextView) findViewById(R.id.womanRank4Score);
        mRank5Score = (TextView) findViewById(R.id.womanRank5Score);
        if (mRank1 > 0 && mRank1 < 100) {
            mRank1Score.setText(String.valueOf(mRank1));
            mRank1LabelScore.setText(String.valueOf(mRank1));
        }
        else {
            mRank1Score.setText("100");
            mRank1LabelScore.setText("100");
        }
        if (mRank2 > 0 && mRank2 < 100) {
            mRank2Score.setText(String.valueOf(mRank2));
            mRank2LabelScore.setText(String.valueOf(mRank2));
        }
        else {
            mRank2Score.setText("100");
            mRank2LabelScore.setText("100");
        }
        if (mRank3 > 0 && mRank3 < 100) {
            mRank3Score.setText(String.valueOf(mRank3));
            mRank3LabelScore.setText(String.valueOf(mRank2));
        }
        else {
            mRank3Score.setText("100");
            mRank3LabelScore.setText("100");
        }
        if (mRank4 > 0 && mRank4 < 100) {
            mRank4Score.setText(String.valueOf(mRank4));
        }
        else {
            mRank4Score.setText("100");
        }
        if (mRank5 > 0 && mRank5 < 100) {
            mRank5Score.setText(String.valueOf(mRank5));
        }
        else {
            mRank5Score.setText("100");
        }

        String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank1.jpg";
        String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank2.jpg";
        String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank3.jpg";
        String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank4.jpg";
        String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank5.jpg";
        Bitmap rank1bitmap = handleBitmap.getBitmap(rank1Image);
        Bitmap rank2bitmap = handleBitmap.getBitmap(rank2Image);
        Bitmap rank3bitmap = handleBitmap.getBitmap(rank3Image);
        Bitmap rank4bitmap = handleBitmap.getBitmap(rank4Image);
        Bitmap rank5bitmap = handleBitmap.getBitmap(rank5Image);
        mRank1Head.setImageBitmap(rank1bitmap);
        mRank1Head.setRotation(270);
        mRank2Head.setImageBitmap(rank2bitmap);
        mRank2Head.setRotation(270);
        mRank3Head.setImageBitmap(rank3bitmap);
        mRank3Head.setRotation(270);

        mReward1Head.setImageBitmap(rank1bitmap);
        mReward1Head.setRotation(270);
        mReward2Head.setImageBitmap(rank2bitmap);
        mReward2Head.setRotation(270);
        mReward3Head.setImageBitmap(rank3bitmap);
        mReward3Head.setRotation(270);
        mReward4Head.setImageBitmap(rank4bitmap);
        mReward4Head.setRotation(270);
        mReward5Head.setImageBitmap(rank5bitmap);
        mReward5Head.setRotation(270);

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex == 1) { //进入该界面3s之后,通过网络获取二维码
                    String fileurl = handleBitmap.GetQR(mDeviceId);
                    String path = getExternalFilesDir(null).getAbsolutePath()  + "/image/";
                    File saveFile = new File(path, "qrString.txt");
                    FileOutputStream outputStream1 = null;
                    try {
                        outputStream1 = new FileOutputStream(saveFile);
                        outputStream1.write(fileurl.getBytes("GBK"));
                        outputStream1.close();
                        outputStream1.flush();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    Bitmap bitmap = handleBitmap.GetNetworkBitmap(fileurl);
                    String qrPath = getExternalFilesDir(null).getAbsolutePath() + "/image/qrImage.jpg";
                    try {
                        File file = new File(qrPath);
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (mIndex == 7) {
                    ChangeToQR();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void ChangeToQR() {
        mTimer.cancel();
        mTimerTask.cancel();
        Intent intent = new Intent(WomenGodRankActivity.this, QRCodeActivity.class);
        startActivity(intent);
    }
}
