package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

/**
 * Created by Klemens on 03.03.2017.
 */
public abstract class BaseRequest {

    private String objectId;
    private String customKey;
    private String alias;

    public abstract URI buildRequestUri(URI baseUri, String uriPath, String uriPathAppendix, boolean skipRequestParameters) throws ServiceException;
    public abstract URI appendUrl(URI url) throws ServiceException;

    protected URI addRequestParams(final URI uri, final Map<String, String> params) throws URISyntaxException {
        URIBuilder ub = new URIBuilder(uri);

        if(params != null && params.size() > 0){
            for(Map.Entry<String, String> entry : params.entrySet()){
                ub.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return ub.build();
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCustomKey() {
        return customKey;
    }

    public void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
