package com.dryfire.sold.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dryfire.sold.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AddProductActivity extends AppCompatActivity {

    private Toolbar add_toolbar;
    private ImageButton item_image;
    private TextInputEditText item_name;
    private TextInputEditText item_description;
    private TextInputEditText item_price;
    private MaterialButton add_product_button;
    private Uri mimageUri;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mSoldDatabase;
    private FirebaseDatabase mDatabase;
    private static final int GALLERY_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_add_product);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mDatabase = FirebaseDatabase.getInstance();
        mSoldDatabase = mDatabase.getReference().child("MSold");


        add_toolbar = findViewById(R.id.sold_add_product_toolbar);
        setSupportActionBar(add_toolbar);
        add_toolbar.setTitle("Add Bid");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        item_image = (ImageButton) findViewById(R.id.sold_product_image);
        item_name = (TextInputEditText) findViewById(R.id.sold_add_itemname);
        item_description = (TextInputEditText) findViewById(R.id.sold_add_description);
        item_price = (TextInputEditText) findViewById(R.id.sold_add_bidPrice);
        add_product_button = (MaterialButton) findViewById(R.id.sold_add_product);

        item_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        add_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startAdding();
            }
        });


    }

    private void startAdding() {
        final String item_title = item_name.getText().toString().trim();
        final String item_desc = item_description.getText().toString().trim();
        final String bid_price = item_price.getText().toString().trim();

        DatabaseReference newItem = mSoldDatabase.push();
        newItem.child("item_name").setValue(item_title);
        newItem.child("description").setValue(item_desc);
        newItem.child("min_bid_price").setValue(bid_price);
        newItem.child("item_image").setValue("none");
        newItem.child("userId").setValue(mUser.getUid());
        newItem.child("username").setValue(mUser.getEmail());

        Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AddProductActivity.this,MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null){
            mimageUri = data.getData();
            item_image.setImageURI(mimageUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
