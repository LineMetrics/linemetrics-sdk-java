package com.linemetrics.api.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.*;
import com.linemetrics.api.returntypes.*;
import com.linemetrics.api.types.FunctionType;
import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 06.03.2017.
 */
public class DataService extends BaseService{

    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    public DataService(LineMetricsService serviceInstance){
        super(serviceInstance);
        logger.debug("Create new instance of DataService");
    }

    /**
     * loads config of a specific datastream
     * @param authToken
     * @param dataStreamConfigRequest
     * @return
     * @throws ServiceException
     */
    public DataStreamType loadDataStreamConfig(OAuth2Token authToken, DataStreamConfigRequest dataStreamConfigRequest) throws ServiceException {
        if(dataStreamConfigRequest == null){
            throw new IllegalArgumentException("dataStreamConfigRequest must not be null");
        }
        logger.debug(String.format("Call loadDataStreamConfig with %s", dataStreamConfigRequest.toString()));

        try {
            final URI uri = dataStreamConfigRequest.buildRequestUri(this.baseUrl, "/v2/data", "/config", true);
            this.restClient.setCreds(authToken);
            JsonElement result = this.restClient.get(uri);
            if (result == null) {
                logger.info("No data found");
                return null;
            }
            return toObject((JsonObject)result, DataStreamType.class);

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    /**
     * @param authToken
     * @param keyValueDataWriteRequest
     * @return
     * @throws ServiceException
     */
    public Map<String, DataWriteResponse> write(OAuth2Token authToken, KeyValueDataWriteRequest keyValueDataWriteRequest) throws ServiceException{
        if(keyValueDataWriteRequest == null){
            throw new IllegalArgumentException("keyValueDataWriteRequest must not be null");
        }
        logger.debug(String.format("Call write with %s", keyValueDataWriteRequest.toString()));

        try {
            final URI uri = keyValueDataWriteRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);

            HttpEntity entity = new StringEntity(toJsonString(keyValueDataWriteRequest.getPayload()));
            JsonElement result = this.restClient.post(uri, entity);
            return toObject((JsonObject) result, Map.class);

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param dataWriteRequest
     * @return
     * @throws ServiceException
     */
    public List<DataWriteResponse> write(OAuth2Token authToken, DataWriteRequest dataWriteRequest) throws ServiceException{
        if(dataWriteRequest == null){
            throw new IllegalArgumentException("dataWriteRequest must not be null");
        }
        logger.debug(String.format("Call write with %s", dataWriteRequest.toString()));

        try {
            final URI uri = dataWriteRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);

            HttpEntity entity = new StringEntity(toJsonString(dataWriteRequest.getPayload()));
            JsonElement result = this.restClient.post(uri, entity);
            return toObjectList((JsonArray) result, DataWriteResponse.class);

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param lastValueDataReadRequest
     * @return
     * @throws ServiceException
     */
    public Base readLastValue(OAuth2Token authToken, LastValueDataReadRequest lastValueDataReadRequest) throws ServiceException{
        if(lastValueDataReadRequest == null){
            throw new IllegalArgumentException("lastValueDataReadRequest must not be null");
        }
        logger.debug(String.format("Call readLastValue with %s", lastValueDataReadRequest.toString()));

        lastValueDataReadRequest.setFunction(FunctionType.LAST_VALUE);
        try {
            final URI uri = lastValueDataReadRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);
            JsonElement result = this.restClient.get(uri);
            if (result == null) {
                logger.info("No data found");
                return null;
            }
            final List<Base> resultList = (List<Base>)toObjectList((JsonArray) result, lastValueDataReadRequest.getDataType());
            return resultList != null && resultList.size() > 0 ? resultList.get(0) : null;

        } catch(Exception e){
            handleException(e);
        }
        return null;
    }

    /**
     *
     * @param authToken
     * @param dataReadRequest
     * @return
     * @throws ServiceException
     */
    public List<DataReadResponse> read(OAuth2Token authToken, DataReadRequest dataReadRequest) throws ServiceException {
        if(dataReadRequest == null){
            throw new IllegalArgumentException("dataReadRequest must not be null");
        }
        logger.debug(String.format("Call read with %s", dataReadRequest.toString()));

        final List<DataReadResponse> resultList = new ArrayList<>();

        try {
            final URI uri = dataReadRequest.buildRequestUri(this.baseUrl, "/v2/data", null, false);
            this.restClient.setCreds(authToken);
            JsonElement result = this.restClient.get(uri);
            if (result == null) {
                logger.info("No data found");
                return null;
            }

            if(dataReadRequest.getFunction().getValue().equalsIgnoreCase(FunctionType.RAW.getValue())){
                final List<Base> tempList = (List<Base>) toObjectList((JsonArray) result, dataReadRequest.getDataType());
                if(tempList != null && tempList.size() > 0){
                    for(final Base entry : tempList){
                        resultList.add(new RawDataReadResponse(entry));
                    }
                }
            } else {
                final List<AggregatedDataReadResponse> tempList = toObjectList((JsonArray) result, AggregatedDataReadResponse.class);
                resultList.addAll(tempList);
            }

        } catch(Exception e){
            handleException(e);
        }
        return resultList;
    }
}