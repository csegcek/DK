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
    EditText firstname, lastname, username, email;
    EditText dateofbirth, empid, desig, org, pass, cpass;
    TextView reg;
    String email_id, password, cpassword;
    String fname, lname, uname, designation, organization, employeeid, dob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getViews(); //user defined

        mAuth = FirebaseAuth.getInstance();
        mProgress = new ProgressDialog(this);
        //Get instance firebase Realtime database.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference().child("users");

       reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extractText();//User defined
                register(); //User defined
            }
        });
    }
    private void register()
    {

        if(check() && validate(password,cpassword)) {

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

                                FirebaseUser user = mAuth.getCurrentUser();
                                String uid = mAuth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = myRef.child(uid);
                                current_user_db.child("firstname").setValue(fname);
                                current_user_db.child("lastname").setValue(lname);
                                current_user_db.child("username").setValue(uname);
                                current_user_db.child("empid").setValue(employeeid);
                                current_user_db.child("dob").setValue(dob);
                                current_user_db.child("designation").setValue(designation);
                                current_user_db.child("organization").setValue(organization);

                                mProgress.dismiss();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Register.this, "Authentication failed?.",
                                        Toast.LENGTH_SHORT).show();
                                mProgress.dismiss();
                            }
                        }
                    });
        }
    }
    //Change UI according to user data.
    public void  updateUI(FirebaseUser account){
        if(account != null) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
    private void getViews()
    {
        firstname = findViewById(R.id.fname);
        lastname = findViewById(R.id.lname);
        username = findViewById(R.id.uname);
        pass = findViewById(R.id.password);
        cpass = findViewById(R.id.cpassword);
        dateofbirth = findViewById(R.id.dob);
        email = findViewById(R.id.email);
        desig = findViewById(R.id.designation);
        org = findViewById(R.id.organization);
        reg = findViewById(R.id.register);
        empid = findViewById(R.id.empid);
    }
    private void extractText()
    {
        email_id = email.getText().toString().trim();
        password = pass.getText().toString().trim();
        cpassword = cpass.getText().toString().trim();
        fname = firstname.getText().toString().trim();
        lname = lastname.getText().toString().trim();
        uname = username.getText().toString().trim();
        designation = desig.getText().toString().trim();
        organization = org.getText().toString().trim();
        employeeid = empid.getText().toString().trim();
        dob = dateofbirth.getText().toString().trim();
    }
    private boolean check()
    {
        EditText fields[] = {firstname,lastname,username,empid,email,pass,cpass,dateofbirth,desig,org};

        for(int i = 0; i < fields.length; i++)
        {
            if(TextUtils.isEmpty(fields[i].getText()))
            {
                fields[i].setError("Required Field");
                return false;
            }
        }
        return true;
    }
    private boolean validate(String actual_pass, String confirm_pass)
    {
        if(!actual_pass.equals(confirm_pass))
        {
            cpass.setError("Not matching");
            return false;
        }
        return true;
    }
}

