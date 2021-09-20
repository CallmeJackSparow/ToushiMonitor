package com.example.johnw.toushimonitor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.johnw.toushimonitor.camera.HandleBitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import androidx.appcompat.app.AppCompatActivity;

public class faceAnalysisActivity extends AppCompatActivity {

    private RelativeLayout mMainLayout;
    private ImageView mImageView;
    TextView beautyView;
    TextView sexView;// = (TextView) findViewById(R.id.sexInfo);
    TextView glassView;// = (TextView) findViewById(R.id.glassInfo);
    TextView expressionView;// = (TextView) findViewById(R.id.moodInfo);
    TextView faceShapeView;// = (TextView) findViewById(R.id.moodInfo);
    TextView ageView;// = (TextView) findViewById(R.id.ageInfo);
    TextView emotionView;// = (TextView) findViewById(R.id.motionInfo);
    TextView maskView;// = (TextView) findViewById(R.id.maskInfo);
    TextView eyeStatusView;// = (TextView) findViewById(R.id.eyesInfo);
    TextView jokeView;
    TextView charmInfoView;

    private Timer mTimer;
    private TimerTask mTimerTask;
    int mIndex = 0;

    int mSex = 0; // 0 == 男 || 1 == 女
    int mBeautyScore = 0;
    HandleBitmap handleBitmap;


    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = this.getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_analysis);

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int count = sharedPreferences.getInt("count", 0);
        TextView totalPeopleCount = (TextView) findViewById(R.id.totalNum2);
        totalPeopleCount.setText(String.valueOf(count));
        SharedPreferences.Editor editor = sharedPreferences.edit();

        mMainLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
        mImageView = (ImageView) findViewById(R.id.mainHeadView);
        beautyView = (TextView) findViewById(R.id.textScore);
        sexView = (TextView) findViewById(R.id.sexInfo);
        glassView = (TextView) findViewById(R.id.glassInfo);
        expressionView = (TextView) findViewById(R.id.moodInfo);
        faceShapeView = (TextView) findViewById(R.id.faceInfo);
        ageView = (TextView) findViewById(R.id.ageInfo);
        emotionView = (TextView) findViewById(R.id.motionInfo);
        maskView = (TextView) findViewById(R.id.maskInfo);
        eyeStatusView = (TextView) findViewById(R.id.eyesInfo);
        jokeView = (TextView) findViewById(R.id.jokeInfo);
        charmInfoView = (TextView) findViewById(R.id.charmInfo);

        String left = getIntent().getStringExtra("player1Left");
        String top = getIntent().getStringExtra("player1Top");
        String width = getIntent().getStringExtra("player1Width");
        String height = getIntent().getStringExtra("player1Height");

        String imageFileName = getExternalFilesDir(null).getAbsolutePath() + "/image/face.jpg";
        Bitmap bitmap = handleBitmap.CaptureImage(imageFileName, Integer.parseInt(left) - 40, Integer.parseInt(top) - 40,
                Integer.parseInt(width) + 80, Integer.parseInt(height) + 80);
        mImageView.setImageBitmap(bitmap);
        //mImageView.setRotation(270);

        //Toast.makeText(faceAnalysisActivity.this, "QRCode",Toast.LENGTH_SHORT).show();
        ShowInfo();
        ShowJoke();

        int mCurrentRank = 1;

        if (mSex == 0) { //男子排行
            String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank1.jpg";
            String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank2.jpg";
            String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank3.jpg";
            String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank4.jpg";
            String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/man/rank5.jpg";

            int manRank1Score = sharedPreferences.getInt("manRank1", 0);
            int manRank2Score = sharedPreferences.getInt("manRank2", 0);
            int manRank3Score = sharedPreferences.getInt("manRank3", 0);
            int manRank4Score = sharedPreferences.getInt("manRank4", 0);
            int manRank5Score = sharedPreferences.getInt("manRank5", 0);

            if (mBeautyScore >= manRank1Score) {
                editor.putInt("manRank5", manRank4Score);
                editor.putInt("manRank4", manRank3Score);
                editor.putInt("manRank3", manRank2Score);
                editor.putInt("manRank2", manRank1Score);
                editor.putInt("manRank1", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(rank3Image, rank4Image);
                renameFile(rank2Image, rank3Image);
                renameFile(rank1Image, rank2Image);
                renameFile(imageFileName, rank1Image);
            } else if (mBeautyScore >= manRank2Score) {
                editor.putInt("manRank5", manRank4Score);
                editor.putInt("manRank4", manRank3Score);
                editor.putInt("manRank3", manRank2Score);
                editor.putInt("manRank2", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(rank3Image, rank4Image);
                renameFile(rank2Image, rank3Image);
                renameFile(imageFileName, rank2Image);
            } else if (mBeautyScore >= manRank3Score) {
                editor.putInt("manRank5", manRank4Score);
                editor.putInt("manRank4", manRank3Score);
                editor.putInt("manRank3", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(rank3Image, rank4Image);
                renameFile(imageFileName, rank3Image);
            } else if (mBeautyScore >= manRank4Score) {
                editor.putInt("manRank5", manRank4Score);
                editor.putInt("manRank4", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(imageFileName, rank4Image);
            } else if (mBeautyScore >= manRank5Score) {
                editor.putInt("manRank5", mBeautyScore);
                renameFile(imageFileName, rank5Image);
            }
        } else { //女子排行
            String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank1.jpg";
            String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank2.jpg";
            String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank3.jpg";
            String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank4.jpg";
            String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/woman/rank5.jpg";

            int womanRank1Score = sharedPreferences.getInt("womanRank1", 0);
            int womanRank2Score = sharedPreferences.getInt("womanRank2", 0);
            int womanRank3Score = sharedPreferences.getInt("womanRank3", 0);
            int womanRank4Score = sharedPreferences.getInt("womanRank4", 0);
            int womanRank5Score = sharedPreferences.getInt("womanRank5", 0);

            if (mBeautyScore > womanRank1Score) {
                editor.putInt("womanRank5", womanRank4Score);
                editor.putInt("womanRank4", womanRank3Score);
                editor.putInt("womanRank3", womanRank2Score);
                editor.putInt("womanRank2", womanRank1Score);
                editor.putInt("womanRank1", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(rank3Image, rank4Image);
                renameFile(rank2Image, rank3Image);
                renameFile(rank1Image, rank2Image);
                renameFile(imageFileName, rank1Image);
            } else if (mBeautyScore > womanRank2Score) {
                editor.putInt("womanRank5", womanRank4Score);
                editor.putInt("womanRank4", womanRank3Score);
                editor.putInt("womanRank3", womanRank2Score);
                editor.putInt("womanRank2", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(rank3Image, rank4Image);
                renameFile(rank2Image, rank3Image);
                renameFile(imageFileName, rank2Image);
            } else if (mBeautyScore > womanRank3Score) {
                editor.putInt("womanRank5", womanRank4Score);
                editor.putInt("womanRank4", womanRank3Score);
                editor.putInt("womanRank3", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(rank3Image, rank4Image);
                renameFile(imageFileName, rank3Image);
            } else if (mBeautyScore > womanRank4Score) {
                editor.putInt("womanRank5", womanRank4Score);
                editor.putInt("womanRank4", mBeautyScore);
                renameFile(rank4Image, rank5Image);
                renameFile(imageFileName, rank4Image);
            } else if (mBeautyScore > womanRank5Score) {
                editor.putInt("womanRank5", mBeautyScore);
                renameFile(imageFileName, rank5Image);
            }
        }
        //charmInfoView.setText("今日魅力排名:NO." + String.valueOf(mCurrentRank));
        editor.apply();

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex == 3) {
                    SaveImage();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void SaveImage() {
        View view = getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();

        if (bitmap != null) {
            try {
              String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                      "/Android/data/com.example.johnw.toushimonitor/files/image/";
              String imagePath = storePath + "screenshot.png";
              File file = new File(imagePath);
              FileOutputStream os = new FileOutputStream(file);
              bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int deviceId = sharedPreferences.getInt("deviceId",1);

        String fileUrl = handleBitmap.GetQR(deviceId);
        String path = getExternalFilesDir(null).getAbsolutePath()  + "/image/";
        File saveFile = new File(path, "qrString.txt");
        FileOutputStream outputStream1 = null;
        try {
            outputStream1 = new FileOutputStream(saveFile);
            outputStream1.write(fileUrl.getBytes("GBK"));
            outputStream1.close();
            outputStream1.flush();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        Bitmap qrBitmap = handleBitmap.GetNetworkBitmap(fileUrl);
        String qrPath = getExternalFilesDir(null).getAbsolutePath() + "/image/qrImage.jpg";
        try {
            File file = new File(qrPath);
            FileOutputStream out = new FileOutputStream(file);
            qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ChangeToRank();
    }

    public void ShowInfo() {
        String faceinfo = getIntent().getStringExtra("faceInfo");
        try {
            JSONObject object = new JSONObject(faceinfo);
            String resultMsg = object.getString("result");
            JSONObject objFaceInfo = new JSONObject(resultMsg);
            String faceList = objFaceInfo.getString("face_list");
            JSONArray arrayFaceInfo = new JSONArray(faceList);
            for (int i = 0; i < arrayFaceInfo.length(); i++) {
                JSONObject value = arrayFaceInfo.getJSONObject(i);
                //Toast.makeText(faceAnalysisActivity.this, "2",Toast.LENGTH_SHORT).show();
                //颜值分
                mBeautyScore = (int) Double.parseDouble(value.getString("beauty"));//.to
                mBeautyScore = mBeautyScore + 40;
                if (mBeautyScore > 100) {
                    mBeautyScore = 100;
                }
                beautyView.setText(String.valueOf(mBeautyScore));
                //Toast.makeText(faceAnalysisActivity.this, "QRCode2",Toast.LENGTH_SHORT).show();
                //年龄
                int age = Integer.parseInt(value.getString("age"));
                ageView.setText(String.valueOf(age - 2));
                //性别
                JSONObject objGender = new JSONObject(value.getString("gender"));
                if (objGender.getString("type").matches("male")) {
                    mSex = 0;
                    sexView.setText("男");
                }
                else {
                    mSex = 1;
                    sexView.setText("女");
                }
                //情绪
                JSONObject objEmotion = new JSONObject(value.getString("emotion"));
                if (objEmotion.getString("type").matches("neutral")) {
                    emotionView.setText("无表情");
                }
                else if (objEmotion.getString("type").matches("angry")) {
                    emotionView.setText("愤怒");
                }
                else if (objEmotion.getString("type").matches("disgust")) {
                    emotionView.setText("厌恶");
                }
                else if (objEmotion.getString("type").matches("fear")) {
                    emotionView.setText("恐惧");
                }
                else if (objEmotion.getString("type").matches("happy")) {
                    emotionView.setText("高兴");
                }
                else if (objEmotion.getString("type").matches("grimace")) {
                    emotionView.setText("鬼脸");
                }
                else if (objEmotion.getString("type").matches("pouty")) {
                    emotionView.setText("噘嘴");
                }
                else if (objEmotion.getString("type").matches("surprise")) {
                    emotionView.setText("惊讶");
                }
                //脸型
                JSONObject objFaceShape = new JSONObject(value.getString("face_shape"));
                if (objFaceShape.getString("type").matches("square")) {
                    faceShapeView.setText("方形");
                } else if (objFaceShape.getString("type").matches("triangle")) {
                    faceShapeView.setText("三角形");
                } else if (objFaceShape.getString("type").matches("oval")) {
                    faceShapeView.setText("椭圆");
                } else if (objFaceShape.getString("type").matches("heart")) {
                    faceShapeView.setText("心形");
                } else if (objFaceShape.getString("type").matches("round")) {
                    faceShapeView.setText("圆形");
                }
                //眼镜
                JSONObject objGlasses = new JSONObject(value.getString("glasses"));
                if (objGlasses.getString("type").matches("none")) {
                    glassView.setText("无");
                } else {
                    glassView.setText("有");
                }
                //表情
                JSONObject objExpression = new JSONObject(value.getString("expression"));
                if (objExpression.getString("type").matches("none")) {
                    expressionView.setText("不笑");
                } else if (objExpression.getString("type").matches("smile")) {
                    expressionView.setText("微笑");
                } else {
                    expressionView.setText("大笑");
                }
                //口罩
                JSONObject objMask = new JSONObject(value.getString("mask"));
                if (objMask.getString("type").matches("0")) {
                    maskView.setText("无");
                } else {
                    maskView.setText("有");
                }
                //眼睛
                JSONObject objEyeStatus = new JSONObject(value.getString("eye_status"));
                if (objEyeStatus.getString("left_eye").matches("1")) {
                    eyeStatusView.setText("闭眼");
                } else {
                    eyeStatusView.setText("睁眼");
                }
            }
        } catch (Exception ex) {
            Toast.makeText(faceAnalysisActivity.this, "Exception",Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }


    public void ShowJoke() {
        String jokeFileName = getExternalFilesDir(null).getAbsolutePath() + "/joke.txt";
        File file = new File(jokeFileName);
        if (!file.exists()) {
            return;
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
        String fileInfo = getString(fileInputStream);
        try {
            JSONObject obj = new JSONObject(fileInfo);
            String infoList = null;

            if (mSex == 0) { //男性
                infoList = obj.getString("manList");

            } else { //女性
                infoList = obj.getString("womanList");
            }
            JSONArray infoArray = new JSONArray(infoList);
            Random random = new Random();
            int jokeIndex = random.nextInt(infoArray.length()) % (infoArray.length() + 1);
            JSONObject jokeObj = infoArray.getJSONObject(jokeIndex);
            jokeView.setText(jokeObj.getString("info"));

        } catch (Exception e) {
            Toast.makeText(faceAnalysisActivity.this, "joke",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private File renameFile(String oldPath, String newPath) {
        if (TextUtils.isEmpty(oldPath)) {
            return null;
        }
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        boolean b = oldFile.renameTo(newFile);
        File file2 = new File(newPath);
        return file2;
    }

    private void ChangeToRank() {
        Intent intent;
        //Toast.makeText(faceAnalysisActivity.this, String.valueOf(mSex),Toast.LENGTH_SHORT).show();
        if (mSex == 0) { //进入男神版
            intent = new Intent(faceAnalysisActivity.this, MenGodRankActivity.class);
        } else { //进入女神版
            intent = new Intent(faceAnalysisActivity.this, WomenGodRankActivity.class);
        }
        startActivity(intent);
    }
}