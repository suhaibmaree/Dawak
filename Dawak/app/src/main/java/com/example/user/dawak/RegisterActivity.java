package com.example.user.dawak;

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

import static com.example.user.dawak.R.id.*;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView loginText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

         buttonRegister = findViewById(register);
         editTextEmail = findViewById(mail);
         editTextPassword = findViewById(pswrdd);
         loginText = findViewById(lin);
         progressDialog = new ProgressDialog(this);
         buttonRegister.setOnClickListener(this);
         loginText.setOnClickListener(this);
         firebaseAuth =FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
            //open profile activity
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        }//end if

    }

    @Override
    public void onClick(View v) {

        if(v == buttonRegister){
            //will register the user
            registerUser();
        }
        else
            if (v == loginText){
            //will open login activity
                finish();
                Intent intent = new Intent(this,LoginActivity.class);
                startActivity(intent);

            }

    }//end onClick method


    private  void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this,"Please enter your Email",Toast.LENGTH_SHORT).show();
            return;

        }
        if (TextUtils.isEmpty(password)){
            // password is empty
            Toast.makeText(this,"Please enter your Password",Toast.LENGTH_SHORT).show();
            return;

        }

        //if validate
        progressDialog.setMessage("Registering User, please wait.");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //user is successfully registered ang logged in
                            //start mainActivity
                            Toast.makeText(RegisterActivity.this,"Registered successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            startActivity(intent);

                        }//end if
                        else {
                            Toast.makeText(RegisterActivity.this,"Could not register. Please try again",Toast.LENGTH_SHORT).show();


                        }//end else

                    }
                });

    }//end registerUser method
}
