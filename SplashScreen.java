package com.dryfire.sold.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.dryfire.sold.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity implements Runnable{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_splash_screen);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        new Handler().postDelayed(this,2000);
    }

    @Override
    public void run() {

        if(mUser != null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else{
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        }

    }
}
