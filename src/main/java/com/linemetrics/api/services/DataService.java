package com.linemetrics.api.services;

import com.google.gson.JsonSyntaxException;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.*;
import com.linemetrics.api.returntypes.*;
import com.linemetrics.api.types.FunctionType;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 06.03.2017.
 */
public class DataService extends BaseService{

    public DataService(LineMetricsService serviceInstance){
        super(serviceInstance);
    }

    public DataStreamType loadDataStreamConfig(OAuth2Token authToken, DataStreamConfigRequest dataStreamConfigRequest) throws ServiceException {
        if(dataStreamConfigRequest == null){
            throw new IllegalArgumentException("dataStreamConfigRequest must not be null");
        }
        try {
            final URI uri = dataStreamConfigRequest.buildRequestUri(this.baseUrl, "/v2/data", "/config", true);
            this.restClient.setCreds(authToken);
            JSONObject result = (JSONObject) this.restClient.get(uri);
            if (result == null) {
                System.out.println("No data found");
                return null;
            }
            return toObject(result, DataStreamType.class);

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    public Map<String, DataWriteResponse> write(OAuth2Token authToken, KeyValueDataWriteRequest keyValueDataWriteRequest) throws ServiceException{
        if(keyValueDataWriteRequest == null){
            throw new IllegalArgumentException("keyValueDataWriteRequest must not be null");
        }
        try {
            final URI uri = keyValueDataWriteRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);

            HttpEntity entity = new StringEntity(toJsonString(keyValueDataWriteRequest.getPayload()));
            JSONObject result = (JSONObject) this.restClient.post(uri, entity);
            return toObject(result, Map.class);

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    public List<DataWriteResponse> write(OAuth2Token authToken, DataWriteRequest dataWriteRequest) throws ServiceException{
        if(dataWriteRequest == null){
            throw new IllegalArgumentException("dataWriteRequest must not be null");
        }
        try {
            final URI uri = dataWriteRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);

            HttpEntity entity = new StringEntity(toJsonString(dataWriteRequest.getPayload()));
            JSONArray result = (JSONArray)this.restClient.post(uri, entity);
            return toObjectList(result, DataWriteResponse.class);

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    public Base readLastValue(OAuth2Token authToken, LastValueDataReadRequest lastValueDataReadRequest) throws ServiceException{
        if(lastValueDataReadRequest == null){
            throw new IllegalArgumentException("lastValueDataReadRequest must not be null");
        }
        lastValueDataReadRequest.setFunction(FunctionType.LAST_VALUE);
        try {
            final URI uri = lastValueDataReadRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);
            JSONArray result = (JSONArray)this.restClient.get(uri);
            if (result == null) {
                System.out.println("No data found");
                return null;
            }
            final List<Base> resultList = (List<Base>)toObjectList(result, lastValueDataReadRequest.getDataType());
            return resultList != null && resultList.size() > 0 ? resultList.get(0) : null;

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    public List<DataReadResponse> read(OAuth2Token authToken, DataReadRequest dataReadRequest) throws ServiceException{
        if(dataReadRequest == null){
            throw new IllegalArgumentException("dataReadRequest must not be null");
        }

        final List<DataReadResponse> resultList = new ArrayList<>();

        try {
            final URI uri = dataReadRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);
            JSONArray result = (JSONArray)this.restClient.get(uri);
            if (result == null) {
                System.out.println("No data found");
                return null;
            }

            if(dataReadRequest.getFunction().getValue().equalsIgnoreCase(FunctionType.RAW.getValue())){
                final List<Base> tempList = (List<Base>) toObjectList(result, dataReadRequest.getDataType());
                if(tempList != null && tempList.size() > 0){
                    for(final Base entry : tempList){
                        resultList.add(new RawDataReadResponse(entry));
                    }
                }
            } else {
                final List<AggregatedDataReadResponse> tempList = toObjectList(result, AggregatedDataReadResponse.class);
                resultList.addAll(tempList);
            }

        } catch(Exception e){
            handleException(e);
        }
        return resultList;
    }
}