package com.oceanservices.krayan.ui.home;

public class ProductItem {
    int image;
    String pname;
    double cost;
    boolean stock;

    public ProductItem(int image, String name, double cost,boolean stock) {
        this.image = image;
        this.pname = name;
        this.cost = cost;
        this.stock = stock;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public String getpName() {
        return pname;
    }
    public void setpName(String name) {
        this.pname = name;
    }
    public double getCost() {
        return cost;
    }
    public void setCost(double cost) {
        this.cost = cost;
    }
    public boolean isStock() {
        return stock;
    }
    public void setStock(boolean stock) {
        this.stock = stock;
    }
}
