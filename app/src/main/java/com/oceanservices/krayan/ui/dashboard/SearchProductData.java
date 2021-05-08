package com.oceanservices.krayan.ui.dashboard;

public class SearchProductData {
    String sname,shopname,cost;
    boolean stock;

    public SearchProductData(String sname, String shopname, String cost, boolean stock) {
        this.sname = sname;
        this.shopname = shopname;
        this.cost = cost;
        this.stock = stock;
    }

    public String getName() {
        return sname;
    }

    public void setName(String name) {
        this.sname = name;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean isStock() {
        return stock;
    }

    public void setStock(boolean stock) {
        this.stock = stock;
    }
}
