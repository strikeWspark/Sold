package com.dryfire.sold.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dryfire.sold.Activities.FullDescriptionActivity;
import com.dryfire.sold.Activities.MainActivity;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.dryfire.sold.UI.BottomSheetFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class SoldRecyclerView extends RecyclerView.Adapter<SoldRecyclerView.ViewHolder> {

    private Context context;
    private List<Sold> soldList;


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
        String imageUrl = null;

        holder.itemname.setText(sold.getItem_name());
        holder.itemdesc.setText(sold.getDescription());
        holder.itemminbid.setText(sold.getMin_bid_price());


    }

    @Override
    public int getItemCount() {
        return soldList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView itemname,itemdesc,itemminbid;
        public ImageView itemimgae;
        public MaterialButton bidbutton;
        //private AlertDialog.Builder bidnowBuilder;
        private TextInputLayout bidnowInput;
        private TextInputEditText bidnowEdit;
        private MaterialButton bidnowButton;
       // private AlertDialog bidnowDialog;
        private FirebaseDatabase mDatabase_bid,database_activities;
        private DatabaseReference mDatabaseReference_bid,databaseReference_activities;
        String userId;

        private FirebaseAuth mAuth;
        private FirebaseUser mUser;



        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            mDatabase_bid = FirebaseDatabase.getInstance();
            mDatabaseReference_bid = mDatabase_bid.getReference().child("MBids");
            itemimgae = itemView.findViewById(R.id.sold_itemcardimage);
            itemname = itemView.findViewById(R.id.sold_carditemname);
            itemdesc = itemView.findViewById(R.id.sold_itemcardesc);
            itemminbid = itemView.findViewById(R.id.sold_minBidprice);
            bidbutton = itemView.findViewById(R.id.sold_bid_button);

            userId = null;

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
                    final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
                    View v = LayoutInflater.from(context).inflate(R.layout.sold_bid_now,null);
                    bidnowInput = v.findViewById(R.id.sold_bid_now_bidInput);
                    bidnowEdit = v.findViewById(R.id.sold_bid_now_bidEdit);
                    bidnowButton = v.findViewById(R.id.sold_bid_now_button);
                    bottomSheetDialog.setContentView(v);
                    bottomSheetDialog.show();
                    bidnowButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            String s = bidnowEdit.getText().toString().trim();
                            DatabaseReference newItem = mDatabaseReference_bid.push();
                            newItem.child("bid_price").setValue(s);
                            newItem.child("username").setValue(mUser.getEmail());
                            newItem.child("userId").setValue(mUser.getUid());
                            Snackbar.make(view, "Your Bid has been placed", Snackbar.LENGTH_LONG)
                                    .show();
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
