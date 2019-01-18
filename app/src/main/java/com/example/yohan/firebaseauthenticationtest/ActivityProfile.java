package com.example.yohan.firebaseauthenticationtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class ActivityProfile extends AppCompatActivity {

    ImageView profilepic;
    EditText displayName;

    Button btnsave;
    private final static int Choose_REQ_NO = 1;
    Uri uriProfilePic;
    ProgressBar progressBar3;
    String downloadlink;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        profilepic = findViewById(R.id.ivProPiC);
        displayName = findViewById(R.id.etDisplayName);
        btnsave = findViewById(R.id.btnSave);
        progressBar3 = findViewById(R.id.ProgressBar3);

        mAuth = FirebaseAuth.getInstance();


        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                chooseImgae();
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveUserInformation();

            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }

    }



    //save user Information
    private void saveUserInformation() {
        String DisplayName = displayName.getText().toString().trim();
        if(DisplayName.isEmpty()){
            displayName.setError("Name Required");
            displayName.requestFocus();
            return;

        }
        //get the curren user
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null && downloadlink  != null){
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(DisplayName)
                    .setPhotoUri(Uri.parse(downloadlink))
                    .build();

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"profile updated",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            startActivity(new Intent(this,Profile.class));
        }


    }

    //choose image from phone
    private void chooseImgae(){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Select Profile Picture"),Choose_REQ_NO);

    }

    //get the image selected image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(Choose_REQ_NO == requestCode && resultCode ==RESULT_OK && data != null && data.getData() != null){

        uriProfilePic  =  data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriProfilePic);
                profilepic.setImageBitmap(bitmap);
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();;

            }
        }
    }

    //upload images to firebase Storgae
    private void uploadImage(){
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference("ProfilePic/"+System.currentTimeMillis()+".jpg");

        if(uriProfilePic != null){
            progressBar3.setVisibility(View.VISIBLE);
            storageReference.putFile(uriProfilePic).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar3.setVisibility(View.GONE);

                     downloadlink = storageReference.getDownloadUrl().toString();
                     /*Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
           while (!urlTask.isSuccessful());
           Uri downloadUrl = urlTask.getResult();

           final String sdownload_url = String.valueOf(downloadUrl);*/

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar3.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
