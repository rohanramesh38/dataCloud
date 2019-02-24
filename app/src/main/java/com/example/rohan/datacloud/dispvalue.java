package com.example.rohan.datacloud;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class dispvalue extends AppCompatActivity {
String val,dom;
    TextView t1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispvalue);


        Intent i=getIntent();
        val=i.getStringExtra("val");
        dom=i.getStringExtra("dom");
t1=findViewById(R.id.textf);






    }

    @Override
    protected void onStart() {
        super.onStart();

        DatabaseReference databasReference = FirebaseDatabase.getInstance().getReference("data").child(dom).child(val);

        Log.v("valee",databasReference.toString());

        // JSONObject obj=(JSONObject) databasReference;
        databasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {



                startCountAnimation(Integer.parseInt(dataSnapshot.getValue().toString()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void startCountAnimation(int num){
        ValueAnimator animator = ValueAnimator.ofInt(0, num);
        animator.setDuration(250);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                t1.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

}
