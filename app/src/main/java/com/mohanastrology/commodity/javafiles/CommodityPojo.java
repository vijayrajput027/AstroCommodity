package com.mohanastrology.commodity.javafiles;

/**
 * Created by niraj on 11/14/2015.
 */
public class CommodityPojo {

    String title;
    int Id;
    
    public CommodityPojo(int Id, String title) {
        this.title=title;
        this.Id=Id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

}
