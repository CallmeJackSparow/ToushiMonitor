package com.example.johnw.toushimonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.johnw.toushimonitor.camera.HandleBitmap;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;


public class MenGodRankActivity extends AppCompatActivity {

    private LinearLayout mRankLayout;

    private Timer mTimer;
    private TimerTask mTimerTask;
    private ImageView mRank1Circle;
    private ImageView mRank2Circle;
    private ImageView mRank3Circle;
    private ImageView mRank1Head;
    private ImageView mRank2Head;
    private ImageView mRank3Head;
    private TextView mRank1LabelScore;
    private TextView mRank2LabelScore;
    private TextView mRank3LabelScore;

    private ImageView mReward1Circle;
    private ImageView mReward2Circle;
    private ImageView mReward3Circle;
    private ImageView mReward4Circle;
    private ImageView mReward5Circle;
    private ImageView mReward1Head;
    private ImageView mReward2Head;
    private ImageView mReward3Head;
    private ImageView mReward4Head;
    private ImageView mReward5Head;

    private ImageView mReward1ProcessBase;
    private ImageView mReward2ProcessBase;
    private ImageView mReward3ProcessBase;
    private ImageView mReward4ProcessBase;
    private ImageView mReward5ProcessBase;
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

    private int mRank1;
    private int mRank2;
    private int mRank3;
    private int mRank4;
    private int mRank5;
    ViewGroup.LayoutParams mParams1;
    ViewGroup.LayoutParams mParams2;
    ViewGroup.LayoutParams mParams3;
    ViewGroup.LayoutParams mParams4;
    ViewGroup.LayoutParams mParams5;

    private int mProgressBarLength;

    int mDeviceId;
    String mFileUrl = "";

