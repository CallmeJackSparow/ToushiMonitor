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
            mFaceTask = new FaceTask(data, mCamera, context, mCountDown);
            mFaceTask.setOnSuccessListener(this);
            //Toast.makeText(context, "开始进行人脸分析 ", Toast.LENGTH_SHORT).show();
            mFaceTask.execute("");
        }
    }

    @Override
    public void onSuccess() {
        mCountDown += 1;
    }
}