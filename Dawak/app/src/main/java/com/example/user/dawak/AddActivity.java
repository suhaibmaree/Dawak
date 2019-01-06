package com.example.user.dawak;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mImageView;
    private EditText mPillName, mTakenDay;
    private TimePicker mTimePicker;
    private Button plusButton, minusButton ,addPillsButton;
    private TextView takenNumberText;
    private int takenNumber = 1;
    private Pill pill;
    private DatabaseReference mDatabase;
    private FirebaseAuth mFirebaseAuth;
    private Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mImageView = findViewById(R.id.photo);
        mPillName = findViewById(R.id.pill_name);
        mTakenDay = findViewById(R.id.taken_day);
        mTimePicker = findViewById(R.id.timePicker);
        plusButton = findViewById(R.id.plus_button);
        minusButton = findViewById(R.id.minus_button);
        addPillsButton = findViewById(R.id.add_pill_button);
        takenNumberText = findViewById(R.id.taken_number_text);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("pills");
        mDatabase.keepSynced(true);
        mFirebaseAuth = FirebaseAuth.getInstance();

        plusButton.setOnClickListener(this);
        minusButton.setOnClickListener(this);
        addPillsButton.setOnClickListener(this);
        takenNumberText.setText(takenNumber+"");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Glide.with(this)
                .load(R.drawable.pill)
                .into(mImageView);


    }//end onCreat


    @Override
    public void onClick(View v) {
        if (v == plusButton){
            takenNumber++;
            takenNumberText.setText(takenNumber+"");

        }
        if (v == minusButton){
            if (takenNumber > 1){
                takenNumber--;
                takenNumberText.setText(takenNumber+"");
            }

        }
        if (v == addPillsButton){

             String pillName;
             String timeOfTaken;
             long currentTime;
             int numberOfTaken;
             String takenDay;

             pill = new Pill();

            if (TextUtils.isEmpty(mPillName.getText())){
                // pill name is empty
                Toast.makeText(this,getString(R.string.enter_med),Toast.LENGTH_SHORT).show();

            }
            else
                if (TextUtils.isEmpty(mTakenDay.getText())){
                    // taken day is empty
                    Toast.makeText(this,getString(R.string.enter_med_taken),Toast.LENGTH_SHORT).show();
                }
                else{
                    String AM_PM ;
                    if(mTimePicker.getHour() < 12) {
                        AM_PM = "AM";
                    } else {
                        AM_PM = "PM";
                    }

                        pillName = mPillName.getText().toString();
                        timeOfTaken = mTimePicker.getHour()+":"+mTimePicker.getMinute()+" "+AM_PM;
                        currentTime = System.currentTimeMillis();
                        numberOfTaken = Integer.parseInt(takenNumberText.getText().toString());
                        takenDay = mTakenDay.getText().toString();

                        pill.setPillName(pillName);
                        pill.setTimeOfTaken(timeOfTaken);
                        pill.setCurrentTime(currentTime);
                        pill.setNumberOfTaken(numberOfTaken);
                        pill.setTakenDay(takenDay);

                        mDatabase.child(mFirebaseAuth.getUid()).push().setValue(pill)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("tag", "onComplete: "+task.isSuccessful());
                                        if (task.isSuccessful()){

                                            Toast.makeText(AddActivity.this,getString(R.string.pill_added_suc),Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(AddActivity.this,ProfileActivity.class);
                                            startActivity(intent);
                                        }
                                        else {
                                            Toast.makeText(AddActivity.this,getString(R.string.pill_add_fail),Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });
                }//end if
        }//end else
    }//end onClick method
}//end class
