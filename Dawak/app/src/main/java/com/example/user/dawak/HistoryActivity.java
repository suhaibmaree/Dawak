package com.example.user.dawak;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class HistoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Pill>> {

    private FirebaseAuth firebaseAuth;
    private RecyclerView mRecyclerView;
    private List<Pill> mPills ;
    private Adapter adapter;
    private int flag =0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        firebaseAuth = FirebaseAuth.getInstance();
        mPills = new ArrayList<>();

        getSupportLoaderManager().initLoader(R.id.pills_loader_id,null,this);

        if (mPills == null){
            Toast.makeText(HistoryActivity.this, "Fetching Data Failed",Toast.LENGTH_SHORT).show();
        }

        //Recycler
        mRecyclerView = findViewById(R.id.pills_recycler1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter =new Adapter(mPills , HistoryActivity.this);
        mRecyclerView.setAdapter(adapter);

    }//end onCreate



    @NonNull
    @Override
    public Loader<List<Pill>> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<List<Pill>>(this) {
            @Nullable
            @Override
            public List<Pill> loadInBackground() {final List<Pill> pills= new ArrayList<>();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("pills");
                final ProgressBar progressBar = findViewById(R.id.progress_bar2);
                progressBar.setVisibility(View.VISIBLE);

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
                if (flag == 0) {
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
}//end main class
