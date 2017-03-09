package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by User on 03.03.2017.
 */
public class Bool extends Base {

    @SerializedName("val")
    private Integer value;

    public Bool(){}

    public Bool(Integer value,
                Date date){
        this.value = value;
        setTimestamp(date);
    }

    public boolean getValue(){
        return this.value==1?true:false;
    }

    public void setValue(boolean value){
        this.value = value?1:0;
    }
}