package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.URIHelper;
import com.linemetrics.api.types.FunctionType;

import java.net.URI;

/**
 * Created by Klemens on 07.03.2017.
 */
public abstract class BaseDataReadRequest extends BaseRequest{

    private FunctionType function;
    private Class<?> dataType;

    @Override
    public URI buildRequestUri(URI baseUri, String uriPath, String uriPathAppendix, boolean skipRequestParameters) throws ServiceException {
        URI result = URIHelper.buildDefaultURI(baseUri, uriPath, getObjectId(), getCustomKey(), getAlias(), uriPathAppendix);
        if(result != null){
            if(!skipRequestParameters){
                result = this.appendUrl(result);
            }
        }
        return result;
    }

    public FunctionType getFunction() {
        return function;
    }

    public void setFunction(FunctionType function) {
        this.function = function;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    public void setDataType(Class<?> dataType) {
        this.dataType = dataType;
    }
}
