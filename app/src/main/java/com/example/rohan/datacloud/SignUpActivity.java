package com.example.rohan.datacloud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class SignUpActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button buttonRegister;
    private TextView textView;
    private EditText editTextEmail,editTextPassword;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null)
        {

            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        progressDialog=new ProgressDialog(this);

        buttonRegister =(Button) findViewById(R.id.button_reg);
        editTextEmail=(EditText) findViewById(R.id.mailid);
        editTextPassword=(EditText) findViewById(R.id.pass);
        textView=(TextView) findViewById(R.id.signin);
        buttonRegister.setOnClickListener(this);
        textView.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {

        if(v==buttonRegister)
        {
            registerUser();
        }

        if(v==textView)
        {

            startActivity(new Intent(this,LoginActivity.class));

        }



    }

    private void registerUser() {

        final String mail=editTextEmail.getText().toString().trim();
        String pwd=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(mail))
        {
            Toast.makeText(this,"please enter a mail id",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pwd))
        {
            Toast.makeText(this,"please enter a password",Toast.LENGTH_SHORT).show();
            return;
        }




        progressDialog.setMessage("signing up...");

        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(mail,pwd).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())

                {  startActivity(new Intent(getApplicationContext(),LoginActivity.class));


                    finish();

                }
                else
                {
                    Toast.makeText(SignUpActivity.this,"couldnt successful",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
