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
import android.view.animation.ScaleAnimation;
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
    private ImageView mRank1Circle;
    private ImageView mRank2Circle;
    private ImageView mRank3Circle;
    private TextView mRank1LabelScore;
    private TextView mRank2LabelScore;
    private TextView mRank3LabelScore;

    private ImageView mReward1Head;
    private ImageView mReward2Head;
    private ImageView mReward3Head;
    private ImageView mReward4Head;
    private ImageView mReward5Head;
    private ImageView mReward1Base;
    private ImageView mReward2Base;
    private ImageView mReward3Base;
    private ImageView mReward4Base;
    private ImageView mReward5Base;

    private ImageView mReward1ProgressBase;
    private ImageView mReward2ProgressBase;
    private ImageView mReward3ProgressBase;
    private ImageView mReward4ProgressBase;
    private ImageView mReward5ProgressBase;
    private ImageView mReward1ProgressBar;
    private ImageView mReward2ProgressBar;
    private ImageView mReward3ProgressBar;
    private ImageView mReward4ProgressBar;
    private ImageView mReward5ProgressBar;

    private TextView mRank1Score;
    private TextView mRank2Score;
    private TextView mRank3Score;
    private TextView mRank4Score;
    private TextView mRank5Score;

    int mDeviceId;

    int mRank1;
    int mRank2;
    int mRank3;
    int mRank4;
    int mRank5;

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

        mRank1 = sharedPreferences.getInt("womanRank1", 0);
        mRank2 = sharedPreferences.getInt("womanRank2", 0);
        mRank3 = sharedPreferences.getInt("womanRank3", 0);
        mRank4 = sharedPreferences.getInt("womanRank4", 0);
        mRank5 = sharedPreferences.getInt("womanRank5", 0);

        TextView totalPeopleCount = (TextView) findViewById(R.id.totalNum2);
        totalPeopleCount.setText(String.valueOf(mCount));

        mRank1Head = (ImageView) findViewById(R.id.womenRank1Image);
        mRank2Head = (ImageView) findViewById(R.id.womenRank2Image);
        mRank3Head = (ImageView) findViewById(R.id.womenRank3Image);
        mRank1Circle = (ImageView) findViewById(R.id.womenRank1);
        mRank2Circle = (ImageView) findViewById(R.id.womenRank2);
        mRank3Circle = (ImageView) findViewById(R.id.womenRank3);
        mRank1LabelScore = (TextView) findViewById(R.id.womanRank1ScoreLabel);
        mRank2LabelScore = (TextView) findViewById(R.id.womanRank2ScoreLabel);
        mRank3LabelScore = (TextView) findViewById(R.id.womanRank3ScoreLabel);

        mReward1Head = (ImageView) findViewById(R.id.womenReward1head);
        mReward2Head = (ImageView) findViewById(R.id.womenReward2head);
        mReward3Head = (ImageView) findViewById(R.id.womenReward3head);
        mReward4Head = (ImageView) findViewById(R.id.womenReward4head);
        mReward5Head = (ImageView) findViewById(R.id.womenReward5head);
        mReward1Base = (ImageView) findViewById(R.id.womenReward1);
        mReward2Base = (ImageView) findViewById(R.id.womenReward2);
        mReward3Base = (ImageView) findViewById(R.id.womenReward3);
        mReward4Base = (ImageView) findViewById(R.id.womenReward4);
        mReward5Base = (ImageView) findViewById(R.id.womenReward5);

        mReward1ProgressBase = (ImageView) findViewById(R.id.womenReward1Image);
        mReward2ProgressBase = (ImageView) findViewById(R.id.womenReward2Image);
        mReward3ProgressBase = (ImageView) findViewById(R.id.womenReward3Image);
        mReward4ProgressBase = (ImageView) findViewById(R.id.womenReward4Image);
        mReward5ProgressBase = (ImageView) findViewById(R.id.womenReward5Image);
        mReward1ProgressBar = (ImageView) findViewById(R.id.womanReward1ProcessBar);
        mReward2ProgressBar = (ImageView) findViewById(R.id.womanReward2ProcessBar);
        mReward3ProgressBar = (ImageView) findViewById(R.id.womanReward3ProcessBar);
        mReward4ProgressBar = (ImageView) findViewById(R.id.womanReward4ProcessBar);
        mReward5ProgressBar = (ImageView) findViewById(R.id.womanReward5ProcessBar);

        if (mRank1 > 0) {
            mRank1Head.setVisibility(View.VISIBLE);
            mRank1Circle.setVisibility(View.VISIBLE);
            mReward1Head.setVisibility(View.VISIBLE);
            mReward1Base.setVisibility(View.VISIBLE);
            mReward1ProgressBase.setVisibility(View.VISIBLE);
            mReward1ProgressBar.setVisibility(View.VISIBLE);
        }
        if (mRank2 > 0) {
            mRank2Head.setVisibility(View.VISIBLE);
            mRank2Circle.setVisibility(View.VISIBLE);
            mReward2Head.setVisibility(View.VISIBLE);
            mReward2Base.setVisibility(View.VISIBLE);
            mReward2ProgressBase.setVisibility(View.VISIBLE);
            mReward2ProgressBar.setVisibility(View.VISIBLE);
        }
        if (mRank3 > 0) {
            mRank3Head.setVisibility(View.VISIBLE);
            mRank3Circle.setVisibility(View.VISIBLE);
            mReward3Head.setVisibility(View.VISIBLE);
            mReward3Base.setVisibility(View.VISIBLE);
            mReward3ProgressBase.setVisibility(View.VISIBLE);
            mReward3ProgressBar.setVisibility(View.VISIBLE);
        }
        if (mRank4 > 0) {
            mReward4Head.setVisibility(View.VISIBLE);
            mReward4Base.setVisibility(View.VISIBLE);
            mReward4ProgressBase.setVisibility(View.VISIBLE);
            mReward4ProgressBar.setVisibility(View.VISIBLE);
        }
        if (mRank5 > 0) {
            mReward5Head.setVisibility(View.VISIBLE);
            mReward5Base.setVisibility(View.VISIBLE);
            mReward5ProgressBase.setVisibility(View.VISIBLE);
            mReward5ProgressBar.setVisibility(View.VISIBLE);
        }

        float num1 = mRank1 / (float)100;
        float num2 = mRank2 / (float)100;
        float num3 = mRank3 / (float)100;
        float num4 = mRank4 / (float)100;
        float num5 = mRank5 / (float)100;
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(0, num1, 1, 1);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(0, num2, 1, 1);
        ScaleAnimation scaleAnimation3 = new ScaleAnimation(0, num3, 1, 1);
        ScaleAnimation scaleAnimation4 = new ScaleAnimation(0, num4, 1, 1);
        ScaleAnimation scaleAnimation5 = new ScaleAnimation(0, num5, 1, 1);
        scaleAnimation1.setFillAfter(true);
        scaleAnimation2.setFillAfter(true);
        scaleAnimation3.setFillAfter(true);
        scaleAnimation4.setFillAfter(true);
        scaleAnimation5.setFillAfter(true);
        scaleAnimation1.setDuration(1000);
        scaleAnimation2.setDuration(1000);
        scaleAnimation3.setDuration(1000);
        scaleAnimation4.setDuration(1000);
        scaleAnimation5.setDuration(1000);
        mReward1ProgressBar.startAnimation(scaleAnimation1);
        mReward2ProgressBar.startAnimation(scaleAnimation2);
        mReward3ProgressBar.startAnimation(scaleAnimation3);
        mReward4ProgressBar.startAnimation(scaleAnimation4);
        mReward5ProgressBar.startAnimation(scaleAnimation5);

        ShowScore();

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
        mRank2Head.setImageBitmap(rank2bitmap);
        mRank3Head.setImageBitmap(rank3bitmap);

        mReward1Head.setImageBitmap(rank1bitmap);
        mReward2Head.setImageBitmap(rank2bitmap);
        mReward3Head.setImageBitmap(rank3bitmap);
        mReward4Head.setImageBitmap(rank4bitmap);
        mReward5Head.setImageBitmap(rank5bitmap);

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex == 7) {
                    ChangeToQR();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void ShowScore() {
        mRank1Score = (TextView) findViewById(R.id.womanRank1Score);
        mRank2Score = (TextView) findViewById(R.id.womanRank2Score);
        mRank3Score = (TextView) findViewById(R.id.womanRank3Score);
        mRank4Score = (TextView) findViewById(R.id.womanRank4Score);
        mRank5Score = (TextView) findViewById(R.id.womanRank5Score);

        if (mRank1 > 0) {
            mRank1Score.setText(String.valueOf(mRank1));
            mRank1LabelScore.setText(String.valueOf(mRank1));
        }
        if (mRank2 > 0) {
            mRank2Score.setText(String.valueOf(mRank2));
            mRank2LabelScore.setText(String.valueOf(mRank2));
        }
        if (mRank3 > 0) {
            mRank3Score.setText(String.valueOf(mRank3));
            mRank3LabelScore.setText(String.valueOf(mRank2));
        }
        if (mRank4 > 0) {
            mRank4Score.setText(String.valueOf(mRank4));
        }
        if (mRank5 > 0) {
            mRank5Score.setText(String.valueOf(mRank5));
        }
    }

    public void ChangeToQR() {
        mTimer.cancel();
        mTimerTask.cancel();
        Intent intent = new Intent(WomenGodRankActivity.this, QRCodeActivity.class);
        startActivity(intent);
    }
}
