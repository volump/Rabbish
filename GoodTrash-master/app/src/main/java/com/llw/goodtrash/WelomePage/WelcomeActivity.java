package com.llw.goodtrash.WelomePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.llw.goodtrash.MainActivity;
import com.llw.goodtrash.R;

import Login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler().postDelayed(new Runnable(){

            @Override
            public void run() {
                Intent loginItent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(loginItent);
                finish();
            }
        },1600);

    }
}