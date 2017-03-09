package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Klemens on 03.03.2017.
 */
public class Double extends Base {

    @SerializedName("val")
    private java.lang.Double value;

    public java.lang.Double getValue() {
        return value;
    }

    public void setValue(java.lang.Double value) {
        this.value = value;
    }
}
