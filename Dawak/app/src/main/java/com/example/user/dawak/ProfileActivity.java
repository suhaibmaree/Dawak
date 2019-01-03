package com.example.user.dawak;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<Pill> mPills ;
    private Adapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FloatingActionButton fab = findViewById(R.id.fab);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(ProfileActivity.this);

        mPills = new ArrayList<>();
        if (firebaseAuth.getCurrentUser() ==null){
            finish();
            Intent intent= new Intent(this,LoginActivity.class);
            startActivity(intent);
        }//end if

        mDatabase = FirebaseDatabase.getInstance().getReference().child("pills");
        mDatabase.keepSynced(true);

        progressDialog.show();
        getData();
        if (mPills != null){
            progressDialog.dismiss();
        }
        else {
            progressDialog.dismiss();
            Toast.makeText(ProfileActivity.this, "Fetching Data Failed",Toast.LENGTH_SHORT).show();
        }

        //Recycler
        mRecyclerView = findViewById(R.id.pills_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =new Adapter(mPills , ProfileActivity.this);
        mRecyclerView.setAdapter(adapter);

        //Floating button listener
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ProfileActivity.this,AddActivity.class);
                //intent.putExtra("pills", (Serializable) mPills);
                startActivity(intent);
            }
        });

    }//end onCreat

    public void getData(){

        mDatabase.child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()){
                            DataSnapshot ds = iterator.next();
                            Pill pill = ds.getValue(Pill.class);
                            mPills.add(pill);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }//end getData method

    @Override
    public void onClick(View v) {

    }//end onClick

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }// end onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.history){
            Intent intent = new Intent(ProfileActivity.this,HistoryActivity.class);
            intent.putExtra("pills", (Serializable) mPills);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }
}//end class
