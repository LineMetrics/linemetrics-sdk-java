package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.lang.*;
import java.lang.String;

/**
 *
 * TODO not defined
 * Created by Klemens on 09.03.2017.
 */
public class Digital extends Base {

    @SerializedName("val")
    private Boolean value;

    public Boolean getValue() {
        return value;
    }

    public void setValue(Boolean value) {
        this.value = value;
    }

    @Override
    public java.lang.String toString(){
        return super.toString() + String.format(", Value: %s", value);
    }
}
