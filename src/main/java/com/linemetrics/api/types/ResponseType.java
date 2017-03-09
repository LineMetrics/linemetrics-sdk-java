package com.linemetrics.api.types;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Klemens on 08.03.2017.
 */
public enum ResponseType {
    SUCCESS("success"),
    UNKNOWN("unknown");

    private ResponseType(String value){
        this.value = value;
    }

    private String value;

    public static ResponseType getByValue(String value){
        if(StringUtils.isNotEmpty(value)){
            for(ResponseType entry : values()){
                if(entry.value.equalsIgnoreCase(value)){
                    return entry;
                }
            }
        }
        return null;
    }

    public static boolean equals(final ResponseType type1, final ResponseType type2){
        return type1 != null && type2 != null && type1.toString().equalsIgnoreCase(type2.toString());
    }
}