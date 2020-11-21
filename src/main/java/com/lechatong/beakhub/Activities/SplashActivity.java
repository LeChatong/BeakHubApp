package com.lechatong.beakhub.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.lechatong.beakhub.Activities.LoginActivity;
import com.lechatong.beakhub.R;

public class SplashActivity extends AppCompatActivity {

    Context ctx;
    SharedPreferences sharedPreferences;

    private Intent intent;

    private static final String ID_ACCOUNT = "ID_ACCOUNT";
    private static final String USERNAME = "USERNAME";
    private static final String PREFS = "PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_splash);

        sharedPreferences = getBaseContext().getSharedPreferences(PREFS, MODE_PRIVATE);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(sharedPreferences.contains(ID_ACCOUNT)){
                    intent = new Intent(ctx, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    intent = new Intent(ctx, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        };
        thread.start();
    }
}
