package com.example.rohan.datacloud;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PayDetailsActivity extends AppCompatActivity {

    String ol,ne;
    TextView tn,to,tlocati,ttorc,tdom,tchoice;
    String choice="Regular";
    String sdom,sloc,storc,sopt;
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_details);
        to = findViewById(R.id.oldprice);
        tn = findViewById(R.id.newprice);
        tlocati = findViewById(R.id.loc);
        ttorc = findViewById(R.id.tors);
        tdom = findViewById(R.id.dom);
        tchoice=findViewById(R.id.ches);
        //tn = findViewById(R.id.newprice);


        startdialog();

        Intent i = getIntent();

       sloc = i.getStringExtra("loc");
       sdom = i.getStringExtra("val");
       storc = i.getStringExtra("torc");

       ttorc.setText(storc);
       tdom.setText(sdom);
       tlocati.setText(sloc);







        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("price").child(sloc).child(sdom);
        try {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String d[] = dataSnapshot.getValue().toString().split(",");
                    Log.v("stur", dataSnapshot.getValue().toString());
                    ol = d[0];
                    ne = d[1];
                    if (ol != null && ne != null) {
                        to.setText(ol);
                        tn.setText(ne);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




        }
        catch (Exception e)
        {

        }
    }

    private void startdialog() {


        final Dialog dialog = new Dialog(PayDetailsActivity.this, R.style.heme_Dialog);
        dialog.setContentView(R.layout.dialogftolrewr);
        //View view=getLayoutInflater().inflate(R.layout.dialogtesttrain,null);
        Button btweek = dialog.findViewById(R.id.buttonweek);
        Button btregu = dialog.findViewById(R.id.buttonregu);
        Button btonline = dialog.findViewById(R.id.buttonon);
        Button btftrack = dialog.findViewById(R.id.buttonfat);



        btweek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    choice = "Weekend";
              //  dialogloc();
                tchoice.setText(choice);

                dialog.dismiss();
            }
        });

        btregu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = "Regular";
                tchoice.setText(choice);

                //dialogloc();
                dialog.dismiss();
            }
        });


        btonline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = "Online";
                tchoice.setText(choice);

                //  dialogloc();
                dialog.dismiss();
            }
        });

        btftrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice = "Fast track";
                tchoice.setText(choice);

                //dialogloc();
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);



    }
}
