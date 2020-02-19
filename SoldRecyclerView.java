package com.dryfire.sold.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dryfire.sold.Activities.MainActivity;
import com.dryfire.sold.Modal.Sold;
import com.dryfire.sold.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

import androidx.annotation.NonNull;
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
        String userId;

        public ViewHolder(@NonNull View itemView, final Context ctx) {
            super(itemView);
            context = ctx;

            itemimgae = itemView.findViewById(R.id.sold_itemcardimage);
            itemname = itemView.findViewById(R.id.sold_carditemname);
            itemdesc = itemView.findViewById(R.id.sold_itemcardesc);
            itemminbid = itemView.findViewById(R.id.sold_minBidprice);
            bidbutton = itemView.findViewById(R.id.sold_bid_button);

            userId = null;



        }
    }
}
