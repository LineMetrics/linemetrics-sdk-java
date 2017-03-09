package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Klemens on 03.03.2017.
 */
public class Timestamp extends Base {

    @SerializedName("val")
    private long value;

    public Timestamp(){}
    public Timestamp(Date date,
                     Date timestamp){
        value = date!=null?date.getTime():0;
        setTimestamp(timestamp);
    }

    public Date getValue(){
        return new Date(this.value);
    }

    public void setValue(Date value){
        this.value = value!=null?value.getTime():0;
    }
}
