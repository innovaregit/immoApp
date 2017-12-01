package com.innovare.marceloagustini.immoapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.innovare.marceloagustini.immoapp.controllers.LoginActivity;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_TIME = 6 * 1000;// 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            new Handler().postDelayed(new Runnable() {

                public void run() {

                    Intent intent = new Intent(SplashActivity.this,
                            LoginActivity.class);
                    startActivity(intent);

                    SplashActivity.this.finish();

                }


            }, SPLASH_TIME);

            new Handler().postDelayed(new Runnable() {
                public void run() {
                }
            }, SPLASH_TIME);
        } catch (Exception e) {
        }
    }




    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }
}
