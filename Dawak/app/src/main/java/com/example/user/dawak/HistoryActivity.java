package com.example.user.dawak;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private DatabaseReference mDatabase;
    private List<Pill> mPills ;
    private Adapter adapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(HistoryActivity.this);
        mPills = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("pills");
        mDatabase.keepSynced(true);
        progressDialog.setMessage("Fetching Data");

        getData();
        //Recycler
        mRecyclerView = findViewById(R.id.pills_recycler1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =new Adapter(mPills , HistoryActivity.this);
        mRecyclerView.setAdapter(adapter);
    }//end onCreate

    public void getData() {

        progressDialog.show();
        mDatabase.child(FirebaseAuth.getInstance().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                        while (iterator.hasNext()) {
                            DataSnapshot ds = iterator.next();
                            Pill pill = ds.getValue(Pill.class);
                            mPills.add(pill);
                        }
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }//end getData
}//end main class
