package com.example.rohan.datacloud;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SearchIterator;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> spinval,spval;
    DatabaseReference databasertist, databaseReference,databasReference;
    public static final String ARTIST_NAME = "artistName";
    public static final String ARTIST_ID = "artistId";
int x=-1,i;

    RecyclerView recyclerView;

    ArrayList<profile> list1;
    MyAdapter adapter;

    String wer="";
    TextView textView;

    String dom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = (RecyclerView)findViewById(R.id.recycle_card);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        list1 = new ArrayList<profile>();

        spinval = new ArrayList<String>();
        databaseReference = FirebaseDatabase.getInstance().getReference("List");



        recyclerView.addOnItemTouchListener(new MyAdapter(MainActivity.this, recyclerView, new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Toast.makeText(SelectActivity.this,"item "+(position+1),Toast.LENGTH_LONG).show();
                 Intent intent=new Intent(MainActivity.this,SelectArea.class);
                intent.putExtra("val",list1.get(position).getDomain());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
    }


    @Override
    protected void onStart() {
        super.onStart();
        databaseReference = FirebaseDatabase.getInstance().getReference("List");
spval=new ArrayList<String>();

        final ArrayList<String> wert=new ArrayList<String>();

        wert.add("https://wallpapercave.com/wp/wp2042036.jpg");
        wert.add("https://www.cbronline.com/wp-content/uploads/2016/09/theinternetofthings.jpg");
        wert.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-YFA5CqHzjzODcfrs1DZJx3E0cZckGMbQ0FDO2MPlSGhKwoMtFg");
        wert.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQPHNIbzDrJc6P97uKPasMKaBLCtR3LARqBEZoKmvCVw1rB9jYY");

databaseReference.addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        i=0;
        list1.clear();
        for (DataSnapshot data : dataSnapshot.getChildren()){
            //Log.v("values",data.toString());

            profile p=new profile(wert.get(i),data.getKey());
            list1.add(p);
            i++;
spval.add(data.getKey());
        }
        Log.v("brden",list1.toString());

        adapter = new MyAdapter(MainActivity.this, list1);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});


    }


}

