package com.example.user.dawak;

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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button button;
    private FirebaseAuth firebaseAuth;

    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<Pill> mPills ;
    private Adapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        button = findViewById(R.id.logout);
        firebaseAuth = FirebaseAuth.getInstance();

        mPills = new ArrayList<>();
        if (firebaseAuth.getCurrentUser() ==null){
            finish();
            Intent intent= new Intent(this,LoginActivity.class);
            startActivity(intent);
        }//end if

        button.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pills");
        mDatabase.keepSynced(true);
        getData();
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
                Parcelable parcelable = (Parcelable) mPills;
                intent.putExtra("pills",parcelable );
                startActivity(intent);
            }
        });

////////////////////////////////////////////////////////////////////////////
//        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Pill pill = new Pill();
////                pill.setCurrentTime(System.currentTimeMillis());
////                pill.setNumberOfTaken(2);
////                pill.setTimeOfTaken(new Date().toString());
////                pill.setPillName("p1");
////
////                mDatabase.child(FirebaseAuth.getInstance().getUid())
////                        .push()
////                        .setValue(pill)
////                .addOnCompleteListener(new OnCompleteListener<Void>() {
////                    @Override
////                    public void onComplete(@NonNull Task<Void> task) {
////                        Log.d("tag", "onComplete: "+task.isSuccessful());
////                    }
////                });
//            }
//        });
/////////////////////////////////////////////////////////////////////////////

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
        if (v == button){
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this,LoginActivity.class));

        }//end if

    }//end onClick

}//end class
