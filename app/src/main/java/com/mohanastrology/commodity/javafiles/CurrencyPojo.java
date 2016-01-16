package com.mohanastrology.commodity.javafiles;

/**
 * Created by user on 11/24/2015.
 */
public class CurrencyPojo {

    public String id;
    public String price;
    public String subcategory;

    public CurrencyPojo(String id,String subcategory,String price)
    {
        this.id=id;
        this.price=price;
        this.subcategory=subcategory;
    }

    public String getId() {
        return id;
    }
    public String getPrice() {
        return price;
    }
    public String getSubcategory() {
        return subcategory;
    }

}
