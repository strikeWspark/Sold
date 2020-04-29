package com.dryfire.sold.Adapter;

import android.app.Activity;
import android.content.Context;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dryfire.sold.Modal.Bid;

import com.dryfire.sold.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BidListAdapter extends BaseAdapter {
    private Context context;
    List<Bid> bid_list;

    public BidListAdapter(Context context, List<Bid> bidList) {
        this.context = context;
        this.bid_list = bidList;

    }

    @Override
    public int getCount() {
        return bid_list.size();
    }

    @Override
    public Object getItem(int position) {
        return bid_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Bid bid = (Bid) getItem(position);
        Activity at = (Activity) context;

        View v = at.getLayoutInflater().inflate(R.layout.sold_user_bid_list,null);
        ImageView imageView = v.findViewById(R.id.bid_list_itemImage);
        TextView itemname = v.findViewById(R.id.bid_list_itemName);
        TextView bidPrice = v.findViewById(R.id.bid_list_itemPrice);

        String imageurl =  bid.getItem_image();

        Picasso.with(context).load(imageurl).into(imageView);
        itemname.setText(bid.getItemname());
        Log.d(bid.getItem_key(), "getView: ");
        bidPrice.setText(bid.getBid_price());
        return v;
    }
}
