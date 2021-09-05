package com.example.johnw.toushimonitor.camera;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.johnw.toushimonitor.MainActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * 单独的任务类。继承AsyncTask，来处理从相机实时获取的耗时操作
 */
public class FaceTask extends AsyncTask<String, Void, String>{

    Context context;
    private byte[] mData;
    Camera mCamera;
    private static final String TAG = "CameraTag";
    private static final String FeatureUrl = "https://aip.baidubce.com/rest/2.0/face/v3/detect";

    private MainActivity mActivity;
    int mIndex = 0;
    boolean mHasFace = false;
    String mFaceInfo;
    String mAccessToken;

    //构造函数
    FaceTask(byte[] data , Camera camera, Context context, int index)
    {
        //handler.postDelayed(runnable, 500);
        this.mIndex = index;
        this.context = context;
        this.mData = data;
        this.mCamera = camera;
    }

    //任务执行之前的操作
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    //返回服务器返回的文件
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        //如果检测到人脸进入该第二阶段
        if (mHasFace) { //检测到人脸将数据传回主界面进行处理
            //返回回调信息
            this.onSuccessListener.onSuccess();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;

        return null;
    }

    public OnSuccessListener onSuccessListener;

    public interface OnSuccessListener {
        void onSuccess();
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
