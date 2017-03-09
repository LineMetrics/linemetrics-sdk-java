package com.linemetrics.api.datatypes;

import com.google.gson.annotations.SerializedName;

import java.lang.*;
import java.lang.String;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 03.03.2017.
 */
public abstract class Base {

    @SerializedName(value = "ts")
    private Long unixTicks;

    public Date getTimestamp(){
        return new Date(unixTicks);
    }

    public void setTimestamp(final Date timestamp){
        this.unixTicks = timestamp.getTime();
    }

    public java.lang.String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return String.format("Timestamp: %s", sdf.format(getTimestamp()));
    }
}
