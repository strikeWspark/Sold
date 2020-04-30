package com.dryfire.sold.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
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
    private StorageReference mstorageReference;
    private DatabaseReference mSoldDatabase;
    private FirebaseDatabase mDatabase;
    private static final int GALLERY_CODE = 1;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_add_product);

        mProgress = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mstorageReference = FirebaseStorage.getInstance().getReference();
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

        mProgress.setMessage("Uploading Data...");
        mProgress.show();
        mProgress.setCancelable(false);
        final String item_title = item_name.getText().toString().trim();
        final String item_desc = item_description.getText().toString().trim();
        final String bid_price = item_price.getText().toString().trim();


        if(!TextUtils.isEmpty(item_title) && !TextUtils.isEmpty(item_desc) &&
                !TextUtils.isEmpty(bid_price) && mimageUri != null){


            final StorageReference filepath = mstorageReference.child("MSold_Images").child(mimageUri.getLastPathSegment());
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    String sold_image_url = taskSnapshot.getUploadSessionUri().toString();
                    String sub_sold_url = sold_image_url.substring(0,sold_image_url.indexOf("&uploadType"));
                    String constant_url = "&alt=media";
                    String final_image_url = sub_sold_url + constant_url;

                    DatabaseReference newItem = mSoldDatabase.push();

                    Map<String,String> dataToSave = new HashMap<>();
                    String post_key = (String.valueOf(newItem)).substring(40);




                    dataToSave.put("key",post_key );
                    dataToSave.put("item_name",item_title);
                    dataToSave.put("description",item_desc);
                    dataToSave.put("min_bid_price",bid_price);
                    dataToSave.put("item_image",final_image_url);
                    dataToSave.put("userId",mUser.getUid());
                    dataToSave.put("username",mUser.getEmail());

                    newItem.setValue(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mProgress.dismiss();
                            Toast.makeText(AddProductActivity.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddProductActivity.this,MainActivity.class));
                            finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                        }
                    });


                }
            });
        }

        /*newItem.child("item_name").setValue(item_title);
        newItem.child("description").setValue(item_desc);
        newItem.child("min_bid_price").setValue(bid_price);
        newItem.child("item_image").setValue("none");
        newItem.child("userId").setValue(mUser.getUid());
        newItem.child("username").setValue(mUser.getEmail());*/


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
