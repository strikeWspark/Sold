package com.dryfire.sold.Adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dryfire.sold.Activities.FullDescriptionActivity;
import com.dryfire.sold.Activities.MainActivity;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;

import com.dryfire.sold.UI.SoldAnimation;
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
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

public class SoldRecyclerView extends RecyclerView.Adapter<SoldRecyclerView.ViewHolder> {

    private Context context;
    private List<Sold> soldList;
    String card_itemname;
    String imageUrl = null;
    private int prevPosition =0;

    public SoldRecyclerView(MainActivity context, List<Sold> soldList){
        this.context = context;
        this.soldList = soldList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sold_row,parent,false);
        return new  ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Sold sold = soldList.get(position);
        String post_description = null;
        String post_price = null;

        if(sold.getDescription().length() > 20){
            post_description = sold.getDescription().substring(0,20) + "...";
        }


        if(sold.getMin_bid_price().length() >= 7 && sold.getMin_bid_price().length() <= 8){
            post_price = sold.getMin_bid_price().substring(0,1) + "B";
        }else if(sold.getMin_bid_price().length() >= 5 && sold.getMin_bid_price().length() <= 6){
            post_price = sold.getMin_bid_price().substring(0,1) + "M";
        }else if(sold.getMin_bid_price().length() >= 3 && sold.getMin_bid_price().length() <= 4){
            post_price = sold.getMin_bid_price().substring(0,1) + "K";
        }
        card_itemname = sold.getItem_name();
        holder.itemname.setText(card_itemname);
        holder.itemdesc.setText(post_description);
        holder.itemminbid.setText("$ " + post_price);
        imageUrl = sold.getItem_image();



        if(imageUrl != null){
            Picasso.with(context).load(imageUrl).into(holder.itemimgae);
        }


