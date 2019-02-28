package com.example.wechat.User;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.wechat.MainActivity;
import com.example.wechat.R;


public class RegisterActivity extends AppCompatActivity {
    EditText username;
    EditText password;
    EditText checkPassword;
    EditText phoneNumber;
    EditText yanzhengma;
    EditText email;
    Button getyanzhengma;
    Button regisetButton;
    TextInputLayout passwordLayout;
    TextInputLayout checkPasswordLayout;
    String usernameText = "";
    String phoneNumberText = "";
    String passwordText = "";
    String checkPasswordText = "";
    String emailText = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }


    private void init(){
        username = findViewById(R.id.username);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        checkPassword = findViewById(R.id.checkPassword);
        yanzhengma = findViewById(R.id.yanzhengma);
        getyanzhengma = findViewById(R.id.getyanzhengma);
        regisetButton = findViewById(R.id.regist);
        passwordLayout = findViewById(R.id.passwordLayout);
        checkPasswordLayout = findViewById( R.id.checkPasswordLayout);

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                phoneNumberText = String.valueOf(s);
                if (phoneNumber.length() != 11)
                    phoneNumber.setError("手机号输入有误");

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailText = String.valueOf(s);
                if (!emailText.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$"))
                    email.setError("邮箱有误");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordText = String.valueOf(s);
                if (passwordText.length() < 6){
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError("密码不能小于6位");
                }
                else
                    passwordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        checkPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkPasswordText = String.valueOf(s);
                if (!checkPasswordText.equals(passwordText)){
                    checkPasswordLayout.setErrorEnabled(true);
                    checkPasswordLayout.setError("两次密码不一样");
                }else
                    checkPasswordLayout.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getyanzhengma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTime();
            }
        });
        regisetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameText = username.getText().toString();
                if(phoneNumberText.length() == 11 && passwordText.length() >= 6 && passwordText.equals(checkPasswordText)){
                    register(usernameText,passwordText,emailText);
                }else {
                    Toast.makeText(RegisterActivity.this,"手机号或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void countTime(){
        CountDownTimer countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                getyanzhengma.setEnabled(false);
                getyanzhengma.setText(millisUntilFinished / 1000  + " s后可重新获取");
            }

            @Override
            public void onFinish() {
                getyanzhengma.setText("点击获取验证码");
                getyanzhengma.setEnabled(true);
            }
        };
        countDownTimer.start();
    }

    public static Intent newIntent(Context packageContext){
        Intent intent = new Intent(packageContext,RegisterActivity.class);

        return intent;
    }
    private void register(String username,String password,String emailText){
        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(username);// 设置用户名
        user.setPassword(password);// 设置密码
        user.setEmail(emailText);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    Toast.makeText(RegisterActivity.this, "注册失败（用户名已存在或者其他原因）", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
