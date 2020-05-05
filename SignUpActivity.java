package com.dryfire.sold.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;

import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


import com.dryfire.sold.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
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

public class SignUpActivity extends AppCompatActivity {

    private ImageButton profilepic;
    private Toolbar toolbar;
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
    private StorageReference mstorageReference;
    private FirebaseAuth mAuth;
    private Uri resulturi = null;
    private final static int GALLERY_CODE = 1;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_sign_activity);

        toolbar = findViewById(R.id.sold_signup_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgress = new ProgressDialog(this);
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("MUsers");

        mstorageReference = FirebaseStorage.getInstance().getReference();
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

        passwordEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(isPassowrd(passwordEdit.getText())){
                    passwordinput.setError(null);
                }
                return false;
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
        String confirm_password = confirmEdit.getText().toString().trim();
        mProgress.setMessage("Signing Up...");
        mProgress.show();

        if(!TextUtils.isEmpty(email) && !(TextUtils.isEmpty(password)) && !(TextUtils.isEmpty(name))
          && !(TextUtils.isEmpty(payment_upi)) && !(TextUtils.isEmpty(sign_location)) && resulturi != null){

            isPassowrd(passwordEdit.getText());
            if(password.equals(confirm_password)){
                confirmpwdInput.setError(null);

                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                StorageReference filepath = mstorageReference.child("MSold_propics")
                                        .child(resulturi.getLastPathSegment());
                                filepath.putFile(resulturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        String sold_image_url = taskSnapshot.getUploadSessionUri().toString();
                                        String sub_sold_url = sold_image_url.substring(0,sold_image_url.indexOf("&uploadType"));
                                        String constant_url = "&alt=media";
                                        String final_image_url = sub_sold_url + constant_url;

                                        Map<String,String> dataToSave = new HashMap<>();


                                        String userid = mAuth.getCurrentUser().getUid();
                                        DatabaseReference currDB = mDatabaseReference.child(userid);

                                        dataToSave.put("name",name);
                                        dataToSave.put("username",email);
                                        dataToSave.put("upiId",payment_upi);
                                        dataToSave.put("location",sign_location);
                                        dataToSave.put("profile_image",final_image_url);
                                        dataToSave.put("mobile_no",sign_mobile);
                                        mDatabaseReference.child(userid).setValue(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                mProgress.dismiss();
                                                startActivity(new Intent(SignUpActivity.this,MainActivity.class));
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

                         /*   currDB.child("name").setValue(name);
                            currDB.child("username").setValue(email);
                            currDB.child("upiId").setValue(payment_upi);
                            // currDB.child("mobile").setValue(sign_mobile);
                            currDB.child("location").setValue(sign_location);
                            currDB.child("image").setValue("none");
                           */


                            }
                        });

            }else{
                confirmpwdInput.setError("Your password does't match");
                mProgress.dismiss();
            }




        }else{
            mProgress.dismiss();
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

    private boolean isPassowrd(Editable pwd){
        return pwd != null && pwd.length() >= 8;
    }
}
