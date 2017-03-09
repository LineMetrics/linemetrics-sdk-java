package com.linemetrics.api.services;

import com.google.gson.JsonSyntaxException;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.AssetRequest;
import com.linemetrics.api.requesttypes.DeleteObjectRequest;
import com.linemetrics.api.types.ResourceType;
import com.linemetrics.api.requesttypes.UpdateObjectRequest;
import com.linemetrics.api.returntypes.Asset;
import com.linemetrics.api.returntypes.OAuth2Token;
import com.linemetrics.api.returntypes.ObjectBase;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.List;

/**
 * Created by Klemens on 03.03.2017.
 */
public class ObjectService extends BaseService {

    public ObjectService(LineMetricsService serviceInstance){
        super(serviceInstance);
    }

    /**
     *
     * @param authToken
     * @param assetRequest
     * @return
     * @throws ServiceException
     */
    public ObjectBase loadObject(OAuth2Token authToken, AssetRequest assetRequest) throws ServiceException {
        if(assetRequest == null){
            throw new IllegalArgumentException("AssetRequest must be filled in");
        }
        try {
            this.restClient.setCreds(authToken);
            final JSONObject result = (JSONObject) this.restClient.get(assetRequest.buildRequestUri(this.baseUrl, "/v2/object", null, false));
            if(result == null){
                System.out.println("No data found");
                return null;
            }
            return addServiceInstance(toObject(result, ObjectBase.class));

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param assetRequest
     * @return
     * @throws ServiceException
     */
    public List<ObjectBase> loadObjects(OAuth2Token authToken, AssetRequest assetRequest) throws ServiceException {
        if(assetRequest == null){
            throw new IllegalArgumentException("AssetRequest must be filled in");
        }
        try {
            this.restClient.setCreds(authToken);
            final JSONArray result = (JSONArray)this.restClient.get(assetRequest.buildRequestUri(this.baseUrl, "/v2/children", null, false));
            if(result == null){
                System.out.println("No data found");
                return null;
            }
            return addServiceInstance(toObjectList(result, ObjectBase.class));

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param assetRequest
     * @return
     * @throws ServiceException
     */
    public List<Asset> loadRootAssets(OAuth2Token authToken, AssetRequest assetRequest) throws ServiceException {
        if(assetRequest == null){
            throw new IllegalArgumentException("AssetRequest must be filled in");
        }

        assetRequest.setObjectType(ResourceType.ASSET.getValue());
        assetRequest.setCustomKey(null);
        assetRequest.setAlias(null);

        return (List<Asset>)(List<?>)this.loadObjects(authToken, assetRequest);
    }

    /**
     * Updates object with given ID
     * @param authToken
     * @param request
     * @return
     * @throws ServiceException
     */
    public String update(OAuth2Token authToken, UpdateObjectRequest request) throws ServiceException {
        if(request == null){
            throw new IllegalArgumentException("UpdateObjectRequest must be filled in");
        }
        try {
            final URI uri = request.buildRequestUri(this.baseUrl, "/v2/object", null, true);
            this.restClient.setCreds(authToken);

            final HttpEntity entity = new StringEntity(toJsonString(request.getData()));
            final JSONObject result = (JSONObject)this.restClient.post(uri, true, entity);

            return result.containsKey("message")?(String)result.get("message"):null;
        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

    /**
     * Deletes object with given ID
     * @param authToken
     * @param request
     * @return
     * @throws ServiceException
     */
    public String delete(OAuth2Token authToken, DeleteObjectRequest request) throws ServiceException {
        if(request == null){
            throw new IllegalArgumentException("DeleteObjectRequest must be filled in");
        }
        try {
            this.restClient.setCreds(authToken);
            final URI uri = request.buildRequestUri(this.baseUrl, "/v2/object", null, false);
            final JSONObject result = (JSONObject)this.restClient.delete(uri, true);
            return result.containsKey("message")?(String)result.get("message"):null;

        } catch (Exception e){
            this.handleException(e);
        }
        return null;
    }

}
