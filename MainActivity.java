package com.dryfire.sold.Activities;

import android.content.Intent;
import android.os.Bundle;

import com.dryfire.sold.Adapter.SoldRecyclerView;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Sold> soldList;
    private DatabaseReference mDatabasereference;
    private FirebaseDatabase mDatabase;
    private SoldRecyclerView soldRecyclerView;
    private FirebaseUser mUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        mDatabase = FirebaseDatabase.getInstance();
        mDatabasereference = mDatabase.getReference("MSold");
        mDatabasereference.keepSynced(true);
        soldList = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.sold_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fab);
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
}

