package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.URIHelper;

import java.net.URI;

/**
 * Created by Klemens on 07.03.2017.
 */
public abstract class BaseDataWriteRequest extends BaseRequest {

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

    @Override
    public URI appendUrl(URI url) throws ServiceException {
        //throw new NotImplementedException("appendUrl not implemented");
        return url;
    }
}
