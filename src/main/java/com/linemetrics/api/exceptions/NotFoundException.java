package com.linemetrics.api.exceptions;

/**
 * Created by Klemens on 24.03.2017.
 */
public class NotFoundException extends ServiceException {
    public NotFoundException(RestException e){
        super(e.getMessage());
    }
}
