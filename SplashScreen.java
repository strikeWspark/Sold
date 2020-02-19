package com.dryfire.sold.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.dryfire.sold.R;

import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity implements Runnable{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_splash_screen);

        new Handler().postDelayed(this,2000);
    }

    @Override
    public void run() {
       startActivity(new Intent(this,LoginActivity.class));
       finish();
    }
}
