package com.medi.LoginPage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.medi.dailynews.MainActivity;
import com.medi.dailynews.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(intent);
                SharedPreferences preferences = getSharedPreferences("UserLogin",MODE_PRIVATE);
                boolean Chack = preferences.getBoolean("flag",false);
                Intent intent;
                if(Chack){
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);


                }
                else {
                    intent=  new Intent(SplashScreenActivity.this,LoginActivity.class);
                }
                startActivity(intent);
                finish();

            }
        },2000);
    }
}