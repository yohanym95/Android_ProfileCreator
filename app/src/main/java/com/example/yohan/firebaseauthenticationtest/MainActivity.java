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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnlogin,btnSignUp;
    EditText email,password;
    FirebaseAuth mAuth;
    ProgressBar progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email= findViewById(R.id.etEmail);
        password = findViewById(R.id.etPassword);
        progressBar1 = findViewById(R.id.ProgressBar2);
        findViewById(R.id.btnsignUp).setOnClickListener(this);
        findViewById(R.id.btnLogin).setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
    }


    //make surer and check user is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this,Profile.class));
        }

    }

    private void userLogin() {
        String email1 = email.getText().toString().trim();
        String password1 = password.getText().toString().trim();

        if(email1.isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            email.setError("Please enter valid email");
            email.requestFocus();
            return;
        }

        if(password1.isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            return;
        }

        if(password1.length() < 6){
            password.setError("Minimum length of password shoul be 6");
            password.requestFocus();
            return;

        }

        progressBar1.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email1,password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar1.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    finish();
                    Intent intent = new Intent(MainActivity.this,Profile.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void onClick(View v){

        switch (v.getId()){
            case R.id.btnsignUp:
                finish();
                startActivity(new Intent(this,SignUpactivity.class));
                break;
            case R.id.btnLogin:
                userLogin();

        }
    }



}
