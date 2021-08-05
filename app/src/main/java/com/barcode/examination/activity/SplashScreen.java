package com.barcode.examination.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.barcode.examination.R;
import com.barcode.examination.model.LoginInResponse;
import com.barcode.examination.util.Constants;
import com.barcode.examination.util.Util;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private final Timer timer = new Timer();
    private LoginInResponse loginResponse = null;
    private  final TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if(loginResponse!=null){

                if (Util.getInstance(getApplicationContext()).getString(Constants.DATA_DOWNLOADED)!=null && Util.getInstance(getApplicationContext()).getString(Constants.DATA_DOWNLOADED).matches("true")){
                    startActivity(new Intent(SplashScreen.this, OptionActivity.class));
                }else{
                    startActivity(new Intent(SplashScreen.this, CentreDetailsActivity.class));
                }
            }else{
                startActivity(new Intent(SplashScreen.this, LogInActivity.class));
            }
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ActionBar ab = getSupportActionBar();
        if (null != ab) {

            ab.hide();
        }
        /*Util.logout(this);
        // Go to login page
        Util.getInstance(SplashScreen.this).setString(Constants.DATA_DOWNLOADED,"false");*/
        getWindow().addFlags(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        timer.schedule(timerTask, 1500);

        TextView txt_title = findViewById(R.id.txt_title);
        Typeface typeface_bold = Typeface.createFromAsset(getAssets(), "Metropolis_SemiBold.ttf");

        // set custom typeface
        txt_title.setTypeface(typeface_bold);

        //Get saved login response, if we have
        try {
            loginResponse = (LoginInResponse) Util.getInstance(getApplicationContext()).
                    pickGsonObject(Constants.PREFS_LOGIN_RESPONSE, new TypeToken<LoginInResponse>() {
                    });
        } catch (Exception e) {
            /*Do Nothing*/
        }

    }

}