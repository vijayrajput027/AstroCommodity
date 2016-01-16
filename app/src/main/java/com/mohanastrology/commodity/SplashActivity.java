package com.mohanastrology.commodity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mohanastrology.commodity.javafiles.ApplicationStatus;

public class SplashActivity extends Activity {

    private ApplicationStatus status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ImageView animationTarget = (ImageView) this.findViewById(R.id.back_img);

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.rotate_around_center_point);
        animationTarget.startAnimation(animation);

        status = new ApplicationStatus(this);
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
                    if(status.getLoginStatus()){
                        startActivity(new Intent(SplashActivity.this,
                                MainActivity.class));
                    }else if(!status.getEmail().equals("")&&status.getEmail() != null){
                        startActivity(new Intent(SplashActivity.this,
                                EnterOTPActivity.class));
                    }else{
                        startActivity(new Intent(SplashActivity.this,
                                MainActivity.class));
                    }
                    finish();
                }
            }
        };
        runTime.start();

    }
}
