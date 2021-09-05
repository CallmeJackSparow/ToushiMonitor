package com.example.johnw.toushimonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.johnw.toushimonitor.camera.CameraSurfaceHolder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CameraActivity extends Activity {

    private SurfaceView mainCamera;
    private Button mButton;
    CameraSurfaceHolder mCameraSurfaceHolder = new CameraSurfaceHolder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_camera);

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int mCount = sharedPreferences.getInt("count", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("count", mCount + 1);
        editor.apply();

        mainCamera = (SurfaceView) findViewById(R.id.cameraSurface);
        //mButton = findViewById(R.id.btn_takephoto);

        TextView peopleCountInfo = (TextView) findViewById(R.id.totalNum);
        peopleCountInfo.setText(String.valueOf(mCount));

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                //这边需要获取最后的一张图片
//                mCameraSurfaceHolder.callback.capturePicture = true;
//                Intent intent = new Intent(CameraActivity.this, faceAnalysisActivity.class);
//                //String picInfo = mCameraSurfaceHolder.callback.mCameraBitmap.toString()
//
//                intent.putExtra("picBase64", (mCameraSurfaceHolder.callback.testString));
////                if (mCameraSurfaceHolder.callback.mCameraBitmap != null) {
////                    String picInfo = mCameraSurfaceHolder.callback.mCameraBitmap.toString();//BitmapToBase64(mCameraSurfaceHolder.callback.mCameraBitmap);
////                    intent.putExtra("picBase64", picInfo);
////                }
//                startActivity(intent);
            }
        });
    }

    public String BitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baos.flush();
            baos.close();
            byte[] bitmapBytes = baos.toByteArray();
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
