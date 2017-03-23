package com.example.kaixin.myapplication;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by kaixin on 2016/10/9.
 */
public class ExpandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);

        final TextInputLayout usernameText = (TextInputLayout) findViewById(R.id.username_hint);
        final TextInputLayout passwordText = (TextInputLayout) findViewById(R.id.password_hint);
        final EditText username = usernameText.getEditText();
        final EditText password = passwordText.getEditText();
        Button login  = (Button) findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    usernameText.setErrorEnabled(true);
                    usernameText.setError("用户名不能为空");
                } else if (TextUtils.isEmpty(password.getText().toString())) {
                    passwordText.setErrorEnabled(true);
                    passwordText.setError("密码不能为空");
                } else if ("Android".equals(username.getText().toString())
                        && "123456".equals(password.getText().toString())) {
                    Snackbar.make(password, "登录成功", Snackbar.LENGTH_LONG)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(ExpandActivity.this, "Snackbar“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                } else {
                    Snackbar.make(password, "登录失败", Snackbar.LENGTH_LONG)
                            .setAction("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(ExpandActivity.this, "Snackbar“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                                }
                            }).show();
                }
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    usernameText.setErrorEnabled(true);
                    usernameText.setError("用户名不能为空");
                } else {
                    usernameText.setError(null);
                }
            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(username.getText().toString())) {
                    usernameText.setErrorEnabled(true);
                    usernameText.setError("用户名不能为空");
                } else {
                    usernameText.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString())) {
                    passwordText.setErrorEnabled(true);
                    passwordText.setError("密码不能为空");
                } else {
                    passwordText.setError(null);
                }
            }
        });

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                Snackbar.make(radioGroup, rb.getText().toString() +
                        "身份被选中", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ExpandActivity.this, "Snackbar“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        final Button register  = (Button) findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                Snackbar.make(register,  rb.getText().toString() +
                        "身份注册功能尚未开启", Snackbar.LENGTH_LONG)
                        .setAction("确定", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ExpandActivity.this, "Snackbar“确定”按钮被点击", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });
    }
}
