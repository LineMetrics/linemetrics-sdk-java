package com.linemetrics.api.exceptions;

/**
 * Created by Klemens on 08.03.2017.
 */
public class AuthorizationException extends ServiceException {
    public AuthorizationException(RestException e){
        super(e.getMessage());
    }
}
