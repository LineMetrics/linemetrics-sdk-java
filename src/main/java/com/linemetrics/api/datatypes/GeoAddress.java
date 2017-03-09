package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by User on 03.03.2017.
 */
public class GeoAddress extends GeoCoord {

    @SerializedName("val")
    private String address;

    public GeoAddress(){}
    public GeoAddress(String address,
                      Date date){
        this.address = address;
        setTimestamp(date);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
