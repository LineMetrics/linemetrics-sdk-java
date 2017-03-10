package com.linemetrics.api.requesttypes;

import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.ServiceException;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klemens on 08.03.2017.
 */
public class CreateAssetRequest extends BaseRequest {

    private Map<String, Base> payload;
    private String templateId;

    public Map<String, Base> getPayload() {
        if(payload == null){
            payload = new HashMap<>();
        }
        return payload;
    }

    public void setPayload(Map<String, Base> payload) {
        this.payload = payload;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    @Override
    public URI buildRequestUri(URI baseUri, String uriPath, String uriPathAppendix, boolean skipRequestParameters) throws ServiceException {
        URI result = null;
        if(StringUtils.isEmpty(uriPath)){
            throw new IllegalArgumentException("uriPath must not be null or empty");
        }
        if(StringUtils.isEmpty(this.templateId)){
            throw new IllegalArgumentException("templateId must not be null or empty");
        }
        StringBuilder builder = new StringBuilder(uriPath);
        builder.append("/");
        builder.append(this.templateId);

        URIBuilder ub = new URIBuilder(baseUri);
        ub.setPath(builder.toString());

        try {
            result = ub.build();
        } catch(Exception e){
            e.printStackTrace();
            throw new ServiceException("Invalid URL "+e.getMessage());
        }
        return result;
    }

    @Override
    public URI appendUrl(URI url) throws ServiceException {
       throw new NotImplementedException("appendUrl not implemented");
    }
}
