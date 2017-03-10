package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.types.FunctionType;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klemens on 08.03.2017.
 */
public class LastValueDataReadRequest extends BaseDataReadRequest {

    public LastValueDataReadRequest(){
        this.setFunction(FunctionType.LAST_VALUE);
    }

    public LastValueDataReadRequest(Class<?> dataType, String objectId){
       this.setDataType(dataType);
       this.setObjectId(objectId);
    }

    public LastValueDataReadRequest(String customKey, String alias, String objectId, Class<?> dataType){
        this.setCustomKey(customKey);
        this.setAlias(alias);
        this.setDataType(dataType);
        this.setFunction(FunctionType.LAST_VALUE);
        this.setObjectId(objectId);
    }

    @Override
    public URI appendUrl(URI url) throws ServiceException {
        final Map<String, String> params = new HashMap<>();
        params.put("function", this.getFunction().getValue());
        try {
            return addRequestParams(url, params);
        } catch (URISyntaxException e){
            throw new ServiceException("Invalid URL "+e.getMessage());
        }
    }
}
