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
            ((MainActivity)context).ChangeViewMode(mFaceInfo); //传递人脸数据信息到主界面
            this.onSuccessListener.onSuccess();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image";

        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(mData, imageFormat, w, h, null);
        try {
            //若要存储可以用下列代码，p格式为jpg
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path + "/face.jpg"));
            yuvImg.compressToJpeg(rect, 50, bos);
            bos.flush();
            bos.close();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Bitmap rawbitmap = BitmapFactory.decodeFile(path + "/face.jpg");
            rawbitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            String picString = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
            mFaceInfo = RequestFaceInfo(picString);
            //mCamera.startPreview();
        }
        catch (Exception e)
        {
            Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
        }
        return null;
    }

    private String RequestFaceInfo(String picData) {
        //Toast.makeText(context, "上传图片 ", Toast.LENGTH_SHORT).show();
        HttpURLConnection httpURLConnection = null;
        String responseInfo = null;
        StringBuffer buffer = new StringBuffer();
        buffer.append("access_token=").append(mAccessToken);
        StringBuffer requestBody = new StringBuffer();
        try {
            URL url = new URL(FeatureUrl + "?" + buffer);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("content-type", "application/json;charset=UTF-8");

            requestBody.append("max_face_num=2&");//
            requestBody.append("image_type=BASE64&");
            requestBody.append("face_field=beauty,gender,age,emotion,expression,glasses,mask,face_shape,eye_status&");
            requestBody.append("image=");
            requestBody.append(URLEncoder.encode(picData));

            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            String responseString = new String(requestBody);

            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image";
            File saveFile = new File(path, "string.txt");
            FileOutputStream outputStream1 = null;
            try {
                outputStream1 = new FileOutputStream(saveFile);
                outputStream1.write(responseString.getBytes("UTF-8"));
                outputStream1.close();
                outputStream1.flush();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            osw.write(responseString);
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream isResponse = httpURLConnection.getInputStream();
                responseInfo = GetFaceInfo(isResponse);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return responseInfo;
    }

    private String GetFaceInfo(InputStream is) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = is.read(buff))!=-1) {
            outputStream.write(buff, 0, len);
        }
        is.close();

        String html = outputStream.toString();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image";
        File saveFile = new File(path, "result.txt");
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream(saveFile);
            outputStream1.write(html.getBytes("GBK"));
            outputStream1.close();
            outputStream1.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        JSONObject object = new JSONObject(html);
        String errMsg = object.getString("error_msg");

        if (errMsg.matches("SUCCESS")) {
            mHasFace = true;
        }
        outputStream.close();
        return html;
    }


    public OnSuccessListener onSuccessListener;

    public interface OnSuccessListener {
        void onSuccess();
    }

    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }
}
