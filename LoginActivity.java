package com.dryfire.sold.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.dryfire.sold.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private MaterialButton nextButton;
    private MaterialButton cancelbutton;
    private MaterialButton signupButton;
    private TextInputLayout usernameInputlayout;
    private TextInputEditText usernameEdittext;
    private TextInputLayout passwordInputlayout;
    private TextInputEditText passwordEdittext;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_login_activity);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mUser = firebaseAuth.getCurrentUser();

                if(mUser != null){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    Toast.makeText(LoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(LoginActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        };

        usernameInputlayout  = (TextInputLayout) findViewById(R.id.sold_input_username);
        usernameEdittext = (TextInputEditText) findViewById(R.id.sold_edit_username);

        passwordInputlayout = (TextInputLayout) findViewById(R.id.sold_textinput_password);
        passwordEdittext = (TextInputEditText) findViewById(R.id.sold_edittext_password);

        cancelbutton = (MaterialButton) findViewById(R.id.sold_cancel_button);
        nextButton = (MaterialButton) findViewById(R.id.sold_next_button);
        signupButton = (MaterialButton) findViewById(R.id.sold_signup_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!TextUtils.isEmpty(usernameEdittext.getText().toString()) &&
                !TextUtils.isEmpty(passwordEdittext.getText().toString())){
                    String email = usernameEdittext.getText().toString().trim();
                    String pwd = passwordEdittext.getText().toString().trim();

                    login(email,pwd);
                }


            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
        }

    private void login(String email, String pwd) {

        mAuth.signInWithEmailAndPassword(email,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Not Signed In", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }
}

