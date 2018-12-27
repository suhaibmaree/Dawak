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

import static com.example.user.dawak.R.id.lin;
import static com.example.user.dawak.R.id.mail;
import static com.example.user.dawak.R.id.pswrdd;
import static com.example.user.dawak.R.id.register;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginButton;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView signinText;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(register);
        editTextEmail = findViewById(mail);
        editTextPassword = findViewById(pswrdd);
        signinText = findViewById(lin);
        progressDialog = new ProgressDialog(this);
        loginButton.setOnClickListener(this);
        signinText.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null){
           //open profile activity
            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);
        }// end if

    }

    @Override
    public void onClick(View v) {

        if(v == loginButton){
            //will login the user
            loginUser();
        }
        else
        if (v == signinText){
            //will open sign up activity
            Intent intent = new Intent(this,RegisterActivity.class);
            startActivity(intent);


        }//end onClick method

    }

    private void loginUser(){
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
        progressDialog.setMessage("Logging in, Please waite.");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            //user is successfully logged in
                            //start mainActivity
                            Toast.makeText(LoginActivity.this," User logged in successfully",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,ProfileActivity.class);
                            startActivity(intent);

                        }//end if
                        else {
                            Toast.makeText(LoginActivity.this,"Could not Login. Please try again",Toast.LENGTH_SHORT).show();


                        }//end else


                    }
                });


    }//end login user method
}
