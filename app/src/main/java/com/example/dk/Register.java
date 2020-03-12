package com.example.dk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;
    private DatabaseReference myRef;
    EditText firstname, lastname, username;
    EditText dob, empid, desig, org;
    TextView reg;
    String email_id, password, cpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstname = findViewById(R.id.fname);
        lastname = findViewById(R.id.lname);
        username = findViewById(R.id.uname);
        password = findViewById(R.id.password).toString().trim();
        cpass= findViewById(R.id.cpassword).toString().trim();
        dob = findViewById(R.id.dob);
        email_id = findViewById(R.id.email).toString().trim();
        desig = findViewById(R.id.designation);
        org = findViewById(R.id.organization);
        reg = findViewById(R.id.register);
        empid = findViewById(R.id.empid);

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        //Get instance firebase Realtime database.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");

       reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Register.this,"Registerd", Toast.LENGTH_SHORT).show();
                register();
            }
        });
    }
    private void register()
    {

        if(!TextUtils.isEmpty(email_id) && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("Signing up...");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(email_id, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            task.getException();
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Register.this, "Authentication Success.",
                                        Toast.LENGTH_SHORT).show();
                                //FirebaseUser user = mAuth.getCurrentUser();
                                //updateUI(user);
                                String uid = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = myRef.child(uid);
                                current_user_db.child("Name").setValue(firstname);
                                mProgress.dismiss();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Authentication failed?.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }
                    });
        }

    }
    //Change UI according to user data.
    public void  updateUI(FirebaseUser account){
        if(account != null){
            startActivity(new Intent(this,MainActivity.class));
        }else {
            Toast.makeText(this,"U Didnt signed in",Toast.LENGTH_LONG).show();
        }
    }
}

