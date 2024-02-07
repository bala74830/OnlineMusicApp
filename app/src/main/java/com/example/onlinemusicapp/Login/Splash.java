package com.example.onlinemusicapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.example.onlinemusicapp.MainActivity;
import com.example.onlinemusicapp.R;

public class Splash extends AppCompatActivity {

    SharedPreferences settings;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        settings = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primarydark));
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (settings.getBoolean("ft", false)){
                    Intent i =new Intent(Splash.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Intent i =new Intent(Splash.this,LoginPage.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 2000);
    }
}