package com.linemetrics.api.services;

import com.google.gson.JsonSyntaxException;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.requesttypes.CreateAssetRequest;
import com.linemetrics.api.requesttypes.TemplateRequest;
import com.linemetrics.api.requesttypes.TemplateRequiredFieldsRequest;
import com.linemetrics.api.returntypes.OAuth2Token;
import com.linemetrics.api.returntypes.Template;
import com.linemetrics.api.returntypes.TemplateRequiredFields;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 06.03.2017.
 */
public class TemplateService extends BaseService{

    public TemplateService(LineMetricsService serviceInstance){
        super(serviceInstance);
    }

    public List<Template> loadTemplates(OAuth2Token authToken, TemplateRequest templateRequest) throws ServiceException {
        if(templateRequest == null){
            throw new IllegalArgumentException("dataStreamConfigRequest must not be null");
        }
        try {
            final URI uri = templateRequest.buildRequestUri(this.baseUrl, "/v2/templates", null, true);
            this.restClient.setCreds(authToken);
            JSONArray result = (JSONArray)this.restClient.get(uri);
            if (result == null) {
                System.out.println("No data found");
                return null;
            }
            return addTemplateServiceInstance(toObjectList(result, Template.class));

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    public List<TemplateRequiredFields> loadRequiredFields(OAuth2Token authToken, TemplateRequiredFieldsRequest templateRequiredFieldsRequest) throws ServiceException {
        if(templateRequiredFieldsRequest == null){
            throw new IllegalArgumentException("templateRequiredFieldsRequest must not be null");
        }
        try {
            final URI uri = templateRequiredFieldsRequest.buildRequestUri(this.baseUrl, "/v2/template", "required-fields", true);
            this.restClient.setCreds(authToken);
            JSONArray result = (JSONArray)this.restClient.get(uri);
            if (result == null) {
                System.out.println("No data found");
                return null;
            }
            return toObjectList(result, TemplateRequiredFields.class);

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    public String createAsset(OAuth2Token authToken, CreateAssetRequest createAssetRequest) throws ServiceException{
        if(createAssetRequest == null){
            throw new IllegalArgumentException("createAssetRequest must not be null");
        }
        try {
            final URI uri = createAssetRequest.buildRequestUri(this.baseUrl, "/v2/template", null, true);
            this.restClient.setCreds(authToken);

            HttpEntity entity = new StringEntity(toJsonString(createAssetRequest.getPayload()));
            JSONObject result = (JSONObject)this.restClient.post(uri, entity);
            return result!=null&&result.containsKey("uid")?(String)result.get("uid"):null;

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }
}
