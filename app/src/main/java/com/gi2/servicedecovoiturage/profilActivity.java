package com.gi2.servicedecovoiturage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gi2.servicedecovoiturage.logregform.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class profilActivity extends AppCompatActivity {

    ArrayList<String> infos;
    TextView username;
    TextView email;
    TextView phone;

    private Button btnChoose, btnLogout, btnReport;
    private ImageView imageView;
    RatingBar mRatingBar;
    TextView mRatingScale;
    Button mSendFeedback;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);


        Bundle b = getIntent().getExtras();
        infos = null;
        if(b != null)
            infos = b.getStringArrayList("infos");

        username = (TextView)findViewById(R.id.username);
        username.setText(infos.get(0));

        email = (TextView)findViewById(R.id.email);
        email.setText(infos.get(1));

        phone = (TextView)findViewById(R.id.phone);
        phone.setText(infos.get(2));

//        btnChoose = (Button) findViewById(R.id.btnChoose);
        imageView = (ImageView) findViewById(R.id.profilePicture);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        StorageReference storageRef = storage.getReferenceFromUrl("gs://service-de-covoiturage.appspot.com")
                .child("users/"+ infos.get(0)+".jpg");
        try {
            final File localFile = File.createTempFile("images", "jpg");
            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });
        } catch (IOException e ) {}





        btnReport = (Button) findViewById(R.id.buttonReport);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewReportDialog dialog = new NewReportDialog();
                dialog.show(getSupportFragmentManager(),"Reporting");

            }
        });

         mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
         mRatingScale = (TextView) findViewById(R.id.tvRatingScale);
         mSendFeedback = (Button) findViewById(R.id.btnSubmit);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

            }
        });

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Perfect");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });


        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                    mRatingBar.setRating(0);
                    Toast.makeText(profilActivity.this, "Thank you for your rating", Toast.LENGTH_SHORT).show();

            }
        });

    }







}


