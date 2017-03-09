package com.linemetrics.api.exceptions;

/**
 * Created by Klemens on 03.03.2017.
 */
public class ServiceException extends Exception {

    private String message;

    public ServiceException(final String message){
        this.message = message;
    }

    public String getMessage() {
       return this.message;
    }
}
