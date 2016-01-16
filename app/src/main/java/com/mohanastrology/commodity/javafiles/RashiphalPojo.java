package com.mohanastrology.commodity.javafiles;

/**
 * Created by user on 11/25/2015.
 */
public class RashiphalPojo {

    private String rashiName;
    private int rashiImage;

    public RashiphalPojo(String rashiName,int rashiImage)
    {
        this.rashiName=rashiName;
        this.rashiImage=rashiImage;
    }
    public String getrashiName() {
        return rashiName;
    }

    public int getRashiImage() {
        return rashiImage;
    }

}
