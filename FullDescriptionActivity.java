package com.dryfire.sold.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FullDescriptionActivity extends AppCompatActivity {

    private Sold sold;
    private TextView full_itemname,full_description,full_bidprice;
    private TextView full_username;
    private MaterialButton full_bid_button;
    private ImageView full_item_image;
    private Toolbar full_toolbar;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_full_description);


        mUser = FirebaseAuth.getInstance().getCurrentUser();
        sold = (Sold) getIntent().getSerializableExtra("sold");

        full_toolbar = findViewById(R.id.sold_full_toolbar);
        setSupportActionBar(full_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        full_itemname = findViewById(R.id.sold_full_itemname);
        full_description = findViewById(R.id.sold_full_description);
        full_bid_button = findViewById(R.id.sold_full_bidButton);
        full_bidprice = findViewById(R.id.sold_full_bidprice);
        full_username = findViewById(R.id.sold_full_username);
        full_item_image = findViewById(R.id.sold_full_image);


        full_itemname.setText(sold.getItem_name());
        full_description.setText("\t\tProduct Description:\n\t\t\t\t\t" + sold.getDescription());
        full_bidprice.setText("$ " +sold.getMin_bid_price());
        full_username.setText("Uploaded By:- " + mUser.getEmail());
    }
}
