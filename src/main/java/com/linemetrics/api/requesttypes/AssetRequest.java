package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.URIHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klemens on 03.03.2017.
 */
public class AssetRequest extends BaseRequest{

    public AssetRequest(){}

    public AssetRequest (String id){
        this.setCustomKey(id);
    }

    public AssetRequest (String id, String alias){
        this.setCustomKey(id);
        this.setAlias(alias);
    }

    public AssetRequest (String id, String alias, String objectType){
        this.setCustomKey(id);
        this.setAlias(alias);
        this.objectType = objectType;
    }

    private String objectType;

    private Integer limit;
    private Integer offset;

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
        final Map<String, String> params = new HashMap<>();
        if(this.limit != null){
            params.put("limit", this.limit.toString());
        }
        if(this.offset != null){
            params.put("offset", this.offset.toString());
        }
        if(this.objectType != null){
            params.put("object_type", this.objectType);
        }

        try {
            return this.addRequestParams(url, params);
        } catch (URISyntaxException e){
            throw new ServiceException("Invalid URL "+e.getMessage());
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString(){
        return String.format("CustomKey: %s, Alias: %s, ObjectType: %s, Limit: %s, Offset: %s",
                this.getCustomKey(), this.getAlias(), this.getObjectType(), this.limit, this.offset);
    }
}
