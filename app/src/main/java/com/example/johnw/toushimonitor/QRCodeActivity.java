package com.example.johnw.toushimonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import com.example.johnw.toushimonitor.camera.HandleBitmap;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView mQrBackground;
    private ImageView mImageView;
    private ImageView mQrView;
    private ImageView mCountDownView;
    private Timer mTimer;
    private TimerTask mTimerTask;
    int mIndex = 0;

    HandleBitmap handleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        mCountDownView = (ImageView) findViewById(R.id.qr_countdown);

        mQrBackground = (ImageView) findViewById(R.id.qrBackground);
        mQrView = (ImageView) findViewById(R.id.qr_image);
        mImageView = (ImageView) findViewById(R.id.scoreImage);
        String imagePath = getExternalFilesDir(null).getAbsolutePath() + "/image/screenshot.png";
        String qtPath = getExternalFilesDir(null).getAbsolutePath() + "/image/qrImage.jpg";
        Bitmap bitmap = handleBitmap.scaleBitmap(imagePath);
        Bitmap qrBitmap = handleBitmap.qrImageBitmap(qtPath);
        mImageView.setImageBitmap(bitmap);
        mQrView.setImageBitmap(qrBitmap);
        mQrBackground.setImageBitmap(bitmap);
//        Thread imageViewHandler = new Thread(new QrImageHandler());
//        imageViewHandler.start();

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex == 0) {
                    mCountDownView.setBackgroundResource(0);
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s8));
                }
                if (mIndex == 1) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s7));
                }
                if (mIndex == 2) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s6));
                }
                if (mIndex == 3) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s5));
                }
                if (mIndex == 4) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s4));
                }
                if (mIndex == 5) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s3));
                }
                if (mIndex == 6) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s2));
                }
                if (mIndex == 7) {
                    mCountDownView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.s1));
                }
                if (mIndex == 9) {
                    ChangeActivity();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    private void ChangeActivity() {
        Intent intent = new Intent(QRCodeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
