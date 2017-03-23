package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.lang.*;
import java.util.Date;

/**
 * Created by Klemens on 23.03.2017.
 */
public class State extends Base {

    @SerializedName("val")
    private int value;

    public State(int value, Date date){
        this.value = value;
        setTimestamp(date);
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public java.lang.String toString(){
        return super.toString() + java.lang.String.format(", Value: %s", value);
    }

}
