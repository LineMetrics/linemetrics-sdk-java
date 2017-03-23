package com.linemetrics.api.datatypes;

import com.linemetrics.api.exceptions.ServiceException;

import java.util.Date;

/**
 * Created by Klemens on 23.03.2017.
 */
public class TypeFactory {

    public static Base getDataType(Object type, long timestamp) throws ServiceException {
        if(type instanceof java.lang.Double){
            return new Double((java.lang.Double)type, new Date(timestamp));
        } else if(type instanceof java.lang.String){
            return new String((java.lang.String) type, new Date(timestamp));
        } else if(type instanceof Date){
            return new Timestamp((Date)type, new Date(timestamp));
        } else if(type instanceof Boolean){
            return new Digital(((Boolean) type).booleanValue(), new Date(timestamp));
        } else {
            throw new ServiceException(java.lang.String.format("Datatype % not supported", type.getClass()));
        }
    }

    public static Base getDataType(Object type, long timestamp, Class<?> expected) throws ServiceException {

            //allowed outputs for Double -> Double, DoubleAverage
        if (type instanceof java.lang.Double) {
            if (expected.getName().equals(Double.class.getName())) {
                return new Double((java.lang.Double) type, new Date(timestamp));
            } else if (expected.getName().equals(DoubleAverage.class.getName())) {
                return new DoubleAverage((java.lang.Double) type, (java.lang.Double) type, (java.lang.Double) type, new Date(timestamp));
            }

            //allowed outputs for String -> String
        } else if (type instanceof java.lang.String) {
            if (expected.getName().equals(String.class.getName())) {
                return new String((java.lang.String) type, new Date(timestamp));
            }

            //allowed outputs for Date -> Timestamp
        } else if (type instanceof Date) {
            if (expected.getName().equals(Timestamp.class.getName())) {
                return new Timestamp((Date) type, new Date(timestamp));
            }

            //allowed outputs for Boolean -> Bool, Digital, State
        } else if (type instanceof Boolean) {
            if (expected.getName().equals(Digital.class.getName())) {
                return new Digital(((Boolean) type).booleanValue(), new Date(timestamp));
            } else if (expected.getName().equals(State.class.getName())) {
                return new State(((Boolean) type).booleanValue() ? 1 : 0, new Date(timestamp));
            } else if (expected.getName().equals(Bool.class.getName())) {
                return new Bool(((Boolean) type).booleanValue() ? 1 : 0, new Date(timestamp));
            }
        }
        return null;
    }
}
