package com.linemetrics.api.exceptions;

/**
 * Created by Klemens on 03.03.2017.
 */
public class RestException extends Exception {

    private int status;
    private String result;

    public RestException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public RestException(String msg, int status, String result) {
        this(msg, status);
        this.result = result;
    }

    public int getHttpStatusCode() {
        return status;
    }

    public String getHttpResult() {
        return result;
    }

    public String getMessage() {
        if (result == null) {
            return String.format("%s %s", Integer.toString(status), super.getMessage());
        } else {
            return String.format("%s %s: %s", Integer.toString(status), super.getMessage(), result);
        }
    }
}