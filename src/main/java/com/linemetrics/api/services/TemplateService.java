package com.linemetrics.api.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.CreateAssetRequest;
import com.linemetrics.api.requesttypes.TemplateRequest;
import com.linemetrics.api.requesttypes.TemplateRequiredFieldsRequest;
import com.linemetrics.api.returntypes.OAuth2Token;
import com.linemetrics.api.returntypes.Template;
import com.linemetrics.api.returntypes.TemplateRequiredFields;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;

/**
 * Created by Klemens on 06.03.2017.
 */
public class TemplateService extends BaseService{

    private static final Logger logger = LoggerFactory.getLogger(TemplateService.class);

    public TemplateService(LineMetricsService serviceInstance){
        super(serviceInstance);
    }

    /**
     *
     * @param authToken
     * @param templateRequest
     * @return
     * @throws ServiceException
     */
    public List<Template> loadTemplates(OAuth2Token authToken, TemplateRequest templateRequest) throws ServiceException {
        if(templateRequest == null){
            throw new IllegalArgumentException("dataStreamConfigRequest must not be null");
        }
        logger.debug(String.format("Call loadTemplates with %s", templateRequest.toString()));
        try {
            final URI uri = templateRequest.buildRequestUri(this.baseUrl, "/v2/templates", null, true);
            this.restClient.setCreds(authToken);
            JsonElement result = this.restClient.get(uri);
            if (result == null) {
                logger.info("No data found");
                return null;
            }
            return addTemplateServiceInstance(toObjectList((JsonArray) result, Template.class));

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param templateRequiredFieldsRequest
     * @return
     * @throws ServiceException
     */
    public List<TemplateRequiredFields> loadRequiredFields(OAuth2Token authToken, TemplateRequiredFieldsRequest templateRequiredFieldsRequest) throws ServiceException {
        if(templateRequiredFieldsRequest == null){
            throw new IllegalArgumentException("templateRequiredFieldsRequest must not be null");
        }
        logger.debug(String.format("Call loadRequiredFields with %s", templateRequiredFieldsRequest.toString()));
        try {
            final URI uri = templateRequiredFieldsRequest.buildRequestUri(this.baseUrl, "/v2/template", "required-fields", true);
            this.restClient.setCreds(authToken);
            JsonElement result = this.restClient.get(uri);
            if (result == null) {
                logger.info("No data found");
                return null;
            }
            return toObjectList((JsonArray) result, TemplateRequiredFields.class);

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param createAssetRequest
     * @return
     * @throws ServiceException
     */
    public String createAsset(OAuth2Token authToken, CreateAssetRequest createAssetRequest) throws ServiceException{
        if(createAssetRequest == null){
            throw new IllegalArgumentException("createAssetRequest must not be null");
        }
        logger.debug(String.format("Call createAsset with %s", createAssetRequest.toString()));
        try {
            final URI uri = createAssetRequest.buildRequestUri(this.baseUrl, "/v2/template", null, true);
            this.restClient.setCreds(authToken);

            HttpEntity entity = new StringEntity(toJsonString(createAssetRequest.getPayload()));
            JsonElement result = this.restClient.post(uri, entity);
            return ((JsonObject)result).has("uid")?((JsonObject)result).get("uid").getAsString():null;

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }
}
