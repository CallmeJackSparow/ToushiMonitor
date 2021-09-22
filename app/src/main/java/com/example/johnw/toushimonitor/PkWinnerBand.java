package com.example.johnw.toushimonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.johnw.toushimonitor.camera.HandleBitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class PkWinnerBand extends AppCompatActivity {

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

    private ImageView mReward1Base;
    private ImageView mReward2Base;
    private ImageView mReward3Base;
    private ImageView mReward4Base;
    private ImageView mReward5Base;
    private ImageView mReward1Head;
    private ImageView mReward2Head;
    private ImageView mReward3Head;
    private ImageView mReward4Head;
    private ImageView mReward5Head;

    private ImageView mProgressBase1;
    private ImageView mProgressBase2;
    private ImageView mProgressBase3;
    private ImageView mProgressBase4;
    private ImageView mProgressBase5;
    private ImageView mProgressBar1;
    private ImageView mProgressBar2;
    private ImageView mProgressBar3;
    private ImageView mProgressBar4;
    private ImageView mProgressBar5;

    private TextView mRank1Score;
    private TextView mRank2Score;
    private TextView mRank3Score;
    private TextView mRank4Score;
    private TextView mRank5Score;

    int mDeviceId;
    String mFileUrl = "";

    HandleBitmap handleBitmap;
    int mIndex = 0;

    int mRank1;
    int mRank2;
    int mRank3;
    int mRank4;
    int mRank5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pk_winner_band);

        mRankLayout = (LinearLayout) findViewById(R.id.pkPopupWindow);
        Animation animation = AnimationUtils.loadAnimation(PkWinnerBand.this, R.anim.animation);
        mRankLayout.startAnimation(animation);
        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int mCount = sharedPreferences.getInt("round", 0);
        mDeviceId = sharedPreferences.getInt("deviceId",1);

        mRank1 = sharedPreferences.getInt("pkRank1", 0);
        mRank2 = sharedPreferences.getInt("pkRank2", 0);
        mRank3 = sharedPreferences.getInt("pkRank3", 0);
        mRank4 = sharedPreferences.getInt("pkRank4", 0);
        mRank5 = sharedPreferences.getInt("pkRank5", 0);

        TextView roundNum = (TextView) findViewById(R.id.pkRoundNum);
        roundNum.setText(String.valueOf(mCount));

        mRankLayout = (LinearLayout) findViewById(R.id.pkPopupWindow);

        mRank1Head = (ImageView) findViewById(R.id.pkRank1Image);
        mRank2Head = (ImageView) findViewById(R.id.pkRank2Image);
        mRank3Head = (ImageView) findViewById(R.id.pkRank3Image);
        mRank1Circle = (ImageView) findViewById(R.id.pkRank1);
        mRank2Circle = (ImageView) findViewById(R.id.pkRank2);
        mRank3Circle = (ImageView) findViewById(R.id.pkRank3);

        mRank1LabelScore = (TextView) findViewById(R.id.pkRank1ScoreLabel);
        mRank2LabelScore = (TextView) findViewById(R.id.pkRank2ScoreLabel);
        mRank3LabelScore = (TextView) findViewById(R.id.pkRank3ScoreLabel);

        mReward1Base = (ImageView) findViewById(R.id.pkReward1);
        mReward2Base = (ImageView) findViewById(R.id.pkReward2);
        mReward3Base = (ImageView) findViewById(R.id.pkReward3);
        mReward4Base = (ImageView) findViewById(R.id.pkReward4);
        mReward5Base = (ImageView) findViewById(R.id.pkReward5);
        mReward1Head = (ImageView) findViewById(R.id.pkReward1head);
        mReward2Head = (ImageView) findViewById(R.id.pkReward2head);
        mReward3Head = (ImageView) findViewById(R.id.pkReward3head);
        mReward4Head = (ImageView) findViewById(R.id.pkReward4head);
        mReward5Head = (ImageView) findViewById(R.id.pkReward5head);

        mProgressBase1 = (ImageView) findViewById(R.id.pkReward1Image);
        mProgressBase2 = (ImageView) findViewById(R.id.pkReward2Image);
        mProgressBase3 = (ImageView) findViewById(R.id.pkReward3Image);
        mProgressBase4 = (ImageView) findViewById(R.id.pkReward4Image);
        mProgressBase5 = (ImageView) findViewById(R.id.pkReward5Image);
        mProgressBar1 = (ImageView) findViewById(R.id.pkProcessBar1);
        mProgressBar2 = (ImageView) findViewById(R.id.pkProcessBar2);
        mProgressBar3 = (ImageView) findViewById(R.id.pkProcessBar3);
        mProgressBar4 = (ImageView) findViewById(R.id.pkProcessBar4);
        mProgressBar5 = (ImageView) findViewById(R.id.pkProcessBar5);

        if (mRank1 > 0) {
            mRank1Head.setVisibility(View.VISIBLE);
            mRank1Circle.setVisibility(View.VISIBLE);
            mReward1Base.setVisibility(View.VISIBLE);
            mReward1Head.setVisibility(View.VISIBLE);
            mProgressBase1.setVisibility(View.VISIBLE);
            mProgressBar1.setVisibility(View.VISIBLE);
        }
        if (mRank2 > 0) {
            mRank2Head.setVisibility(View.VISIBLE);
            mRank2Circle.setVisibility(View.VISIBLE);
            mReward2Base.setVisibility(View.VISIBLE);
            mReward2Head.setVisibility(View.VISIBLE);
            mProgressBase2.setVisibility(View.VISIBLE);
            mProgressBar2.setVisibility(View.VISIBLE);
        }
        if (mRank3 > 0) {
            mRank3Head.setVisibility(View.VISIBLE);
            mRank3Circle.setVisibility(View.VISIBLE);
            mReward3Base.setVisibility(View.VISIBLE);
            mReward3Head.setVisibility(View.VISIBLE);
            mProgressBase3.setVisibility(View.VISIBLE);
            mProgressBar3.setVisibility(View.VISIBLE);
        }
        if (mRank4 > 0) {
            mReward4Base.setVisibility(View.VISIBLE);
            mReward4Head.setVisibility(View.VISIBLE);
            mProgressBase4.setVisibility(View.VISIBLE);
            mProgressBar4.setVisibility(View.VISIBLE);
        }
        if (mRank5 > 0) {
            mReward5Base.setVisibility(View.VISIBLE);
            mReward5Head.setVisibility(View.VISIBLE);
            mProgressBase5.setVisibility(View.VISIBLE);
            mProgressBar5.setVisibility(View.VISIBLE);
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
        mProgressBar1.startAnimation(scaleAnimation1);
        mProgressBar2.startAnimation(scaleAnimation2);
        mProgressBar3.startAnimation(scaleAnimation3);
        mProgressBar4.startAnimation(scaleAnimation4);
        mProgressBar5.startAnimation(scaleAnimation5);

        ShowScore();

        mRank1Score = (TextView) findViewById(R.id.pkRank1Score);
        mRank2Score = (TextView) findViewById(R.id.pkRank2Score);
        mRank3Score = (TextView) findViewById(R.id.pkRank3Score);
        mRank4Score = (TextView) findViewById(R.id.pkRank4Score);
        mRank5Score = (TextView) findViewById(R.id.pkRank5Score);

        String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank1.jpg";
        String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank2.jpg";
        String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank3.jpg";
        String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank4.jpg";
        String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank5.jpg";
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
        mRank1Score = (TextView) findViewById(R.id.pkRank1Score);
        mRank2Score = (TextView) findViewById(R.id.pkRank2Score);
        mRank3Score = (TextView) findViewById(R.id.pkRank3Score);
        mRank4Score = (TextView) findViewById(R.id.pkRank4Score);
        mRank5Score = (TextView) findViewById(R.id.pkRank5Score);

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
            mRank5Score.setText(String.valueOf(mRank5));
        }





    }

    public void ChangeToQR() {
        mTimer.cancel();
        mTimerTask.cancel();
        Intent intent = new Intent(PkWinnerBand.this, QRCodeActivity.class);
        startActivity(intent);
    }
}