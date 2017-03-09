package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by User on 03.03.2017.
 */
public class DoubleAverage extends Base {

    @SerializedName("val")
    private java.lang.Double value;

    @SerializedName("min")
    private java.lang.Double minimum;

    @SerializedName("max")
    private java.lang.Double maximum;

    public DoubleAverage(){}

    public DoubleAverage(java.lang.Double value,
            java.lang.Double minimum,
            java.lang.Double maximum,
            Date date){
        this.value = value;
        this.minimum = minimum;
        this.maximum = maximum;
        this.setTimestamp(date);
    }


    public java.lang.Double getValue() {
        return value;
    }

    public void setValue(java.lang.Double value) {
        this.value = value;
    }

    public java.lang.Double getMinimum() {
        return minimum;
    }

    public void setMinimum(java.lang.Double minimum) {
        this.minimum = minimum;
    }

    public java.lang.Double getMaximum() {
        return maximum;
    }

    public void setMaximum(java.lang.Double maximum) {
        this.maximum = maximum;
    }

    public java.lang.String toString(){
        return super.toString() +  java.lang.String.format(", Value: %s, Min: %s, Max: %s", value.toString(), minimum.toString(), maximum.toString());
    }
}
