package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by User on 03.03.2017.
 */
public class String extends Base {

    @SerializedName("val")
    private java.lang.String value;

    public String(){}
    public String(java.lang.String value,
                  Date date){
        this.value = value;
        setTimestamp(date);
    }

    public java.lang.String getValue() {
        return value;
    }

    public void setValue(java.lang.String value) {
        this.value = value;
    }
}
