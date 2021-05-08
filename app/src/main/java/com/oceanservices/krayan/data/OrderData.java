package com.oceanservices.krayan.data;

import java.util.List;

public class OrderData {
    String user_uid,shop_uid,user_address1,user_address2,user_mobile,time,date,shop_name,user_name,delivery_charge,unique_id;
    double userLat,userLong;
    double shopLat,shopLong;
    boolean shopCompleted,userCompleted,driver;
    List<ProductData> productDataList;
    public OrderData(){}

    public List<ProductData> getProductDataList() {
        return productDataList;
    }

    public String getUser_uid() {
        return user_uid;
    }

    public void setUser_uid(String user_uid) {
        this.user_uid = user_uid;
    }

    public String getShop_uid() {
        return shop_uid;
    }

    public void setShop_uid(String shop_uid) {
        this.shop_uid = shop_uid;
    }

    public String getUser_address1() {
        return user_address1;
    }

    public void setUser_address1(String user_address1) {
        this.user_address1 = user_address1;
    }

    public String getUser_address2() {
        return user_address2;
    }

    public void setUser_address2(String user_address2) {
        this.user_address2 = user_address2;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public double getUserLat() {
        return userLat;
    }

    public void setUserLat(double userLat) {
        this.userLat = userLat;
    }

    public double getUserLong() {
        return userLong;
    }

    public void setUserLong(double userLong) {
        this.userLong = userLong;
    }

    public double getShopLat() {
        return shopLat;
    }

    public void setShopLat(double shopLat) {
        this.shopLat = shopLat;
    }

    public double getShopLong() {
        return shopLong;
    }

    public void setShopLong(double shopLong) {
        this.shopLong = shopLong;
    }

    public boolean isShopCompleted() {
        return shopCompleted;
    }

    public void setShopCompleted(boolean shopCompleted) {
        this.shopCompleted = shopCompleted;
    }

    public boolean isUserCompleted() {
        return userCompleted;
    }

    public void setUserCompleted(boolean userCompleted) {
        this.userCompleted = userCompleted;
    }

    public void setProductDataList(List<ProductData> productDataList) {
        this.productDataList = productDataList;
    }

    public boolean isDriver() {
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }
}
