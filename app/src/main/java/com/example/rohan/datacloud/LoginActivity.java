package com.example.rohan.datacloud;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.util.List;

public class LoginActivity extends AppCompatActivity  implements View.OnClickListener {


    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 99;
    public static String USER_MAIL;
    // public static  String USER_ID="userId";
    private Button buttonSignin;
    private TextView textView;
    private EditText editTextEmail, editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseUser;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;


    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FirebaseOptions options = new FirebaseOptions.Builder().setApplicationId("datacloud-1c132") // Required for Analytics.
                .setApiKey("AIzaSyBa_BXgrFV1lBqh6rP2iEGY-9_92KG06vg")
                .setDatabaseUrl("https://datacloud-1c132.firebaseio.com")
                .build();

        FirebaseApp.initializeApp(this);
//checkpermi();
        PermissionsCheck();


        FirebaseApp.initializeApp(getApplicationContext());
        FirebaseApp.initializeApp(LoginActivity.this);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));

        }


        progressDialog = new ProgressDialog(this);
        buttonSignin = (Button) findViewById(R.id.button_log);
        editTextEmail = (EditText) findViewById(R.id.mailid);
        editTextPassword = (EditText) findViewById(R.id.pass);
        textView = (TextView) findViewById(R.id.signup);
        buttonSignin.setOnClickListener(this);
        textView.setOnClickListener(this);


    }


    private void PermissionsCheck() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            //  SetLocation();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SetLocation();
                } else {

                    PermissionsCheck();
                }
                return;
            }
        }
    }


    @Override
    public void onClick(View v) {


        if (v == buttonSignin) {
            userLogin();
        }
        if (v == textView) {
            finish();
            startActivity(new Intent(this, SignUpActivity.class));

        }

    }

    private void userLogin() {

        final String mail = editTextEmail.getText().toString().trim();
        String pwd = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(mail)) {
            Toast.makeText(this, "please enter a mail id", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Toast.makeText(this, "please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }


        progressDialog.setMessage("signing in...");

        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();


                if (task.isSuccessful()) {


                    finish();


                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(intent);

                }


            }
        });

    }


    private void SetLocation() {

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED)) {

            fusedLocationProviderClient = new FusedLocationProviderClient(this);
            locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setFastestInterval(2000);
            locationRequest.setInterval(4000);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    super.onLocationResult(locationResult);

                    double lati = locationResult.getLastLocation().getLatitude();
                    double longi = locationResult.getLastLocation().getLongitude();




                }
            }, getMainLooper());

        } else {
            PermissionsCheck();
        }


    }

}