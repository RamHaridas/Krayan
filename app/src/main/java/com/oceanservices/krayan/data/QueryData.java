package com.oceanservices.krayan.data;

import java.util.List;

public class QueryData {
    String name,description,uid;
    List<String> shopkeepers;

    public QueryData(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getShopkeepers() {
        return shopkeepers;
    }

    public void setShopkeepers(List<String> shopkeepers) {
        this.shopkeepers = shopkeepers;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
