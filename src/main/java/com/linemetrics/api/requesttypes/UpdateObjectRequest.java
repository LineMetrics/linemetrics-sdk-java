package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

/**
 * Created by Klemens on 07.03.2017.
 */
public class UpdateObjectRequest extends BaseRequest {

    private UpdateData data;

    public UpdateObjectRequest(String objectId){
        this.setObjectId(objectId);
    }

    public UpdateObjectRequest(String objectId, UpdateData data){
        this.setObjectId(objectId);
        this.data = data;
    }

    public URI buildRequestUri(URI baseUri, String uriPath, String uriPathAppendix, boolean skipRequestParameters) throws ServiceException {
        URI result = null;

        if(StringUtils.isEmpty(uriPath)){
            throw new ServiceException("uriPath must not be null");
        }

        if(StringUtils.isEmpty(this.getObjectId())){
            throw new ServiceException("objectId must not be null");
        }

        StringBuilder builder = new StringBuilder(uriPath);
        builder.append("/");
        builder.append(this.getObjectId());

        final URIBuilder ub = new URIBuilder(baseUri);
        ub.setPath(ub.getPath() + builder.toString());

        try {
            result = ub.build();
        } catch(Exception e){
            e.printStackTrace();
            throw new ServiceException("Invalid URL "+e.getMessage());
        }
        return result;
    }

    public URI appendUrl(URI url) throws ServiceException {
        throw new NotImplementedException("appendUrl is not implemented");
    }

    public UpdateData getData() {
        return data;
    }

    public void setData(UpdateData data) {
        this.data = data;
    }
}
