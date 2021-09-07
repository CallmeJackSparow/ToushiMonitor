package com.example.johnw.toushimonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
//import io.vov.vitamio.MediaPlayer;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;

import com.example.johnw.toushimonitor.camera.HandleBitmap;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView mImageView;
    private ImageView mQrView;
    private TextView mCountDownText;
    private Timer mTimer;
    private TimerTask mTimerTask;
    int mIndex = 0;
    Bitmap mBitmap = null;
    String mQrImage;

    HandleBitmap handleBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        mCountDownText = (TextView) findViewById(R.id.countdown);

//        if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this)) {
//
//
//        }

        mQrView = (ImageView) findViewById(R.id.qr_image);
        mImageView = (ImageView) findViewById(R.id.scoreImage);
        String imagePath = getExternalFilesDir(null).getAbsolutePath() + "/image/screenshot.png";
        String qtPath = getExternalFilesDir(null).getAbsolutePath() + "/image/qrImage.jpg";
        Bitmap bitmap = handleBitmap.scaleBitmap(imagePath);
        Bitmap qrBitmap = handleBitmap.qrImageBitmap(qtPath);
        mImageView.setImageBitmap(bitmap);
        mQrView.setImageBitmap(qrBitmap);
        Thread imageViewHandler = new Thread(new QrImageHandler());
        imageViewHandler.start();

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mCountDownText.setText(String.valueOf(9 - mIndex));
                if (mIndex == 9) {
                    ChangeActivity();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    class QrImageHandler implements Runnable {
        @Override
        public void run () {
            try {
                URL url = new URL(mQrImage);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5000);

                if (conn.getResponseCode() == 200) {
                    InputStream is = conn.getInputStream();
                    mBitmap = BitmapFactory.decodeStream(is);
                }
                //发送消息，通知UI组件显示图片
                handler.sendEmptyMessage(0);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                mQrView.setImageBitmap(mBitmap);
            }
        }
    };

    public void ChangeActivity() {
//        mTimer.cancel();
//        mTimerTask.cancel();
        Intent intent = new Intent(QRCodeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