    HandleBitmap handleBitmap;
    int mIndex = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_men_god_rank);

        mRankLayout = (LinearLayout) findViewById(R.id.popupWindow);
        Animation animation = AnimationUtils.loadAnimation(MenGodRankActivity.this, R.anim.animation);
        mRankLayout.startAnimation(animation);
        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int mCount = sharedPreferences.getInt("count", 0);
        mDeviceId = sharedPreferences.getInt("deviceId",1);

        mRank1 = sharedPreferences.getInt("manRank1", 0);
        mRank2 = sharedPreferences.getInt("manRank2", 0);
        mRank3 = sharedPreferences.getInt("manRank3", 0);
        mRank4 = sharedPreferences.getInt("manRank4", 0);
        mRank5 = sharedPreferences.getInt("manRank5", 0);

        TextView totalPeopleCount = (TextView) findViewById(R.id.totalNum2);
        totalPeopleCount.setText(String.valueOf(mCount));

        mRankLayout = (LinearLayout) findViewById(R.id.popupWindow);

        //排行榜内框
        mRank1Head = (ImageView) findViewById(R.id.ManRank1Image);
        mRank2Head = (ImageView) findViewById(R.id.ManRank2Image);
        mRank3Head = (ImageView) findViewById(R.id.ManRank3Image);
        //排行榜外框
        mRank1Circle = (ImageView) findViewById(R.id.manRank1);
        mRank2Circle = (ImageView) findViewById(R.id.manRank2);
        mRank3Circle = (ImageView) findViewById(R.id.manRank3);
        //排行榜成绩
        mRank1LabelScore = (TextView) findViewById(R.id.manRank1ScoreLabel);
        mRank2LabelScore = (TextView) findViewById(R.id.manRank2ScoreLabel);
        mRank3LabelScore = (TextView) findViewById(R.id.manRank3ScoreLabel);
        //滚动条头像底框
        mReward1Circle = (ImageView) findViewById(R.id.reward1);
        mReward2Circle = (ImageView) findViewById(R.id.reward2);
        mReward3Circle = (ImageView) findViewById(R.id.reward3);
        mReward4Circle = (ImageView) findViewById(R.id.reward4);
        mReward5Circle = (ImageView) findViewById(R.id.reward5);
        //滚动条头像表框
        mReward1Head = (ImageView) findViewById(R.id.reward1head);
        mReward2Head = (ImageView) findViewById(R.id.reward2head);
        mReward3Head = (ImageView) findViewById(R.id.reward3head);
        mReward4Head = (ImageView) findViewById(R.id.reward4head);
        mReward5Head = (ImageView) findViewById(R.id.reward5head);
        //滚动条底层
        mReward1ProcessBase = (ImageView) findViewById(R.id.reward1Image);
        mReward2ProcessBase = (ImageView) findViewById(R.id.reward2Image);
        mReward3ProcessBase = (ImageView) findViewById(R.id.reward3Image);
        mReward4ProcessBase = (ImageView) findViewById(R.id.reward4Image);
        mReward5ProcessBase = (ImageView) findViewById(R.id.reward5Image);
        //滚动条表层
        mReward1ProgreessBar = (ImageView) findViewById(R.id.manReward1ProcessBar);
        mReward2ProgreessBar = (ImageView) findViewById(R.id.manReward2ProcessBar);
        mReward3ProgreessBar = (ImageView) findViewById(R.id.manReward3ProcessBar);
        mReward4ProgreessBar = (ImageView) findViewById(R.id.manReward4ProcessBar);
        mReward5ProgreessBar = (ImageView) findViewById(R.id.manReward5ProcessBar);


        if (mRank1 != 0) {
            mRank1Head.setVisibility(View.VISIBLE);
            mRank1Circle.setVisibility(View.VISIBLE);
            mReward1Circle.setVisibility(View.VISIBLE);
            mReward1Head.setVisibility(View.VISIBLE);
            mReward1ProcessBase.setVisibility(View.VISIBLE);
            mReward1ProgreessBar.setVisibility(View.VISIBLE);
        }
        if (mRank2 != 0) {
            mRank2Head.setVisibility(View.VISIBLE);
            mRank2Circle.setVisibility(View.VISIBLE);
            mReward2Circle.setVisibility(View.VISIBLE);
            mReward2Head.setVisibility(View.VISIBLE);
            mReward2ProcessBase.setVisibility(View.VISIBLE);
            mReward2ProgreessBar.setVisibility(View.VISIBLE);
        }
        if (mRank3 != 0) {
            mRank3Head.setVisibility(View.VISIBLE);
            mRank3Circle.setVisibility(View.VISIBLE);
            mReward3Circle.setVisibility(View.VISIBLE);
            mReward3Head.setVisibility(View.VISIBLE);
            mReward3ProcessBase.setVisibility(View.VISIBLE);
            mReward3ProgreessBar.setVisibility(View.VISIBLE);
        }
        if (mRank4 != 0) {
            mReward4Circle.setVisibility(View.VISIBLE);
            mReward4Head.setVisibility(View.VISIBLE);
            mReward4ProcessBase.setVisibility(View.VISIBLE);
            mReward4ProgreessBar.setVisibility(View.VISIBLE);
        }
        if (mRank5 != 0) {
            mReward5Circle.setVisibility(View.VISIBLE);
            mReward5Head.setVisibility(View.VISIBLE);
            mReward5ProcessBase.setVisibility(View.VISIBLE);
            mReward5ProgreessBar.setVisibility(View.VISIBLE);
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
        mReward1ProgreessBar.startAnimation(scaleAnimation1);
        mReward2ProgreessBar.startAnimation(scaleAnimation2);
        mReward3ProgreessBar.startAnimation(scaleAnimation3);
        mReward4ProgreessBar.startAnimation(scaleAnimation4);
        mReward5ProgreessBar.startAnimation(scaleAnimation5);

        ShowScore();

        String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank1.jpg";
        String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank2.jpg";
        String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank3.jpg";
        String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank4.jpg";
        String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank5.jpg";
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
                if (mIndex == 8) {
                    ChangeToQR();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void ShowScore() {
        mRank1Score = (TextView) findViewById(R.id.manRank1Score);
        mRank2Score = (TextView) findViewById(R.id.manRank2Score);
        mRank3Score = (TextView) findViewById(R.id.manRank3Score);
        mRank4Score = (TextView) findViewById(R.id.manRank4Score);
        mRank5Score = (TextView) findViewById(R.id.manRank5Score);
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
            mRank3LabelScore.setText(String.valueOf(mRank3));
        }
        if (mRank4 > 0) {
            mRank4Score.setText(String.valueOf(mRank4));
        }
        if (mRank5 > 0) {
            mRank4Score.setText(String.valueOf(mRank5));
        }
    }


    public void ChangeToQR() {
//        mTimer.cancel();
//        mTimerTask.cancel();
        Intent intent = new Intent(MenGodRankActivity.this, QRCodeActivity.class);
        startActivity(intent);
    }
}
