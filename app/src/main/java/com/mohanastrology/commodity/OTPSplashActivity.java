package com.mohanastrology.commodity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class OTPSplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpsplash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        final int splashTime = 3000;

        Thread runTime = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    int wait = 0;
                    while (wait < splashTime) {
                        sleep(50);
                        wait += 50;
                    }
                } catch (Exception e) {
                } finally {
                    startActivity(new Intent(OTPSplashActivity.this,
                            MainActivity.class));
                    finish();
                }
            }
        };
        runTime.start();

    }
}

