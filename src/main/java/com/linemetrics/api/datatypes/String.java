package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 03.03.2017.
 */
public class String extends Base {

    @SerializedName("val")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
