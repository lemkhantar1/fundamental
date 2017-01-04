package com.lemkhantar.fundamental.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.lemkhantar.fundamental.R;
import com.lemkhantar.fundamental.helper.TimeService;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2500;
    Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intentService = new Intent(this, TimeService.class);
        startService(intentService);

        new Handler().postDelayed(new Runnable()
        {
            @Override public void run()
            {
                Intent i = new Intent(MainActivity.this, Accumulateur.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);



    }




}


