package com.dryfire.sold.Activities;

import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.widget.ImageButton;


import com.dryfire.sold.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private ImageButton profilepic;
    private TextInputLayout nameInput,usernameInput,passwordinput;
    private TextInputLayout confirmpwdInput,mobileInput;
    private TextInputEditText nameEdit,usernameEdit,passwordEdit;
    private TextInputEditText confirmEdit,mobilrEdit;
    private TextInputLayout upiInput;
    private TextInputEditText upiEdit;
    private TextInputLayout locationInput;
    private TextInputEditText locationEdit;
    private MaterialButton signupButton;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mDatabase;
    private FirebaseAuth mAuth;
    private Uri resulturi = null;
    private final static int GALLERY_CODE = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_sign_activity);


        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        mAuth = FirebaseAuth.getInstance();

        profilepic = findViewById(R.id.sold_profile_add);
        nameInput = findViewById(R.id.sold_signup_name_input);
        nameEdit = findViewById(R.id.sold_signup_name_edit);

        usernameInput = findViewById(R.id.sold_signup_username_input);
        usernameEdit = findViewById(R.id.sold_signup_username_edit);

        passwordinput = findViewById(R.id.sold_signup_password_input);
        passwordEdit = findViewById(R.id.sold_signup_password_edit);

        confirmpwdInput = findViewById(R.id.sold_signup_confirm_input);
        confirmEdit = findViewById(R.id.sold_sign_confirm_edit);

        upiInput = findViewById(R.id.sold_upi_input);
        upiEdit = findViewById(R.id.sold_upi_edit);

        locationInput = findViewById(R.id.sold_location_input);
        locationEdit = findViewById(R.id.sold_location_edit);

        mobileInput = findViewById(R.id.sold_signup_mobile_input);
        mobilrEdit = findViewById(R.id.sold_signup_mobile_edit);

        signupButton = findViewById(R.id.sold_signup);

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,GALLERY_CODE);
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {

        final String name = nameEdit.getText().toString().trim();
        final String email = usernameEdit.getText().toString().trim();
        final String payment_upi = upiEdit.getText().toString().trim();
        final String sign_location = locationEdit.getText().toString().trim();
        final String sign_mobile = mobilrEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !(TextUtils.isEmpty(password)) && !(TextUtils.isEmpty(name))
          && !(TextUtils.isEmpty(payment_upi)) && !(TextUtils.isEmpty(sign_location))){
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            System.out.println("Inside onSuccess");
                            String userid = mAuth.getCurrentUser().getUid();
                            DatabaseReference currDB = mDatabaseReference.child(userid);
                            currDB.child("name").setValue(name);
                            currDB.child("username").setValue(email);
                            currDB.child("upiId").setValue(payment_upi);
                            // currDB.child("mobile").setValue(sign_mobile);
                            currDB.child("location").setValue(sign_location);
                            currDB.child("image").setValue("none");


                            startActivity(new Intent(SignUpActivity.this,MainActivity.class));
                            finish();

                        }
                    });
        }else{
            nameInput.setError(getString(R.string.error_message));
            usernameInput.setError(getString(R.string.error_message));
            locationInput.setError(getString(R.string.error_message));
            upiInput.setError(getString(R.string.error_message));

        }
        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK){
            Uri mImageUri = data.getData();
            resulturi = mImageUri;
            profilepic.setImageURI(resulturi);
        }
    }
}
