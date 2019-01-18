package com.example.yohan.firebaseauthenticationtest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;

public class SignUpactivity extends AppCompatActivity implements View.OnClickListener{

    EditText userName,Password;
    Button login,signUp;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upactivity);

        userName = findViewById(R.id.etEmail1);
        Password = findViewById(R.id.etPassword1);
        progressBar = findViewById(R.id.ProgressBar1);


        findViewById(R.id.btnSignUp1).setOnClickListener(this);
        findViewById(R.id.btnLogin1).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();




    }

    private void registerUser() {

        String email = userName.getText().toString().trim();
        String password = Password.getText().toString().trim();

        if(email.isEmpty()){
            userName.setError("Email is required");
            userName.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            userName.setError("Please enter valid email");
            userName.requestFocus();
            return;
        }

        if(password.isEmpty()){
            Password.setError("Password is required");
            Password.requestFocus();
            return;
        }

        if(password.length() < 6){
            userName.setError("Minimum length of password shoul be 6");
            userName.requestFocus();
            return;

        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){

                   Toast.makeText(getApplicationContext(),"User Registered successfull",Toast.LENGTH_LONG).show();

                    finish();
                    Intent intent = new Intent(SignUpactivity.this,ActivityProfile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{
                   if(task.getException() instanceof FirebaseAuthUserCollisionException){

                       Toast.makeText(getApplicationContext(),"You are already registred",Toast.LENGTH_LONG).show();
                       Intent intent = new Intent(SignUpactivity.this,MainActivity.class);
                       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }else {
                       Toast.makeText(getApplicationContext(),"Some Error occurred!",Toast.LENGTH_LONG).show();
                   }

                }


            }
        });








    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btnSignUp1:

                registerUser();
                break;
            case R.id.btnLogin1:

                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }

    }


}
