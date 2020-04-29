package com.dryfire.sold.Modal;

import java.io.Serializable;

public class Sold implements Serializable {

    public String item_name;
    public String key;
    public String description;
    public String min_bid_price;
    public String item_image;
    public String timeStamp;
    public String upiId;
    public String location;
    public String mobile_no;
    public String name;
    public String username;
    public String userId;

    public Sold(){

    }

    public Sold(String item_name, String description, String min_bid_price,
                String item_image, String timeStamp, String upiId, String location,
                String mobile_no, String name,String username, String userId,String key) {
        this.item_name = item_name;
        this.description = description;
        this.min_bid_price = min_bid_price;
        this.item_image = item_image;
        this.timeStamp = timeStamp;
        this.upiId = upiId;
        this.location = location;
        this.mobile_no = mobile_no;
        this.name = name;
        this.username = username;
        this.userId = userId;
        this.key = key;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMin_bid_price() {
        return min_bid_price;
    }

    public void setMin_bid_price(String min_bid_price) {
        this.min_bid_price = min_bid_price;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUpiId(){
        return upiId;
    }

    public void setUpiId(String upiId){
        this.upiId = upiId;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setKey(String key){
        this.key = key;
    }
    public String getKey_node_value(){
        return key;
    }


}
