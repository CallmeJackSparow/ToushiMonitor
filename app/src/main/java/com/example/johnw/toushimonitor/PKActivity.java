package com.example.johnw.toushimonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
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

    private TextView mRound;
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

        mRound = (TextView) findViewById(R.id.pkRound);
        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        int count = sharedPreferences.getInt("round", 0);
        mRound.setText(String.valueOf(count));

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

        SaveRankInfo();

        mTimer = new Timer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (mIndex == 3) {
                    SaveImage();
                }
//                if (mIndex == 6) {
//                    ChangeToRank();
//                }
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
                //选手1
                JSONObject player1 = arrayFaceInfo.getJSONObject(0);
                int player1Score = (int) Double.parseDouble(player1.getString("beauty"));//.to
                player1Score = player1Score + 40;
                if (player1Score > 100) {
                    player1Score = 100;
                }

                String player1Location = player1.getString("location");
                JSONObject objPlayer1Location = new JSONObject(player1Location);
                int player1left = (int) Math.round(objPlayer1Location.getDouble("left"));
                int player1top = (int) Math.round(objPlayer1Location.getDouble("top"));
                int player1Width = (int) Math.round(objPlayer1Location.getDouble("width"));
                int player1Height = (int) Math.round(objPlayer1Location.getDouble("height"));

                Bitmap player1Bitmap = handleBitmap.CaptureImage(imageFileName, player1left - 200, player1top - 200,
                        player1Width + 400, player1Height + 400);
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
                //选手2
                JSONObject player2 = arrayFaceInfo.getJSONObject(1);
                int player2Score = (int) Double.parseDouble(player2.getString("beauty"));//.to
                player2Score = player2Score + 40;
                if (player2Score > 100) {
                    player2Score = 100;
                }

                String player2Location = player2.getString("location");
                JSONObject objPlayer2Location = new JSONObject(player2Location);
                int player2left = (int) Math.round(objPlayer2Location.getDouble("left"));
                int player2top = (int) Math.round(objPlayer2Location.getDouble("top"));
                int player2Width = (int) Math.round(objPlayer2Location.getDouble("width"));
                int player2Height = (int) Math.round(objPlayer2Location.getDouble("height"));
                Bitmap player2Bitmap = handleBitmap.CaptureImage(imageFileName, player2left - 40, player2top - 40,
                        player2Width + 80, player2Height + 80);
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

                ValueAnimator animator = ValueAnimator.ofInt(0, player1Score);
                animator.setDuration(1500);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mPlayer1Score.setText(animation.getAnimatedValue().toString());
                    }
                });
                animator.start();

                ValueAnimator animator1 = ValueAnimator.ofInt(0, player2Score);
                animator1.setDuration(1500);
                animator1.setInterpolator(new LinearInterpolator());
                animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        mPlayer2Score.setText(animation.getAnimatedValue().toString());
                    }
                });
                animator1.start();

                String winnerImage = getExternalFilesDir(null).getAbsolutePath() + "/image/pkWinner.png";
                if (player1Score > player2Score) {
                    mBeautyScore = player1Score;
                    if (player1Bitmap!= null) {
                        try {
                            File file = new File(winnerImage);
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
                            File file = new File(winnerImage);
                            FileOutputStream os = new FileOutputStream(file);
                            player2Bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
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
        String imageFileName = getExternalFilesDir(null).getAbsolutePath() + "/image/pkWinner.png";

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String rank1Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank1.jpg";
        String rank2Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank2.jpg";
        String rank3Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank3.jpg";
        String rank4Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank4.jpg";
        String rank5Image = getExternalFilesDir(null).getAbsolutePath() + "/image/pk/rank5.jpg";

        int rank1Score = sharedPreferences.getInt("pkRank1", 0);
        int rank2Score = sharedPreferences.getInt("pkRank2", 0);
        int rank3Score = sharedPreferences.getInt("pkRank3", 0);
        int rank4Score = sharedPreferences.getInt("pkRank4", 0);
        int rank5Score = sharedPreferences.getInt("pkRank5", 0);

        if (mBeautyScore >= rank1Score) {
            editor.putInt("pkRank5", rank4Score);
            editor.putInt("pkRank4", rank3Score);
            editor.putInt("pkRank3", rank2Score);
            editor.putInt("pkRank2", rank1Score);
            editor.putInt("pkRank1", mBeautyScore);
            renameFile(rank4Image, rank5Image);
            renameFile(rank3Image, rank4Image);
            renameFile(rank2Image, rank3Image);
            renameFile(rank1Image, rank2Image);
            renameFile(imageFileName, rank1Image);
        } else if (mBeautyScore >= rank2Score) {
            editor.putInt("pkRank5", rank4Score);
            editor.putInt("pkRank4", rank3Score);
            editor.putInt("pkRank3", rank2Score);
            editor.putInt("pkRank2", mBeautyScore);
            renameFile(rank4Image, rank5Image);
            renameFile(rank3Image, rank4Image);
            renameFile(rank2Image, rank3Image);
            renameFile(imageFileName, rank2Image);
        } else if (mBeautyScore >= rank3Score) {
            editor.putInt("pkRank5", rank4Score);
            editor.putInt("pkRank4", rank3Score);
            editor.putInt("pkRank3", mBeautyScore);
            renameFile(rank4Image, rank5Image);
            renameFile(rank3Image, rank4Image);
            renameFile(imageFileName, rank3Image);
        } else if (mBeautyScore >= rank4Score) {
            editor.putInt("pkRank5", rank4Score);
            editor.putInt("pkRank4", mBeautyScore);
            renameFile(rank4Image, rank5Image);
            renameFile(imageFileName, rank4Image);
        } else if (mBeautyScore >= rank5Score) {
            editor.putInt("pkRank5", mBeautyScore);
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
                String storePath = getExternalFilesDir(null).getAbsolutePath() + "/image/screenshot.png";
                File file = new File(storePath);
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