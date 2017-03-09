package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

/**
 *
 * TODO not defined
 * Created by Klemens on 09.03.2017.
 */
public class Digital extends Base {

    @SerializedName("val")
    private Integer value;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
