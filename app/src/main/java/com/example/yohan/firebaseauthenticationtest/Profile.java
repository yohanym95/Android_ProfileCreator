package com.example.yohan.firebaseauthenticationtest;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class Profile extends AppCompatActivity {

    ImageView profilePicl;
    TextView DisplayName;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.drawable.ic_person_black_24dp);
        actionBar.setTitle("Profile Creator");
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        profilePicl = findViewById(R.id.diplaypic);
        DisplayName = findViewById(R.id.dislpayname);
       // DisplayId = findViewById(R.id.dislpayid);
        mAuth = FirebaseAuth.getInstance();

        loadUserInformation();


    }
//check if user not logged in
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,MainActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void loadUserInformation() {


        FirebaseUser user = mAuth.getCurrentUser();

        String photoURL = user.getPhotoUrl().toString();
        String disPlayName = user.getDisplayName();
        String userId = user.getProviderId();

        if(user.getPhotoUrl() != null){
            Glide.with(this)
                    .load(user.getPhotoUrl().toString())
                    .into(profilePicl);
        }

        if(user.getDisplayName() != null){
            DisplayName.setText(user.getDisplayName());
        }
      //  if(user.getProviderId() != null){
           // DisplayId.setText(user.getUid());
       // }

    }


}
