package com.dryfire.sold.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    private Sold sold;
    private Toolbar toolbar;
    private ListView sold_listview;
    private TextView sold_profile_name;
    private TextView sold_profile_upi;
    private TextView sold_profile_mobile;
    private TextView sold_profile_location;
    private TextView sold_profile_username;
    private ImageView profile_picture;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_profile_activity);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        toolbar = findViewById(R.id.sold_profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance();

        String Id = getIntent().getStringExtra("ID");
        mDatabaseReference = mDatabase.getReference("MUsers").child(Id);

        Toast.makeText(this,""+Id,Toast.LENGTH_LONG).show();
        sold_profile_name = findViewById(R.id.sold_name_view);
        sold_profile_upi = findViewById(R.id.sold_upiId_view);
        sold_profile_mobile = findViewById(R.id.sold_mobile_no_view);
        sold_profile_location = findViewById(R.id.sold_location_view);
        sold_profile_username = findViewById(R.id.sold_username_view);

        getProfileDetails();
    }

    private void getProfileDetails() {
       // sold_profile_name.setText(sold.getName());
      //  sold_profile_upi.setText(sold.getUpiId());
       // sold_profile_mobile.setText(sold.getMobile_no());
       //sold_profile_location.setText(sold.getLocation());
        sold_profile_username.setText(mUser.getEmail());
    }
}
