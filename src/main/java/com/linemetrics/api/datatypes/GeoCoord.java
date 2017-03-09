package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 03.03.2017.
 */
public class GeoCoord extends Base {

    @SerializedName("lat")
    private java.lang.Double latitude;

    @SerializedName("long")
    private java.lang.Double longitude;

    @SerializedName("precision")
    private Integer precision;

    public java.lang.Double getLatitude() {
        return latitude;
    }

    public void setLatitude(java.lang.Double latitude) {
        this.latitude = latitude;
    }

    public java.lang.Double getLongitude() {
        return longitude;
    }

    public void setLongitude(java.lang.Double longitude) {
        this.longitude = longitude;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }
}
