package com.example.user.dawak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener
        ,LoaderManager.LoaderCallbacks<List<Pill>> {


    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private List<Pill> mPills = null ;
    private Adapter adapter;
    private String medicines = "Your medicines is ";
    private int flag =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FloatingActionButton fab = findViewById(R.id.fab);
        firebaseAuth = FirebaseAuth.getInstance();
        mPills = new ArrayList<>();

        if (firebaseAuth.getCurrentUser() ==null){
            finish();
            Intent intent= new Intent(this,LoginActivity.class);
            startActivity(intent);
        }//end if

        getSupportLoaderManager().initLoader(R.id.pills_loader_id,null,this);

        if (mPills == null){
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
                startActivity(intent);
            }
        });

    }//end onCreat



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
            startActivity(intent);
        }
        if (item.getItemId() == R.id.logout) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        return true;
    }

    @NonNull
    @Override
    public Loader<List<Pill>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<List<Pill>>(this) {
            final ProgressBar progressBar = findViewById(R.id.progress_bar);
            @Nullable
            @Override
            public List<Pill> loadInBackground() {final List<Pill> pills= new ArrayList<>();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("pills");
                progressBar.setVisibility(View.VISIBLE);

                mDatabase.child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
                                while (iterator.hasNext()){
                                    DataSnapshot ds = iterator.next();
                                    Pill pill = ds.getValue(Pill.class);

                                    //Check if the medicine date valid or ended to display
                                    Long num = new Long(pill.getNumberOfTaken());
                                    Long last = System.currentTimeMillis()/(60*60*24*1000);
                                    Long now = pill.getCurrentTime()/(60*60*24*1000);
                                    if (now - last < num) {
                                        mPills.add(pill);
                                        //Set String for widget
                                        medicines += "\n"+pill.getPillName()+" At "+pill.getTimeOfTaken()
                                                +" On "+pill.getTakenDay();
                                    }//end if
                                }

                                adapter.notifyDataSetChanged();
                                WidgetService.startActionDisplay(ProfileActivity.this,medicines);
                                progressBar.setVisibility(View.INVISIBLE);
                                flag=1;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                return pills;
            }

            @Override
            protected void onStartLoading() {
                if (flag==0) {
                    forceLoad();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Pill>> loader, List<Pill> pills) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Pill>> loader) {

    }
}//end class
