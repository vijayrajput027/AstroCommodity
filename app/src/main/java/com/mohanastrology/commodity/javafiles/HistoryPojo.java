package com.mohanastrology.commodity.javafiles;

import android.graphics.Bitmap;

/**
 * Created by user on 11/19/2015.
 */
public class HistoryPojo {
    String id;
    String imagepath;
    String imagename;
    Bitmap image;
    String date;
    String category;

    public String getSubcategory() {
        return subcategory;
    }

    public String getCategory() {
        return category;
    }

    String subcategory;

    public HistoryPojo(String imagepath, Bitmap image, String date, String category, String subcategory)
    {
        this.imagepath=imagepath;
        this.image=image;
        this.date=date;
        this.category=category;
        this.subcategory=subcategory;
    }
    public HistoryPojo(String imageid,String imagename, Bitmap image, String date, String category, String subcategory)
    {
        this.id=imageid;
        this.imagename=imagename;
        this.image=image;
        this.date=date;
        this.category=category;
        this.subcategory=subcategory;
    }

    public String getId() {
        return id;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getDate() {
        return date;
    }
}