        if(position>prevPosition){
            SoldAnimation.animate(holder,true);
        }else {
            SoldAnimation.animate(holder,false);
        }
        prevPosition = position;

    }

    @Override
    public int getItemCount() {
        return soldList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemname,itemdesc,itemminbid;
        public ImageView itemimgae;
        public MaterialButton bidbutton;
        private AlertDialog.Builder bidnowBuilder;
        private TextInputLayout bidnowInput;
        private TextInputEditText bidnowEdit;
        private MaterialButton bidnowButton;
        private AlertDialog bidnowDialog;
        private ProgressDialog mprogress;
        private FirebaseDatabase mDatabase_bid;
        private DatabaseReference mDatabaseReference_bid;
        String userId;
        View myView;
        String key_value;

        private FirebaseAuth mAuth;
        private FirebaseUser mUser;



        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            myView = itemView;
            context = ctx;


            mprogress = new ProgressDialog(context);
            mDatabase_bid = FirebaseDatabase.getInstance();
            mDatabaseReference_bid = mDatabase_bid.getReference().child("MBids");
            itemimgae = itemView.findViewById(R.id.sold_itemcardimage);
            itemname = itemView.findViewById(R.id.sold_carditemname);
            itemdesc = itemView.findViewById(R.id.sold_itemcardesc);
            itemminbid = itemView.findViewById(R.id.sold_minBidprice);
            bidbutton = itemView.findViewById(R.id.sold_bid_button);
            userId = null;
            key_value = null;



            mAuth = FirebaseAuth.getInstance();
            mUser = mAuth.getCurrentUser();
            bidbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  /*  bidnowBuilder = new AlertDialog.Builder(context);
                    View v = LayoutInflater.from(context).inflate(R.layout.sold_bid_now,null);
                    bidnowInput = v.findViewById(R.id.sold_bid_now_bidInput);
                    bidnowEdit = v.findViewById(R.id.sold_bid_now_bidEdit);
                    bidnowButton = v.findViewById(R.id.sold_bid_now_button);

                    bidnowBuilder.setView(v);
                    bidnowDialog = bidnowBuilder.create();
                    bidnowDialog.show();*/
                    final Sold sold = soldList.get(getAdapterPosition());
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                    final View v = LayoutInflater.from(context).inflate(R.layout.sold_bid_now,null);
                    bidnowInput = v.findViewById(R.id.sold_bid_now_bidInput);
                    bidnowEdit = v.findViewById(R.id.sold_bid_now_bidEdit);
                    bidnowButton = v.findViewById(R.id.sold_bid_now_button);
                    bottomSheetDialog.setContentView(v);
                    bottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                            Color.TRANSPARENT));
                    bottomSheetDialog.show();
                    final TextView[] dialogitem_name = new TextView[1];
                    final TextView[] bid_amount = new TextView[1];
                    final MaterialButton[] cancel_button = new MaterialButton[1];
                    final MaterialButton[] confirm_button = new MaterialButton[1];
                    final CheckBox[] check_dialog = new CheckBox[1];
                    bidnowButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bidnowBuilder = new  AlertDialog.Builder(context);
                            final View alert_view = LayoutInflater.from(context)
                                    .inflate(R.layout.sold_confirm_bid_dialog,null);
                            dialogitem_name[0] = alert_view.findViewById(R.id.sold_dialog_itemname);
                            bid_amount[0] = alert_view.findViewById(R.id.sold_dialog_bidAmount);
                            cancel_button[0] = alert_view.findViewById(R.id.sold_bid_cancel);
                            confirm_button[0] = alert_view.findViewById(R.id.sold_confirm_bid);
                            check_dialog[0] = alert_view.findViewById(R.id.sold_dialog_check);
                            bidnowBuilder.setView(alert_view);
                            final String s = bidnowEdit.getText().toString().trim();

                            //dialogitem_name[0].setText("Item Name: " + card_itemname);
                            dialogitem_name[0].setText("Item name:" + sold.getItem_name());
                            bid_amount[0].setText("Bid Amount: " + s);
                            bidnowDialog = bidnowBuilder.create();
                            bidnowDialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                                    Color.TRANSPARENT));
                            bidnowDialog.show();
                            cancel_button[0].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                        bidnowDialog.dismiss();
                                }
                            });


                            confirm_button[0].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(check_dialog[0].isChecked()){
                                        bidnowDialog.dismiss();
                                        mprogress.setMessage("Placing your bid...");
                                        mprogress.show();
                                        mprogress.setCancelable(false);

                                        DatabaseReference newItem = mDatabaseReference_bid.push();
                                        Map<String,String> dataToSave= new HashMap<>();
                                        dataToSave.put("bid_price",s);
                                        dataToSave.put("item_image",sold.getItem_image());
                                        dataToSave.put("username",mUser.getEmail());
                                        dataToSave.put("userId",mUser.getUid());
                                        dataToSave.put("itemname",sold.getItem_name());
                                        dataToSave.put("item_key",sold.getKey_node_value());
                                        //newItem.child("bid_price").setValue(s);
                                        //newItem.child("username").setValue(mUser.getEmail());
                                        //newItem.child("userId").setValue(mUser.getUid());

                                        mDatabaseReference_bid.child(mUser.getUid()).
                                                child(sold.getKey_node_value())
                                                .setValue(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                mprogress.dismiss();
                                                Snackbar.make(myView, "Your Bid has been placed", Snackbar.LENGTH_LONG)
                                                        .show();
                                            }
                                        });

                                    }else{
                                        Toast.makeText(context, "Please check the condition", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            });




                            bottomSheetDialog.dismiss();
                        }
                    });


                    
                    if(!(TextUtils.isEmpty(bidnowEdit.getText().toString().trim()))){
                        Toast.makeText(ctx, "", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Sold sold = soldList.get(getAdapterPosition());
                    Intent intent = new Intent(context, FullDescriptionActivity.class);
                    intent.putExtra("sold",sold);
                    ctx.startActivity(intent);
                }
            });



        }
    }
}
