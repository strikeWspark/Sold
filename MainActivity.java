package com.dryfire.sold.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import com.dryfire.sold.Adapter.SoldRecyclerView;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Sold> soldList;
    private DatabaseReference mDatabasereference;
    private FirebaseDatabase mDatabase;
    private SoldRecyclerView soldRecyclerView;
    private ProgressDialog mprogressDialog;

    private static final String ACTION_UPDATE_NOTIFICATION = "com.dryfire.sold.Activities";
    private static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    private static final int NOTIFICATION_ID = 0;
    private NotificationManager notificationManager;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mprogressDialog=  new ProgressDialog(this);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        createNotification();
        mAuth = FirebaseAuth.getInstance();



        mDatabase = FirebaseDatabase.getInstance();
        mDatabasereference = mDatabase.getReference("MSold");
        mDatabasereference.keepSynced(true);
        soldList = new ArrayList<>();
        mprogressDialog.setMessage("Loading Content");
        mprogressDialog.show();
        mprogressDialog.setCancelable(false);



        recyclerView = (RecyclerView) findViewById(R.id.sold_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadBidContents();
        FloatingActionButton fab = findViewById(R.id.fab);

        //new MyLoaderTask().execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action",null).show();

                startActivity(new Intent(MainActivity.this,AddProductActivity.class));
            }
        });
        //fab.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                        .setAction("Action", new View.OnClickListener() {
        //                            @Override
        //                            public void onClick(View view) {
        //
        //                            }
        //                        }).show();
        //
        //                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
        //            }
        //        });


    }

    private void loadBidContents() {
        mDatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Sold sold = dataSnapshot.getValue(Sold.class);

                soldList.add(sold);
                Collections.reverse(soldList);
                soldRecyclerView = new SoldRecyclerView(MainActivity.this,soldList);
                recyclerView.setAdapter(soldRecyclerView);
                mprogressDialog.dismiss();
                soldRecyclerView.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notificationIntent = new Intent(this,MainActivity.class);
        PendingIntent notificationpendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,PRIMARY_CHANNEL_ID)
                        .setContentTitle("You've been notified")
                        .setContentText("This is your notification text")
                        .setSmallIcon(R.drawable.sold_icon)
                        .setAutoCancel(true).setContentIntent(notificationpendingIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(NotificationCompat.DEFAULT_ALL);
                return notifyBuilder;
    }

    private void createNotification() {

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Notification",NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Description");
            notificationManager.createNotificationChannel(notificationChannel);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sold_profile:
                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                intent.putExtra("ID",mAuth.getCurrentUser().getUid());
                startActivity(intent);
                break;
            case R.id.sold_signout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
        }
        return true;
    }
/*
    @Override
    protected void onStart() {
        super.onStart();
        mDatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                Sold sold = dataSnapshot.getValue(Sold.class);
                soldList.add(sold);

                soldRecyclerView = new SoldRecyclerView(MainActivity.this,soldList);
                recyclerView.setAdapter(soldRecyclerView);
                mprogressDialog.dismiss();
                soldRecyclerView.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/
/*
    private class MyLoaderTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mDatabasereference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                    Sold sold = dataSnapshot.getValue(Sold.class);
                    soldList.add(sold);

                    soldRecyclerView = new SoldRecyclerView(MainActivity.this,soldList);

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            recyclerView.setAdapter(soldRecyclerView);
            soldRecyclerView.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/
}

