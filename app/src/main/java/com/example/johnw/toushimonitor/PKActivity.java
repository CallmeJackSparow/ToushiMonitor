package com.example.johnw.toushimonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.johnw.toushimonitor.camera.HandleBitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

public class PKActivity extends AppCompatActivity {

    private TextView mPlayer1Score;
    private TextView mPlayer2Score;
    private ImageView mPlayer1Face;
    private ImageView mPlayer2Face;

    private TextView mPlayer1Feature1;
    private TextView mPlayer1Feature2;
    private TextView mPlayer1Feature3;

    private TextView mPlayer2Feature1;
    private TextView mPlayer2Feature2;
    private TextView mPlayer2Feature3;

    HandleBitmap handleBitmap;

    private Timer mTimer;
    private TimerTask mTimerTask;
    int mIndex = 0;
    int mBeautyScore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pkactivity);

        mPlayer1Score = (TextView) findViewById(R.id.player1Score);
        mPlayer2Score = (TextView) findViewById(R.id.player2Score);

        mPlayer1Feature1 = (TextView) findViewById(R.id.player1Feature1);
        mPlayer1Feature2 = (TextView) findViewById(R.id.player1Feature2);
        mPlayer1Feature3 = (TextView) findViewById(R.id.player1Feature3);

        mPlayer2Feature1 = (TextView) findViewById(R.id.player2Feature1);
        mPlayer2Feature2 = (TextView) findViewById(R.id.player2Feature2);
        mPlayer2Feature3 = (TextView) findViewById(R.id.player2Feature3);

        mPlayer1Face = (ImageView) findViewById(R.id.player1Face);
        mPlayer2Face = (ImageView) findViewById(R.id.player2Face);


        ShowInfo();


        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex == 3) {
                    SaveImage();
                }
                if (mIndex == 6) {
                    ChangeToRank();
                }
                mIndex++;
            }
        };
        mTimer.schedule(mTimerTask, 0, 1000);
    }

    public void ShowInfo() {
        String imageFileName = getExternalFilesDir(null).getAbsolutePath() + "/image/face.jpg";
        String faceinfo = getIntent().getStringExtra("faceInfo");
        Toast.makeText(getApplicationContext(), "切换界面", Toast.LENGTH_SHORT).show();


        try {
            JSONObject object = new JSONObject(faceinfo);
            String resultMsg = object.getString("result");
            JSONObject objFaceInfo = new JSONObject(resultMsg);
            String faceList = objFaceInfo.getString("face_list");
            JSONArray arrayFaceInfo = new JSONArray(faceList);

            if (arrayFaceInfo.length() == 2) { //双人模式
                JSONObject player1 = arrayFaceInfo.getJSONObject(0); //选手1
                int player1Score = (int) Double.parseDouble(player1.getString("beauty"));//.to
                player1Score = player1Score + 40;
                if (player1Score > 100) {
                    player1Score = 100;
                }
                mPlayer1Score.setText(String.valueOf(player1Score));

                String player1Location = player1.getString("location");
                JSONObject objPlayer1Location = new JSONObject(player1Location);
                int player1left = (int) Math.round(objPlayer1Location.getDouble("left"));
                int player1top = (int) Math.round(objPlayer1Location.getDouble("top"));
                int player1Width = (int) Math.round(objPlayer1Location.getDouble("width"));
                int player1Height = (int) Math.round(objPlayer1Location.getDouble("height"));
                Bitmap player1Bitmap = handleBitmap.CaptureImage(imageFileName, player1left, player1top, player1Width, player1Height);
                mPlayer1Face.setImageBitmap(player1Bitmap);

                //情绪
                JSONObject objEmotion = new JSONObject(player1.getString("emotion"));
                if (objEmotion.getString("type").matches("neutral")) {
                    mPlayer1Feature1.setText("无表情");
                }
                else if (objEmotion.getString("type").matches("angry")) {
                    mPlayer1Feature1.setText("愤怒");
                }
                else if (objEmotion.getString("type").matches("disgust")) {
                    mPlayer1Feature1.setText("厌恶");
                }
                else if (objEmotion.getString("type").matches("fear")) {
                    mPlayer1Feature1.setText("恐惧");
                }
                else if (objEmotion.getString("type").matches("happy")) {
                    mPlayer1Feature1.setText("高兴");
                }
                else if (objEmotion.getString("type").matches("grimace")) {
                    mPlayer1Feature1.setText("鬼脸");
                }
                else if (objEmotion.getString("type").matches("pouty")) {
                    mPlayer1Feature1.setText("噘嘴");
                }
                else if (objEmotion.getString("type").matches("surprise")) {
                    mPlayer1Feature1.setText("惊讶");
                }
                //脸型
                JSONObject objFaceShape = new JSONObject(player1.getString("face_shape"));
                if (objFaceShape.getString("type").matches("square")) {
                    mPlayer1Feature2.setText("方形");
                } else if (objFaceShape.getString("type").matches("triangle")) {
                    mPlayer1Feature2.setText("三角形");
                } else if (objFaceShape.getString("type").matches("oval")) {
                    mPlayer1Feature2.setText("椭圆");
                } else if (objFaceShape.getString("type").matches("heart")) {
                    mPlayer1Feature2.setText("心形");
                } else if (objFaceShape.getString("type").matches("round")) {
                    mPlayer1Feature2.setText("圆形");
                }
                //表情
                JSONObject objExpression = new JSONObject(player1.getString("expression"));
                if (objExpression.getString("type").matches("none")) {
                    mPlayer1Feature3.setText("不笑");
                } else if (objExpression.getString("type").matches("smile")) {
                    mPlayer1Feature3.setText("微笑");
                } else {
                    mPlayer1Feature3.setText("大笑");
                }

                JSONObject player2 = arrayFaceInfo.getJSONObject(1); //选手2
                int player2Score = (int) Double.parseDouble(player2.getString("beauty"));//.to
                player2Score = player2Score + 40;
                if (player2Score > 100) {
                    player2Score = 100;
                }
                mPlayer2Score.setText(String.valueOf(player2Score));

                String player2Location = player2.getString("location");
                JSONObject objPlayer2Location = new JSONObject(player2Location);
                int player2left = (int) Math.round(objPlayer2Location.getDouble("left"));
                int player2top = (int) Math.round(objPlayer2Location.getDouble("top"));
                int player2Width = (int) Math.round(objPlayer2Location.getDouble("width"));
                int player2Height = (int) Math.round(objPlayer2Location.getDouble("height"));
                Bitmap player2Bitmap = handleBitmap.CaptureImage(imageFileName, player2left, player2top, player2Width, player2Height);
                mPlayer2Face.setImageBitmap(player2Bitmap);
                //情绪
                JSONObject objEmotion1 = new JSONObject(player2.getString("emotion"));
                if (objEmotion1.getString("type").matches("neutral")) {
                    mPlayer2Feature1.setText("无表情");
                }
                else if (objEmotion1.getString("type").matches("angry")) {
                    mPlayer2Feature1.setText("愤怒");
                }
                else if (objEmotion1.getString("type").matches("disgust")) {
                    mPlayer2Feature1.setText("厌恶");
                }
                else if (objEmotion1.getString("type").matches("fear")) {
                    mPlayer2Feature1.setText("恐惧");
                }
                else if (objEmotion1.getString("type").matches("happy")) {
                    mPlayer2Feature1.setText("高兴");
                }
                else if (objEmotion1.getString("type").matches("grimace")) {
                    mPlayer2Feature1.setText("鬼脸");
                }
                else if (objEmotion1.getString("type").matches("pouty")) {
                    mPlayer2Feature1.setText("噘嘴");
                }
                else if (objEmotion1.getString("type").matches("surprise")) {
                    mPlayer2Feature1.setText("惊讶");
                }
                //脸型
                JSONObject objFaceShape1 = new JSONObject(player2.getString("face_shape"));
                if (objFaceShape1.getString("type").matches("square")) {
                    mPlayer2Feature2.setText("方形");
                } else if (objFaceShape1.getString("type").matches("triangle")) {
                    mPlayer2Feature2.setText("三角形");
                } else if (objFaceShape1.getString("type").matches("oval")) {
                    mPlayer2Feature2.setText("椭圆");
                } else if (objFaceShape1.getString("type").matches("heart")) {
                    mPlayer2Feature2.setText("心形");
                } else if (objFaceShape1.getString("type").matches("round")) {
                    mPlayer2Feature2.setText("圆形");
                }
                //表情
                JSONObject objExpression1 = new JSONObject(player2.getString("expression"));
                if (objExpression1.getString("type").matches("none")) {
                    mPlayer2Feature3.setText("不笑");
                } else if (objExpression1.getString("type").matches("smile")) {
                    mPlayer2Feature3.setText("微笑");
                } else {
                    mPlayer2Feature3.setText("大笑");
                }

                if (player1Score > player2Score) {
                    mBeautyScore = player1Score;
                    if (player1Bitmap!= null) {
                        try {
                            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                    "/Android/data/com.example.johnw.toushimonitor/files/image/";
                            String imagePath = storePath + "pkWinner.png";
                            File file = new File(imagePath);
                            FileOutputStream os = new FileOutputStream(file);
                            player1Bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    mBeautyScore = player2Score;
                    if (player2Bitmap!= null) {
                        try {
                            String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                                    "/Android/data/com.example.johnw.toushimonitor/files/image/";
                            String imagePath = storePath + "pkWinner.png";
                            File file = new File(imagePath);
                            FileOutputStream os = new FileOutputStream(file);
                            player1Bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }


        } catch (Exception ex) {
            Toast.makeText(PKActivity.this, "Exception",Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }


    public void SaveRankInfo()
    {

        String imageFileName = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Android/data/com.example.johnw.toushimonitor/files/image/pkWinner.png";

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank1.jpg";
        String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank2.jpg";
        String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank3.jpg";
        String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank4.jpg";
        String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank5.jpg";

        int manRank1Score = sharedPreferences.getInt("pkRank1", 0);
        int manRank2Score = sharedPreferences.getInt("pkRank2", 0);
        int manRank3Score = sharedPreferences.getInt("pkRank3", 0);
        int manRank4Score = sharedPreferences.getInt("pkRank4", 0);
        int manRank5Score = sharedPreferences.getInt("pkRank5", 0);

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
        editor.apply();
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
        mTimer.cancel();
        mTimerTask.cancel();
        Intent intent;
        intent = new Intent(PKActivity.this, PkWinnerBand.class);
        startActivity(intent);
    }
}