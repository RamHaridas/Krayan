package com.oceanservices.krayan.ui.notifications;

public class CartData {
    String cname,quantity,amount,cost;
    int image;

    public CartData(String name, String quantity, String amount, String cost, int image) {
        this.cname = name;
        this.quantity = quantity;
        this.amount = amount;
        this.cost = cost;
        this.image = image;
    }

    public String getName() {
        return cname;
    }

    public void setName(String name) {
        this.cname = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
