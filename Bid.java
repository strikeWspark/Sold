package com.dryfire.sold.Modal;

import java.io.Serializable;

public class Bid implements Serializable {

    public String item_image;
    public String itemname;
    public String bid_price;
    public String item_key;

    public Bid(){

    }

    public Bid(String item_image, String itemname, String bid_price,String item_key) {
        this.item_image = item_image;
        this.itemname = itemname;
        this.bid_price = bid_price;
        this.item_key = item_key;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public void setBid_price(String bid_price) {
        this.bid_price = bid_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public String getItemname() {
        return itemname;
    }

    public String getBid_price() {
        return bid_price;
    }

    public String getItem_key(){
        return item_key;
    }
}
