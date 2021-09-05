package com.example.johnw.toushimonitor;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private Button mLogoPath;
    private Button mQrImagePath;
    private Button mBtnBack;
    private Button mBtnDeviceId;
    private Button mBtnPage1Info;

    boolean mButtonType = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
        String deviceId = sharedPreferences.getString("DeviceId", "1");

        mBtnDeviceId = (Button) findViewById(R.id.btn_deviceId);
        mBtnDeviceId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonType = true;
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("输入要修改的设备编号");
                builder.setIcon(R.drawable.ic_launcher_foreground);
                EditText edtId = new EditText(SettingsActivity.this);
                edtId.setText(deviceId);
                builder.setView(edtId);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("DeviceId", edtId.getText().toString());
                        editor.apply();
                        builder.setCancelable(true);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(false);
                    }
                });
                builder.show();
            }
        });

        mLogoPath = (Button) findViewById(R.id.btnLogoPath);
        mLogoPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonType = true;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mQrImagePath = (Button) findViewById(R.id.btnQrCodePath);
        mQrImagePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonType = false;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });

        mBtnPage1Info = (Button) findViewById(R.id.btnFirstpage);
        mBtnPage1Info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText = sharedPreferences.getString("titleText", "颜值机");
                int titleSize = sharedPreferences.getInt("titleSize", 50);
                String companyText = sharedPreferences.getString("companyText", "TOUSHI Tec");
                int companySize = sharedPreferences.getInt("companySize", 30);

                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("修改首页标题信息");
                builder.setIcon(R.drawable.ic_launcher_foreground);
                EditText edtTitle = new EditText(SettingsActivity.this);
                edtTitle.setText(titleText);
                //edtTitle.set("标题名称");
                EditText edtTitleSize = new EditText(SettingsActivity.this);
                edtTitleSize.setText(String.valueOf(titleSize));
                //edtTitleSize.setHint("公司字体大小");
                EditText edtCompany = new EditText(SettingsActivity.this);
                edtCompany.setText(companyText);
                //edtCompany.setHint("公司名称");
                EditText edtCompanySize = new EditText(SettingsActivity.this);
                edtCompanySize.setText(String.valueOf(companySize));
                //edtCompanySize.setHint("公司字体大小");

                LinearLayout linearLayout = new LinearLayout(SettingsActivity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);

                linearLayout.addView(edtTitle);
                linearLayout.addView(edtTitleSize);
                linearLayout.addView(edtCompany);
                linearLayout.addView(edtCompanySize);


                builder.setView(linearLayout);
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        if (!edtTitle.getText().equals("")) {
                            editor.putString("titleText", edtTitle.getText().toString());
                        }
                        if (!edtTitleSize.getText().equals("")) {
                            editor.putInt("titleSize", Integer.parseInt(edtTitleSize.getText().toString()));
                        }
                        if (!edtCompany.getText().equals("")) {
                            editor.putString("companyText", edtCompany.getText().toString());
                        }
                        if (!edtCompanySize.getText().equals("")) {
                            editor.putInt("companySize", Integer.parseInt(edtCompanySize.getText().toString()));
                        }

                        editor.apply();

                        builder.setCancelable(true);
                    }
                });
                builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(false);
                    }
                });
                builder.show();
            }
        });

        mBtnBack = (Button) findViewById(R.id.btn_setting_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String path;
            Uri uri = data.getData();
            path = uri.toString();
            SharedPreferences sharedPreferences = getSharedPreferences("Toushi", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (mButtonType == false) {
                editor.putInt("qrImage", Integer.valueOf(path));
                //mQrImageView.setText(path);
            } else {
                editor.putInt("logoImage", Integer.valueOf(path));
                //mLogoView.setText(path);
            }
            editor.apply();
        }
    }
}