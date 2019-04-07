package com.example.rohan.datacloud;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_LOCATION= 99;
    ArrayList<String> spinval;
    ArrayList<String> spval;
    DatabaseReference databasertist, databaseReference, databasReference;
    String loc = "help";
    int x = -1, i;
    Geocoder geocoder;
    RecyclerView recyclerView;
    String pos = "Searching your location..";
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    ArrayList<profile> list1;
    MyAdapter adapter;
        String torc = "Training";
    TextView textViewpos;
    List<String> listloc;
    String dom;
    LocationManager locationManager;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewpos = findViewById(R.id.position);

        startdialog();


        listloc = new ArrayList<String>();

        listloc.add("Anna Nagar");
        listloc.add("Ambattur");
        listloc.add("Avadi");
        listloc.add("Villivakkam:");
        listloc.add("Guindy");
        listloc.add("Kolathur");
        listloc.add("Madhavaram");
        listloc.add("Washermanpet");
        listloc.add("Perambur");
        listloc.add("T Nagar");

        recyclerView = (RecyclerView) findViewById(R.id.recycle_card);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list1 = new ArrayList<profile>();
        PermissionsCheck();


//checkloc();


        spinval = new ArrayList<String>();
        databaseReference = FirebaseDatabase.getInstance().getReference("List");


loc="Fetching your location";
textViewpos.setText(loc);
        recyclerView.addOnItemTouchListener(new MyAdapter(MainActivity.this, recyclerView, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(SelectActivity.this,"item "+(position+1),Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, PayDetailsActivity.class);
                intent.putExtra("val", list1.get(position).getDomain());
                intent.putExtra("loc", loc);
                intent.putExtra("torc",torc);
                if (loc.equals("help")) {
                    //checkloc();
                    Toast.makeText(MainActivity.this, "pls wait fetching location", Toast.LENGTH_SHORT).show();
                } else {
                    if(!loc.equals("Fetching your location"))
                    {startActivity(intent);}
                    else
                    {
                        Toast.makeText(MainActivity.this, "Location not found", Toast.LENGTH_SHORT).show();

                    }
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));



        }

    private void PermissionsCheck() {


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
        else
        {
            SetLocation();
        }


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

                    double   lati = locationResult.getLastLocation().getLatitude();
                    double longi = locationResult.getLastLocation().getLongitude();


                    geocoder = new Geocoder(getApplicationContext());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(lati, longi, 1);
                        String addr = addressList.get(0).getLocality() + "," + addressList.get(0).getAddressLine(0);

                    Log.v("stringloc",addr);
                    String swer=addr.substring(addr.indexOf("600"),addr.indexOf("600")+6);
                    Log.v("swer",swer);

                    for (i=0;i<listloc.size();i++)
                    {
                        if(addr.indexOf(listloc.get(i))>0)
                        {
                            loc=listloc.get(i);

                            break;
                        }

                    }

                    }
                    catch (IOException e)
                    {

                    }

                }
            }, getMainLooper());

        } else {
            PermissionsCheck();
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


    private void startdialog() {

        final Dialog dialog = new Dialog(MainActivity.this, R.style.heme_Dialog);
        dialog.setContentView(R.layout.dialogtesttrain);
        //View view=getLayoutInflater().inflate(R.layout.dialogtesttrain,null);
        Button bttrain = dialog.findViewById(R.id.buttrain);
        Button btcert = dialog.findViewById(R.id.butcert);


        bttrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                torc = "Training";
                dialogloc();
                dialog.dismiss();
            }
        });

        btcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                torc = "Certification";
                dialogloc();
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



    }

    private void dialogloc() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Access your location ").setTitle("Location");
        builder.setCancelable(false)
                .setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SetLocation();
                        textViewpos.setText(loc);
                        dialog.cancel();

                    }
                });
        AlertDialog alert = builder.create();

        alert.show();




    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("List");
        spval = new ArrayList<String>();

        final ArrayList<String> wert = new ArrayList<String>();

        wert.add("https://wallpapercave.com/wp/wp2042036.jpg");
        wert.add("https://www.cbronline.com/wp-content/uploads/2016/09/theinternetofthings.jpg");
        wert.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-YFA5CqHzjzODcfrs1DZJx3E0cZckGMbQ0FDO2MPlSGhKwoMtFg");
        wert.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPHNIbzDrJc6P97uKPasMKaBLCtR3LARqBEZoKmvCVw1rB9jYY");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                i = 0;
                list1.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    //Log.v("values",data.toString());

                    profile p = new profile(wert.get(i), data.getKey());
                    list1.add(p);
                    i++;
                    spval.add(data.getKey());
                }
                Log.v("brden", list1.toString());

                adapter = new MyAdapter(MainActivity.this, list1);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signup) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);


            return true;
        }
        else if(id==R.id.share_action)
        {
            return true;
        }



        return super.onOptionsItemSelected(item);
    }





}

