package com.example.testibm;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class SplashScreen extends Activity {
    Handler handler;
    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN); //enable full screen

        imageView=findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.splashscreen);
        handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,SignIn.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplashScreen.this).toBundle());
                finish();
                Toast.makeText(SplashScreen.this,"Hello",Toast.LENGTH_SHORT).show();
            }
        },2000);
    }
}
