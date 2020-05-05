package com.dryfire.sold.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dryfire.sold.Developer.SupportDevelopment;
import com.dryfire.sold.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener{
    TextView textView;
    private ImageView badbutton;
    private ImageView averagebutton;
    private ImageView goodbutton;
    private ImageView verygoodbutton;
    private ImageView awesomebutton;
    private FirebaseUser mUser;
    private Toolbar toolbar;
    private DatabaseReference databaseReference;
    private MaterialButton submitButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_feedback);



        mUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("MFeedback");
        toolbar = findViewById(R.id.feedtoolbar);
        toolbar.setTitle(" ");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textView = findViewById(R.id.welcome_name);
        badbutton = findViewById(R.id.rate_bad);
        averagebutton = findViewById(R.id.rate_average);
        goodbutton = findViewById(R.id.rate_good);
        verygoodbutton = findViewById(R.id.rate_verygood);
        awesomebutton = findViewById(R.id.rate_awesome);
        submitButton = findViewById(R.id.rateButton);
        String s = mUser.getEmail();
        int index = s.indexOf("@");
        String finalString = s.substring(0,index);
        textView.setText("Welcome, Mr." + finalString);

        badbutton.setOnClickListener(this);
        averagebutton.setOnClickListener(this);
        goodbutton.setOnClickListener(this);
        verygoodbutton.setOnClickListener(this);
        awesomebutton.setOnClickListener(this);
        submitButton.setOnClickListener(this);
    }

    public void sendFeedback(String feedback){

        databaseReference.child(mUser.getUid()).child("message").setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rate_bad:
                badbutton.setImageResource(R.drawable.bad_check);
                averagebutton.setImageResource(R.drawable.average);
                goodbutton.setImageResource(R.drawable.good);
                verygoodbutton.setImageResource(R.drawable.verygood);
                awesomebutton.setImageResource(R.drawable.awesome);
                sendFeedback("Bad");
                break;
            case R.id.rate_average:
                badbutton.setImageResource(R.drawable.bad);
                averagebutton.setImageResource(R.drawable.average_check);
                goodbutton.setImageResource(R.drawable.good);
                verygoodbutton.setImageResource(R.drawable.verygood);
                awesomebutton.setImageResource(R.drawable.awesome);

                sendFeedback("Average");
                break;
            case R.id.rate_good:
                badbutton.setImageResource(R.drawable.bad);
                averagebutton.setImageResource(R.drawable.average);
                goodbutton.setImageResource(R.drawable.good_check);
                verygoodbutton.setImageResource(R.drawable.verygood);
                awesomebutton.setImageResource(R.drawable.awesome);
                sendFeedback("Good");
                break;
            case R.id.rate_verygood:
                badbutton.setImageResource(R.drawable.bad);
                averagebutton.setImageResource(R.drawable.average);
                goodbutton.setImageResource(R.drawable.good);
                verygoodbutton.setImageResource(R.drawable.verygood_check);
                awesomebutton.setImageResource(R.drawable.awesome);
                sendFeedback("Very Good");
                break;
            case R.id.rate_awesome:
                badbutton.setImageResource(R.drawable.bad);
                averagebutton.setImageResource(R.drawable.average);
                goodbutton.setImageResource(R.drawable.good);
                verygoodbutton.setImageResource(R.drawable.verygood);
                awesomebutton.setImageResource(R.drawable.awesome_check);
                sendFeedback("Awesome");
                break;
            case R.id.rateButton:
                Toast.makeText(FeedbackActivity.this, "Thank you for the feedback", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(FeedbackActivity.this,MainActivity.class));
                finish();
                break;

        }

    }

}
