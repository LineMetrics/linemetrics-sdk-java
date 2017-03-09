package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 03.03.2017.
 */
public class Bool extends Base {

    @SerializedName("val")
    private Integer value;

    public boolean getValue(){
        return this.value==1?true:false;
    }

    public void setValue(boolean value){
        this.value = value?1:0;
    }
}