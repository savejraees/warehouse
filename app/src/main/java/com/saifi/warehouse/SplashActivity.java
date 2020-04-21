package com.saifi.warehouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    String id ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        Thread background = new Thread() {
            public void run() {
                try {
                    // Thread will sleep for 5 seconds
                    sleep(3*1000);

                    if(id.isEmpty()){
                        Intent i=new Intent(getBaseContext(),LoginAcivity.class);
                        startActivity(i);

                    }
                    else{
                        startActivity(new Intent(getApplicationContext(),LoginAcivity.class));
                    }
                    // After 5 seconds redirect to another intent

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };
        // start thread
        background.start();
    }
}
