package com.example.finalprojectdu.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.finalprojectdu.R;
import com.example.finalprojectdu.adapter.ReportAdapter;
import com.example.finalprojectdu.model.Report;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DailyUploadActivity extends AppCompatActivity {

    //Report RecyclerView /
    private RecyclerView reportRecyclerView;
    private List<Report> reportList;
    private ReportAdapter reportAdapter;

    //Initialize Variables
    private ProgressBar progressBar;

    //database
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reportRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_upload);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize Variables
        progressBar = findViewById(R.id.progressBar_cat);

        //initializing firebase
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance("https://finalproject-d876c-default-rtdb.asia-southeast1.firebasedatabase.app/");
        reportRef = firebaseDatabase.getReference("Reports");

        //report RecyclerView
        reportRecyclerView = findViewById(R.id.report_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        reportRecyclerView.setLayoutManager(layoutManager);
        reportList = new ArrayList<>();
        //getting report from firebase
        reportRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot npsnapshot : snapshot.getChildren()){
                        Report l = npsnapshot.getValue(Report.class);
                        reportList.add(l);

                        //Progress Bar
                        if (progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                    reportAdapter = new ReportAdapter(reportList, getApplicationContext());
                    reportRecyclerView.setAdapter(reportAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    //For Back Button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}