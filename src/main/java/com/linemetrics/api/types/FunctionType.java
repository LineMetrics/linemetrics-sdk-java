package com.linemetrics.api.types;

/**
 * Created by Klemens on 07.03.2017.
 */
public enum FunctionType {

    RAW("raw"),
    SUM("sum"),
    AVERAGE("average"),
    LAST_VALUE("last_value");

    private FunctionType(String value){
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
