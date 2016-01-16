package com.mohanastrology.commodity.javafiles;

/**
 * Created by niraj on 11/14/2015.
 */
public class CommodityServicePojo {
     String sub_category;
     int id;
     String price;

    public CommodityServicePojo(int Id, String sub_category,String price) {
        this.sub_category=sub_category;
        this.id=Id;
        this.price=price;
    }
    public String getSub_category() {
        return sub_category;
    }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    }
