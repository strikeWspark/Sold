package com.dryfire.sold.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FullDescriptionActivity extends AppCompatActivity {

    private Sold sold;
    private TextView full_itemname,full_description,full_bidprice;
    private TextView full_username;
    private MaterialButton full_bid_button;
    private ImageView full_item_image;
    private Toolbar full_toolbar;
    private FirebaseUser mUser;
    private AlertDialog.Builder bidnowBuilder;
    private AlertDialog bidnowDialog;
    private TextInputLayout bidnowInput;
    private TextInputEditText bidnowEdit;
    private MaterialButton bidnowButton;
    private ProgressDialog mprogress;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sold_full_description);


        mprogress = new ProgressDialog(this);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        sold = (Sold) getIntent().getSerializableExtra("sold");

        full_toolbar = findViewById(R.id.sold_full_toolbar);
        setSupportActionBar(full_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        full_itemname = findViewById(R.id.sold_full_itemname);
        full_description = findViewById(R.id.sold_full_description);
        full_bid_button = findViewById(R.id.sold_full_bidButton);
        full_bidprice = findViewById(R.id.sold_full_bidprice);
        full_username = findViewById(R.id.sold_full_username);
        full_item_image = findViewById(R.id.sold_full_image);

        Picasso.with(this).load(sold.getItem_image()).into(full_item_image);
        Log.d("" + sold.getItem_image(), "onCreate: ");


        full_itemname.setText(sold.getItem_name());
        full_description.setText("\t\tProduct Description:\n\t\t\t\t\t" + sold.getDescription());
        full_bidprice.setText("$ " +sold.getMin_bid_price());
        full_username.setText("Uploaded By:- " + sold.getUsername());


        full_bid_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(FullDescriptionActivity.this);
                View v = getLayoutInflater().inflate(R.layout.sold_bid_now,null);


                bidnowInput = v.findViewById(R.id.sold_bid_now_bidInput);
                bidnowEdit = v.findViewById(R.id.sold_bid_now_bidEdit);
                bidnowButton = v.findViewById(R.id.sold_bid_now_button);
                bottomSheetDialog.setContentView(v);
                bottomSheetDialog.show();
                final TextView[] dialogitem_name = new TextView[1];
                final TextView[] bid_amount = new TextView[1];
                final MaterialButton[] cancel_button = new MaterialButton[1];
                final MaterialButton[] confirm_button = new MaterialButton[1];
                final CheckBox[] check_dialog = new CheckBox[1];
                bidnowButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        bidnowBuilder = new  AlertDialog.Builder(FullDescriptionActivity.this);
                        final View alert_view = LayoutInflater.from(FullDescriptionActivity.this)
                                .inflate(R.layout.sold_confirm_bid_dialog,null);
                        dialogitem_name[0] = alert_view.findViewById(R.id.sold_dialog_itemname);
                        bid_amount[0] = alert_view.findViewById(R.id.sold_dialog_bidAmount);
                        cancel_button[0] = alert_view.findViewById(R.id.sold_bid_cancel);
                        confirm_button[0] = alert_view.findViewById(R.id.sold_confirm_bid);
                        check_dialog[0] = alert_view.findViewById(R.id.sold_dialog_check);
                        bidnowBuilder.setView(alert_view);
                        bidnowDialog = bidnowBuilder.create();
                        bidnowDialog.show();
                        confirm_button[0].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View view) {
                                if(check_dialog[0].isChecked()){
                                    bidnowDialog.dismiss();
                                    mprogress.setMessage("Placing your bid...");
                                    mprogress.show();
                                    mprogress.setCancelable(false);
                                    String s = bidnowEdit.getText().toString().trim();
                                    Map<String,String> dataToSave= new HashMap<>();
                                    dataToSave.put("bid_price",s);
                                    dataToSave.put("username",mUser.getEmail());
                                    dataToSave.put("userId",mUser.getUid());
                                    //newItem.child("bid_price").setValue(s);
                                    //newItem.child("username").setValue(mUser.getEmail());
                                    //newItem.child("userId").setValue(mUser.getUid());

                                    mDatabaseReference.child(String.valueOf(java.lang.System.currentTimeMillis()))
                                            .setValue(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mprogress.dismiss();
                                            Snackbar.make(view, "Your Bid has been placed", Snackbar.LENGTH_LONG)
                                                    .show();
                                            startActivity(new Intent(FullDescriptionActivity.this,MainActivity.class));
                                            finish();
                                        }
                                    });

                                }else{
                                    Toast.makeText(FullDescriptionActivity.this, "Please check the condition", Toast.LENGTH_SHORT).show();
                                }

                            }

                        });




                        bottomSheetDialog.dismiss();
                    }
                });
            }
        });
    }
}
