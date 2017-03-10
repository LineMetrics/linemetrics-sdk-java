package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.URIHelper;
import org.apache.commons.lang3.NotImplementedException;

import java.net.URI;

/**
 * Created by Klemens on 07.03.2017.
 */
public class DataStreamConfigRequest extends BaseRequest {

    @Deprecated
    public DataStreamConfigRequest(){}

    public DataStreamConfigRequest(String customKey, String alias){
        this.setCustomKey(customKey);
        this.setAlias(alias);
    }

    public DataStreamConfigRequest(String customKey, String alias, String objectId){
        this.setCustomKey(customKey);
        this.setAlias(alias);
        this.setObjectId(objectId);
    }

    @Override
    public URI buildRequestUri(URI baseUri, String uriPath, String uriPathAppendix, boolean skipRequestParameters) throws ServiceException {
        return URIHelper.buildDefaultURI(baseUri, uriPath, getObjectId(), getCustomKey(), getAlias(), uriPathAppendix);
    }

    @Override
    public URI appendUrl(URI url) throws ServiceException {
        throw new NotImplementedException("appendUrl not implemented");
    }

    @Override
    public String toString(){
        return String.format("CustomKey: %s, Alias: %s, ObjectId: %s", getCustomKey(), getAlias(), getObjectId());
    }
}
