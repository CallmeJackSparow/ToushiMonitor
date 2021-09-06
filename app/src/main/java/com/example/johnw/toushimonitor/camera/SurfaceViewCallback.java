package com.example.johnw.toushimonitor.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Toast;

import com.example.johnw.toushimonitor.MainActivity;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 相机界面SurfaceView的回调类
 */
public final class SurfaceViewCallback implements SurfaceHolder.Callback, Camera.PreviewCallback, FaceTask.OnSuccessListener {

    Context context;
    static final String TAG = "Camera";
    FrontCamera mFrontCamera = new FrontCamera();
    boolean previewing = mFrontCamera.getPreviewing();
    Camera mCamera;
    FaceTask mFaceTask;
    private int mIndex = 0;
    private int mCountDown = 0;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public void setContext(Context context) {
        this.context = context;
        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex < 50) {
                    mIndex++;
                }
            }
        };
        mTimer.schedule(mTimerTask, 0, 30);
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (previewing) {
            mCamera.stopPreview();
            Log.i(TAG, "停止预览");
        }

        try {
            mCamera.setPreviewDisplay(arg0);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
            
            Log.i(TAG, "开始预览");
            //调用旋转屏幕时自适应
            //setCameraDisplayOrientation(MainActivity.this, mCurrentCamIndex, mCamera);
        } catch (Exception e) {
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        //初始化前置摄像头
        mFrontCamera.setCamera(mCamera);
        mCamera = mFrontCamera.initCamera();
        mCamera.setPreviewCallback(this);
        //适配竖排固定角度
        FrontCamera.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);

        Log.i(TAG, "surfaceCreated");
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mFrontCamera.StopCamera(mCamera);
        Log.i(TAG, "surfaceDestroyed");
    }

    /**
     * 相机实时数据的回调
     *
     * @param data   相机获取的数据，格式是YUV
     * @param camera 相应相机的对象
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mIndex == 50) {
            mIndex = 0;
            if (mFaceTask != null) {
                switch (mFaceTask.getStatus()) {
                    case RUNNING:
                        return;
                    case PENDING:
                        mFaceTask.cancel(false);
                        break;
                }
            }
            byte[] buf = rotateYUV420Degree90(data, mCamera.getParameters().getPreviewSize().width, mCamera.getParameters().getPreviewSize().height);
            mFaceTask = new FaceTask(buf, mCamera, context, mCountDown);
            mFaceTask.setOnSuccessListener(this);
            mFaceTask.execute("");
        }
    }

    @Override
    public void onSuccess() {
        mCountDown += 1;
    }


    private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight)
    {
        byte [] yuv = new byte[imageWidth * imageHeight * 3 / 2];
        // Rotate the Y luma
        int i = 0;

        for (int x = imageWidth - 1; x >= 0; x--) {
            for (int y = 0; y < imageHeight; y++) {
                yuv[i] = data[y * imageWidth + x];
                i++;
            }
        }
//        for (int x = 0; x < imageWidth; x++) {
//            for (int y = imageHeight - 1; y >= 0; y--) {
//                yuv[i] = data[y * imageWidth + x];
//                i++;
//            }
//        }
        // Rotate the U and V color components
        //i = imageWidth * imageHeight * 3 / 2 - 1;
        for (int x = imageWidth - 1; x > 1; x = x - 2) {
            for (int y = 0; y < imageHeight / 2; y++) {
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
                i++;
                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + x];
                i++;
            }
        }
//        for (int x = imageWidth - 1; x > 0; x = x - 2) {
//            for (int y = 0; y < imageHeight / 2; y++) {
//                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth)+ x];
//                i--;
//                yuv[i] = data[(imageWidth * imageHeight) + (y * imageWidth) + (x - 1)];
//                i--;
//            }
//        }
        return yuv;
    }
}