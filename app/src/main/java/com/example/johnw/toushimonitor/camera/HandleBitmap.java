package com.example.johnw.toushimonitor.camera;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.biometrics.BiometricManager;
import android.os.Environment;
import android.util.Base64;

import com.example.johnw.toushimonitor.MainActivity;
import com.example.johnw.toushimonitor.PKActivity;

import org.json.JSONObject;

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
import java.security.spec.ECField;

public class HandleBitmap {

    public static Bitmap GetNormalBitmap(String pImagePath) { //获取最原始的图片Bitmap信息
        Bitmap originBitmap = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = 1920;
            options.outWidth = 1080;
            options.inJustDecodeBounds = false;
            originBitmap = BitmapFactory.decodeFile(pImagePath, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return originBitmap;
    }

    public static Bitmap getBitmap(String pImagePath)
    {
        File file = new File(pImagePath);
        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pImagePath, options);
        options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pImagePath, options);

        try {
            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(),
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(circleBitmap);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getWidth());
            final RectF rectF = new RectF(new Rect(0,0, bitmap.getWidth(), bitmap.getWidth()));
            float roundPx = 0.0f;
            roundPx = bitmap.getHeight();
            paint.setAntiAlias(true);
            canvas.drawARGB(0,0,0,0);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0,0,bitmap.getWidth(), bitmap.getWidth());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return circleBitmap;
        } catch (Exception e) {
            return bitmap;
        }

        //  return bitmap1;
    }

    public static Bitmap qrImageBitmap(String pImagePath) {
        File file = new File(pImagePath);
        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pImagePath, options);
        options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pImagePath, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 440;
        int newHeight = 440;
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,0,width,height, matrix, true);

        return newBitmap;
    }


    public static Bitmap scaleBitmap(String pImagePath) {
        File file = new File(pImagePath);
        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pImagePath, options);
        options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pImagePath, options);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 370;
        int newHeight = 673;
        float scaleWidth = ((float)newWidth) / width;
        float scaleHeight = ((float)newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0,0,width,height, matrix, true);

        return newBitmap;
    }

    public static Bitmap getBitmap2(String pImagePath)
    {
        File file = new File(pImagePath);
        if (!file.exists()) {
            return null;
        }

        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pImagePath, options);
        options.inSampleSize = Math.max(1, (int)Math.ceil(Math.max(
                (double) options.outWidth / 1024f,
                (double) options.outHeight / 1024f)));
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pImagePath, options);
        try {
            Bitmap circleBitmap = Bitmap.createBitmap(300, 600, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(circleBitmap);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            paint.setAntiAlias(true);
            canvas.drawARGB(0,0,0,0);
            paint.setColor(Color.WHITE);
            canvas.drawRect(rect, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0,20, bitmap.getWidth(), bitmap.getHeight() - 20);
            canvas.drawBitmap(bitmap, src, rect, paint);
            return circleBitmap;
        } catch (Exception e) {
            return bitmap;
        }
//        return bitmap;
        //  return bitmap1;
    }

    public static String GetFaceInfo(InputStream is) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int len = -1;
        while ((len = is.read(buff))!=-1) {
            outputStream.write(buff, 0, len);
        }
        is.close();

        String html = outputStream.toString();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image";
        File saveFile = new File(path, "qrImage.txt");
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
        String url = object.getString("qrcodeurl");

        outputStream.close();
        return url;
    }

    public static String GetQR(int deviceId) { //获取二维码
        String FeatureUrl = "https://wx.17toushi.com/e/extend/weixin/app_picupload.php";
        HttpURLConnection httpURLConnection = null;
        String responseInfo = null;
        StringBuffer requestBody = new StringBuffer();

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Bitmap rawbitmap = BitmapFactory.decodeFile(path + "/screenshot.png");
        rawbitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        String picString = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP);
        picString = "data:image/jpg;base64," + picString;

        try {
            URL url = new URL(FeatureUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=UTF-8");

            requestBody.append("type=text&");
            requestBody.append("name=pic&");
            requestBody.append("key=c9bf8a221e40f33a9b5d5083703d4740&");
            requestBody.append("path=");
            requestBody.append(String.valueOf(deviceId));
            requestBody.append("&");
            requestBody.append("pic=");
            requestBody.append(URLEncoder.encode(picString));

            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            String responseString = new String(requestBody);

            String path1 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/com.example.johnw.toushimonitor/files/image";
            File saveFile = new File(path1, "string123.txt");
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
                return GetFaceInfo(isResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "123";
    }

    public static Bitmap GetNetworkBitmap(String PicUrl) {
        Bitmap bitmap = null;
        try {
            InputStream inputStream = null;
            URL url = new URL(PicUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inputStream = conn.getInputStream();
            }

            bitmap = BitmapFactory.decodeStream(inputStream);

//            byte[] buffer = new byte[1024];
//            int len = 0;
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            while ((len = inputStream.read(buffer)) != -1) {
//                bos.write(buffer, 0, len);
//            }
//            bos.close();
//
//            byte[] data = bos.toByteArray();
//            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            //return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap CaptureImage(String pImagePath, int left, int top, int width, int height) {
        Bitmap originBitmap = null;
        try {
            FileInputStream fis = new FileInputStream(pImagePath);
            originBitmap = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap captureBitmap = null;

        try {
            if (left < 0) {
                left = 0;
            }
            if (top < 0) {
                top = 0;
            }
            if (left + width > originBitmap.getWidth()) {
                width = originBitmap.getWidth() - left;
            }
            if (top + height > originBitmap.getHeight()) {
                height = originBitmap.getHeight() - top;
            }
            captureBitmap = Bitmap.createBitmap(originBitmap, left, top, width, height);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            int circleLength = 0;
            if (captureBitmap.getWidth() > captureBitmap.getHeight()) {
                circleLength = captureBitmap.getHeight();
            } else {
                circleLength = captureBitmap.getWidth();
            }

            Bitmap circleBitmap = Bitmap.createBitmap(circleLength, circleLength, Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(circleBitmap);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, circleLength, circleLength);
            final RectF rectF = new RectF(new Rect(0,0, circleLength, circleLength));
            float roundPx = 0.0f;
            roundPx = circleLength;
            paint.setAntiAlias(true);
            canvas.drawARGB(0,0,0,0);
            paint.setColor(Color.WHITE);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            final Rect src = new Rect(0,0, captureBitmap.getWidth(), captureBitmap.getHeight());
            canvas.drawBitmap(captureBitmap, src, rect, paint);
            return circleBitmap;
        } catch (Exception e) {
            return captureBitmap;
        }
    }

    public int GetFaceNum(String result) {
        int faceNum = 0;
        try {
            JSONObject object = new JSONObject(result);
            String resultMsg = object.getString("result");
            JSONObject objFaceInfo = new JSONObject(resultMsg);
            faceNum = objFaceInfo.getInt("face_num");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return faceNum;
    }
}
