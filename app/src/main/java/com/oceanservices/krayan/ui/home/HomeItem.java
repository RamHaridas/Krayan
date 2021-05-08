package com.oceanservices.krayan.ui.home;

public class HomeItem {
    int image;
    String shopname;
    public HomeItem(int image, String shopname) {
        this.image = image;
        this.shopname = shopname;
    }
    public int getImage() {
        return image;
    }
    public void setImage(int image) {
        this.image = image;
    }
    public String getShopName() {
        return shopname;
    }
    public void setShopName(String name) {
        this.shopname = name;
    }
}
