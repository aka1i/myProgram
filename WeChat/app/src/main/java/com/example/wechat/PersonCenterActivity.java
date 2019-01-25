package com.example.wechat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;

public class PersonCenterActivity extends AppCompatActivity {
    Button mLogoutButton;
    Button mToRegisterButton;
    Button mToLoginButton;
    TextView mMyUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        init();
    }

    void init(){
        mLogoutButton = findViewById(R.id.logout);
        mToRegisterButton = findViewById(R.id.toregister);
        mToLoginButton = findViewById(R.id.tologin);
        mMyUsername = findViewById(R.id.my_username);
        if (AVUser.getCurrentUser() != null)
            mMyUsername.setText(AVUser.getCurrentUser().getUsername());
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        mToRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = RegisterActivity.newIntent(getApplicationContext());
                startActivity(intent);
            }
        });
        mToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.newIntent(getApplicationContext());
                startActivity(intent);
            }
        });
        if (AVUser.getCurrentUser() != null){
            mToLoginButton.setVisibility(View.INVISIBLE);
            mToRegisterButton.setVisibility(View.INVISIBLE);
        }else {
            mLogoutButton.setVisibility(View.INVISIBLE);
        }

    }
    private void logout(){
        AVUser.getCurrentUser().logOut();
        finish();
    }
}
