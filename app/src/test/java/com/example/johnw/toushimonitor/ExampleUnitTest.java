package com.example.johnw.toushimonitor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import org.junit.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    /**
     * Created by johnw on 2021/7/9.
     */

    public static class CameraSurfaceHolder {

        Context context;
        SurfaceHolder surfaceHolder;
        SurfaceView surfaceView;

        SurfaceViewCallback callback = new SurfaceViewCallback();

        public void setCameraSurfaceHolder(Context context, SurfaceView surfaceView){
            this.context = context;
            this.surfaceView = surfaceView;
            surfaceHolder = surfaceView.getHolder();
            surfaceHolder.addCallback(callback);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            callback.setContext(context);
        }
    }

    /**
     * Created by johnw on 2021/7/9.
     */

    public static class FaceTask extends AsyncTask {

        private byte[] mData;
        Camera mCamera;
        private static final String TAG = "CameraTag";

        FaceTask(byte[] data, Camera camera) {
            this.mData = data;
            this.mCamera = camera;
        }

        @Override
        protected Object doInBackground(Object[] params) {
            Camera.Parameters parameters = mCamera.getParameters();
            int imageFormat = parameters.getPreviewFormat();
            int w = parameters.getPreviewSize().width;
            int h = parameters.getPreviewSize().height;

            Rect rect = new Rect(0, 0, w, h);
            YuvImage yuvImg = new YuvImage(mData, imageFormat, w, h, null);
            try {
                ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
                yuvImg.compressToJpeg(rect, 100, outputstream);
                Bitmap rawbitmap = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
                Log.i(TAG, "onPreviewFrame:rawbitmap:" + rawbitmap.toString());
            } catch (Exception e) {
                Log.e(TAG, "onPreviewFrame:获取相机实时数据失败 " + e.getLocalizedMessage());
            }

            return null;
        }
    }

    /**
     * Created by johnw on 2021/7/9.
     */

    public static class FrontCamera {
        static final String TAG = "Camera";
        Camera mCamera;
        int mCurrentCamIndex = 0;
        boolean previewing;

        public void setCamera(Camera camera) {
            this.mCamera = camera;
        }

        public int getCurrentCamIndex() {
            return this.mCurrentCamIndex;
        }

        public boolean getPreviewing() {
            return this.previewing;
        }

        Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {

            }
        };

        Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bitmap = null;
                bitmap = BitmapFactory.decodeByteArray(data, 0 ,data.length);
                Log.i(TAG, "已经获取了bitmap:" + bitmap.toString());
                previewing = false;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
                previewing = true;
            }
        };

        public Camera initCamera() {
            int cameraCount = 0;
            Camera cam = null;
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            cameraCount = Camera.getNumberOfCameras();
            Log.e(TAG, "cameraCount:" + cameraCount);
            previewing = true;

            for (int camIdx= 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo);
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    try {
                        cam = Camera.open(camIdx);
                        mCurrentCamIndex = camIdx;
                    }catch (RuntimeException e) {
                        Log.e(TAG, "Camera failed to open: " + e.getLocalizedMessage());
                    }
                }
            }
            return cam;
        }

        public void StopCamera(Camera mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            previewing = false;
        }

        public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraId, info);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int degrees = 0;
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0;break;
                case Surface.ROTATION_90: degrees = 90;break;
                case Surface.ROTATION_180: degrees = 180;break;
                case Surface.ROTATION_270: degrees = 270;break;
            }

            int result;

            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            }
            else {
                result = (info.orientation - degrees + 360) % 360;
            }
            camera.setDisplayOrientation(result);
        }
    }

    /**
     * Created by johnw on 2021/7/9.
     */

    public static final class SurfaceViewCallback implements SurfaceHolder.Callback,Camera.PreviewCallback {
        Context context;
        static  final String TAG = "SYRFACECamera";
        Camera.FrontCamera mFrontCamera = new Camera.FrontCamera();
        boolean previewing = mFrontCamera.getPreviewing();
        Camera mCamera;
        Camera.FaceTask mFaceTask;

        public void setContext(Context context) {
            this.context = context;
        }

        @Override
        public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){
            if (previewing) {
                mCamera.stopPreview();
                Log.i(TAG, "停止预览");
            }

            try {
                mCamera.setPreviewDisplay(arg0);
                mCamera.startPreview();
                mCamera.setPreviewCallback(this);
                Log.i(TAG, "开始预览");
                //调用旋转屏幕时自适应
                //setCameraDisplayOrientation(MainActivity.this, mCurrentCamIndex, mCamera);
            } catch (Exception e) {
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder){
            //初始化前置摄像头
            mCamera = mFrontCamera.initCamera();
            mFrontCamera.setCamera(mCamera);
            mCamera.setPreviewCallback(this);
            //适配竖排固定角度
            Camera.FrontCamera.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);
            Log.i(TAG, "surfaceCreated");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mFrontCamera.StopCamera(mCamera);
            Log.i(TAG, "surfaceDestroyed");
        }

        @Override
        public void onPreviewFrame(byte[] data, Camera camera){
            if (mFaceTask != null) {
                switch (mFaceTask.getStatus()) {
                    case AsyncTask.Status.RUNNING:
                        return;
                    case AsyncTask.Status.PENDING:
                        mFaceTask.cancel(false);
                        break;
                }

            }
            mFaceTask = new Camera.FaceTask(data, camera);
            mFaceTask.execute((Void) null);
        }
    }
}