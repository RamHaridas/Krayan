package com.oceanservices.krayan.data;

import java.util.HashMap;
import java.util.Map;

public class ProductData {
    String image,name,quantity,price,shop_uid,ref1,ref2;
    int count;
    boolean Expanded,Stock;
    public ProductData(){}

    public int getCount() {
        return count;
    }

    public boolean isStock() {
        return Stock;
    }

    public boolean isExpanded() {
        return Expanded;
    }

    public String getRef1() {
        return ref1;
    }

    public String getRef2() {
        return ref2;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getShop_uid() {
        return shop_uid;
    }

    public void setShop_uid(String shop_uid) {
        this.shop_uid = shop_uid;
    }

    public void setExpanded(boolean expanded) {
        Expanded = expanded;
    }

    public void setStock(boolean stock) {
        Stock = stock;
    }

    public void setRef1(String ref1) {
        this.ref1 = ref1;
    }

    public void setRef2(String ref2) {
        this.ref2 = ref2;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Map<String,Object> toMap(){
        Map<String,Object> map = new HashMap<>();
        /*image,name,quantity,price,shop_uid,ref1,ref2,Expanded,Stock*/
        map.put("image",image);
        map.put("name",name);
        map.put("quantity",quantity);
        map.put("price",price);
        map.put("shop_uid",shop_uid);
        map.put("ref1",ref1);
        map.put("ref2",ref2);
        map.put("expanded",Expanded);
        return map;
    }
}
