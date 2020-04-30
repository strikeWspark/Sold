package com.dryfire.sold.Developer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dryfire.sold.R;
import com.google.android.material.button.MaterialButton;

public class DeveloperActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private MaterialButton supportButton;
    private ImageView gitbutton;
    private ImageView linkedinButton;
    private ImageView mailButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer);

        toolbar = findViewById(R.id.developertoolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gitbutton = findViewById(R.id.githubPage);
        linkedinButton = findViewById(R.id.linkedIn);
        mailButton = findViewById(R.id.gmail);
        supportButton = findViewById(R.id.developerSupport);

        gitbutton.setOnClickListener(this);
        linkedinButton.setOnClickListener(this);
        mailButton.setOnClickListener(this);
        supportButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.githubPage:
                String url = "https://github.com/strikeWspark";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.linkedIn:
                String link = "https://www.linkedin.com/in/saurabh-tripathi-323a09131";
                Intent in = new Intent(Intent.ACTION_VIEW);
                in.setData(Uri.parse(link));
                startActivity(in);
                break;
            case R.id.gmail:
              /*  String email = "kstipathi7@gmail.com";
                Intent mail = new Intent(Intent.ACTION_VIEW);
                mail.setData(Uri.parse(email));
                startActivity(mail);*/
                break;
            case R.id.developerSupport:
                startActivity(new Intent(DeveloperActivity.this,SupportDevelopment.class));
                break;
        }
    }
}
