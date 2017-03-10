package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.URIHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klemens on 07.03.2017.
 */
public class DeleteObjectRequest extends BaseRequest {

    public DeleteObjectRequest(){}
    public DeleteObjectRequest(String customKey, Boolean recursive){
        this.setCustomKey(customKey);
        this.recursive = recursive;
    }

    private Boolean recursive;

    public URI buildRequestUri(URI baseUri, String uriPath, String uriPathAppendix, boolean skipRequestParameters) throws ServiceException {
        URI result = URIHelper.buildDefaultURI(baseUri, uriPath, getObjectId(), getCustomKey(), getAlias(), uriPathAppendix);
        if(result != null){
            if(!skipRequestParameters){
                result = this.appendUrl(result);
            }
        }
        return result;
    }

    public URI appendUrl(URI url) throws ServiceException {
        final Map<String, String> params = new HashMap<>();
        params.put("recursive", recursive!=null&&recursive?"1":"0");
        try {
            return addRequestParams(url, params);
        } catch (URISyntaxException e){
            throw new ServiceException("Invalid URL "+e.getMessage());
        }
    }
}
