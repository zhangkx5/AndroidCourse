package com.example.kaixin.myapplication;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by kaixin on 2016/10/9.
 */
public class BasicActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_basic);

            final EditText username = (EditText) findViewById(R.id.username_hint);
            final EditText password = (EditText) findViewById(R.id.password_hint);
            Button login  = (Button) findViewById(R.id.login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (TextUtils.isEmpty(username.getText().toString())) {
                        Toast.makeText(BasicActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(password.getText().toString())) {
                        Toast.makeText(BasicActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else if ("Android".equals(username.getText().toString())
                            && "123456".equals(password.getText().toString())) {
                        new AlertDialog.Builder(BasicActivity.this)
                                .setTitle("提示")
                                .setMessage("登录成功")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(BasicActivity.this, "对话框“取消”按钮被点击", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(BasicActivity.this, "对话框“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .create().show();
                    } else {
                        new AlertDialog.Builder(BasicActivity.this)
                                .setTitle("提示")
                                .setMessage("登录失败")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(BasicActivity.this, "对话框“取消”按钮被点击", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(BasicActivity.this, "对话框“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .create().show();
                    }
                }
            });

            final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int id = radioGroup.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(id);
                    Toast.makeText(BasicActivity.this, rb.getText().toString() +
                            "身份被选中", Toast.LENGTH_SHORT).show();
                }
            });

            Button register  = (Button) findViewById(R.id.register);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int id = radioGroup.getCheckedRadioButtonId();
                    RadioButton rb = (RadioButton) findViewById(id);
                    Toast.makeText(BasicActivity.this, rb.getText().toString() +
                            "身份注册功能尚未开启", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
