package com.dryfire.sold.Activities;

import android.os.Bundle;

import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dryfire.sold.Adapter.BidListAdapter;
import com.dryfire.sold.Modal.Bid;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {

    private Sold sold;
    private Toolbar toolbar;
    private ListView sold_listview;
    private BidListAdapter bidListAdapter;
    private List<Bid> bidList;
    private TextView sold_profile_name;
    private TextView sold_profile_upi;
    private TextView sold_profile_mobile;
    private TextView sold_profile_location;
    private TextView sold_profile_username;
    private ImageView profile_picture;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase,listDatabase;
    private DatabaseReference mDatabaseReference,listDatabasereference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_profile_activity);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        toolbar = findViewById(R.id.sold_profile_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance();
        listDatabase = FirebaseDatabase.getInstance();

        String Id = getIntent().getStringExtra("ID");
        mDatabaseReference = mDatabase.getReference("MUsers").child(Id);

        listDatabasereference = listDatabase.getReference("MBids").child(Id);
        listDatabasereference.keepSynced(true);
        bidList = new ArrayList<>();
        Toast.makeText(this,""+mDatabaseReference,Toast.LENGTH_LONG).show();
        sold_profile_name = findViewById(R.id.sold_name_view);
        sold_profile_upi = findViewById(R.id.sold_upiId_view);
        sold_profile_mobile = findViewById(R.id.sold_mobile_no_view);
        sold_profile_location = findViewById(R.id.sold_location_view);
        sold_profile_username = findViewById(R.id.sold_username_view);
        sold_listview = findViewById(R.id.sold_profile_listView);

        getProfileDetails();

        loadListOfBids();


    }

    private void loadListOfBids() {
        listDatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Bid bid = dataSnapshot.getValue(Bid.class);
                Log.d(String.valueOf(bid), "onChildAdded: ");
                bidList.add(bid);
                Collections.reverse(bidList);
                bidListAdapter = new BidListAdapter(ProfileActivity.this,bidList);
                sold_listview.setAdapter(bidListAdapter);
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

    private void getProfileDetails() {

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sold = dataSnapshot.getValue(Sold.class);
                sold_profile_name.setText(sold.getName());
                sold_profile_upi.setText(sold.getUpiId());
                sold_profile_mobile.setText(sold.getMobile_no());
                sold_profile_location.setText(sold.getLocation());
                sold_profile_username.setText(mUser.getEmail());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


 /*   @Override
    protected void onStart() {
        super.onStart();
        listDatabasereference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Bid bid = dataSnapshot.getValue(Bid.class);
                Log.d(String.valueOf(bid), "onChildAdded: ");
                bidList.add(bid);

                bidListAdapter = new BidListAdapter(ProfileActivity.this,bidList);
                sold_listview.setAdapter(bidListAdapter);

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
}
