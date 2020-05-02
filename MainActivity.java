package com.dryfire.sold.Activities;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.dryfire.sold.Adapter.SoldRecyclerView;
import com.dryfire.sold.Developer.DeveloperActivity;
import com.dryfire.sold.Developer.SupportDevelopment;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.dryfire.sold.UI.NavigationIconClickListener;
import com.google.android.material.button.MaterialButton;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Sold> soldList;
    private DatabaseReference motorreference,electronicsreference;
    private DatabaseReference home_appreference,furniturereference,groceriesreference;
    private FirebaseDatabase mDatabase;
    private SoldRecyclerView soldRecyclerView;
    private ProgressDialog mprogressDialog;
    private MaterialButton signoutButton;
    private MaterialButton profileButton;
    private MaterialButton supportButton;
    private MaterialButton developerButton;
    private AlertDialog.Builder filterbuilder;
    private AlertDialog filterdialog;
    private TextView electronicsbutton,furniturebutton,groceriesbutton;
    private TextView homeappliancebutton,motor_vehiclebutton;
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

        toolbar.setNavigationOnClickListener(new NavigationIconClickListener(this,
                findViewById(R.id.sold_grid),
                new AccelerateDecelerateInterpolator(),
                this.getResources().getDrawable(R.drawable.sold_menu_icon),
                this.getResources().getDrawable(R.drawable.ic_close_black_24dp)));


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            findViewById(R.id.sold_grid).setBackground(this.getDrawable(R.drawable.toolbar_shape));
        }

                mprogressDialog = new ProgressDialog(this);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        createNotification();
        mAuth = FirebaseAuth.getInstance();


        filterbuilder = new AlertDialog.Builder(this);
        mDatabase = FirebaseDatabase.getInstance();
        electronicsreference = mDatabase.getReference("MSold").child("Electronics");
        electronicsreference.keepSynced(true);

        motorreference = FirebaseDatabase.getInstance().getReference("MSold").child("Motor Vehicle");
        motorreference.keepSynced(true);

        furniturereference = FirebaseDatabase.getInstance().getReference("MSold").child("Furniture");
        furniturereference.keepSynced(true);

        home_appreference = FirebaseDatabase.getInstance().getReference("MSold").child("Home Appliance");
        home_appreference.keepSynced(true);

        groceriesreference = FirebaseDatabase.getInstance().getReference("MSold").child("Groceries");
        groceriesreference.keepSynced(true);

        soldList = new ArrayList<>();
        mprogressDialog.setMessage("Loading Content");
        mprogressDialog.show();
//        mprogressDialog.setCancelable(false);



        recyclerView = (RecyclerView) findViewById(R.id.sold_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));




        loadBidContents(electronicsreference);
        loadBidContents(motorreference);
        loadBidContents(furniturereference);
        loadBidContents(home_appreference);
        loadBidContents(groceriesreference);

        FloatingActionButton fab = findViewById(R.id.fab);
        signoutButton = findViewById(R.id.sold_signout);
        profileButton = findViewById(R.id.sold_profile);
        supportButton = findViewById(R.id.support_developement);
        developerButton = findViewById(R.id.sold_Developer);

        signoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        });


        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this,ProfileActivity.class);
                intent.putExtra("ID",mAuth.getCurrentUser().getUid());
                startActivity(intent);

            }
        });

        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SupportDevelopment.class));
            }
        });

        developerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DeveloperActivity.class));
            }
        });
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

    private void loadBidContents(DatabaseReference a) {
        a.addChildEventListener(new ChildEventListener() {
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
                break;
            case R.id.filter:
                View v = getLayoutInflater().inflate(R.layout.sold_filter_dialog,null);
                electronicsbutton = v.findViewById(R.id.sold_electrons);
                furniturebutton = v.findViewById(R.id.sold_furniture);
                groceriesbutton = v.findViewById(R.id.sold_groceries);
                homeappliancebutton = v.findViewById(R.id.sold_homeapp);
                motor_vehiclebutton = v.findViewById(R.id.sold_motor);
                filterbuilder.setView(v);
                filterdialog = filterbuilder.create();
                filterdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                filterdialog.show();

                electronicsbutton.setOnClickListener(this);
                furniturebutton.setOnClickListener(this);
                groceriesbutton.setOnClickListener(this);
                homeappliancebutton.setOnClickListener(this);
                motor_vehiclebutton.setOnClickListener(this);

               // soldList.clear();
                //soldRecyclerView.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sold_electrons:
                soldList.clear();
                soldRecyclerView.notifyDataSetChanged();
                loadBidContents(electronicsreference);
                filterdialog.dismiss();
                break;
            case R.id.sold_furniture:
                soldList.clear();
                soldRecyclerView.notifyDataSetChanged();
                loadBidContents(furniturereference);
                filterdialog.dismiss();
                break;
            case R.id.sold_groceries:
                soldList.clear();
                soldRecyclerView.notifyDataSetChanged();
                loadBidContents(groceriesreference);
                filterdialog.dismiss();
                break;
            case R.id.sold_homeapp:
                soldList.clear();
                soldRecyclerView.notifyDataSetChanged();
                loadBidContents(home_appreference);
                filterdialog.dismiss();
                break;
            case R.id.sold_motor:
                soldList.clear();
                soldRecyclerView.notifyDataSetChanged();
                loadBidContents(motorreference);
                filterdialog.dismiss();
                break;
        }
    }
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


