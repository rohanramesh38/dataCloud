package com.example.rohan.datacloud;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectArea extends AppCompatActivity {
TextView t1;
String dom;

    MyAdapter adapter;
    RecyclerView recyclerView;
    DatabaseReference databasReference;
    ArrayList<profile> arealist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_area);

        t1=(TextView) findViewById(R.id.textve);
        Intent i=getIntent();
         dom=i.getStringExtra("val");
        t1.setText(dom);
       databasReference = FirebaseDatabase.getInstance().getReference("List").child(dom);

        recyclerView = (RecyclerView)findViewById(R.id.recycle_area);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        recyclerView.addOnItemTouchListener(new MyAdapter(SelectArea.this, recyclerView, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(SelectActivity.this,"item "+(position+1),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SelectArea.this,dispvalue.class);
                intent.putExtra("val",arealist.get(position).getDomain());
                intent.putExtra("dom",dom);
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        databasReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // String value =  dataSnapshot.getString("Value");
                arealist =new ArrayList<profile>();
                String s = dataSnapshot.getValue().toString().replace("{val=", "").trim();
                String p = s.substring(0, s.indexOf('}'));
                Log.v("reman", p);
                List<String> myList = new ArrayList<String>(Arrays.asList(p.split("_")));
Log.v("listval",myList.toString());





                String f="https://firebasestorage.googleapis.com/v0/b/datacloud-1c132.appspot.com/o/donny.jpeg?alt=media&token=80752b7a-d7e8-4e62-abf8-5f1cfd521f38";
arealist.clear();
                for(int i=0;i<myList.size();i++)
                {
                    f=f.replace("donny",myList.get(i).trim());

f=f.trim();
Log.v("hert",f);
                    profile pr=new profile(f,myList.get(i));
arealist.add(pr);
                    f="https://firebasestorage.googleapis.com/v0/b/datacloud-1c132.appspot.com/o/donny.jpeg?alt=media&token=80752b7a-d7e8-4e62-abf8-5f1cfd521f38";
                }


                adapter = new MyAdapter(SelectArea.this,arealist);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
