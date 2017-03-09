package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 03.03.2017.
 */
public class GeoAddress extends GeoCoord {

    @SerializedName("val")
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
