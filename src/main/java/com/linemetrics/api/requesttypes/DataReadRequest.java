package com.linemetrics.api.requesttypes;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.types.FunctionType;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klemens on 07.03.2017.
 */
public class DataReadRequest extends BaseDataReadRequest {

    private Date from;
    private Date to;
    private String timeZone;
    private String granularity;

    public DataReadRequest(){}
    public DataReadRequest(FunctionType function, Date from, Date to, String timeZone, String granularity){
        this.setFunction(function);
        this.from = from;
        this.to = to;
        this.timeZone = timeZone;
        this.granularity = granularity;
    }

    public DataReadRequest(String customKey, String alias, String objectId, Class<?> dataType, Date from, Date to, String timeZone, String granularity, FunctionType function){
        this.setCustomKey(customKey);
        this.setAlias(alias);
        this.setObjectId(objectId);
        this.from = from;
        this.to = to;
        this.timeZone = timeZone;
        this.granularity = granularity;
        this.setFunction(function);
        this.setDataType(dataType);
    }

    @Override
    public URI appendUrl(URI url) throws ServiceException {
        final Map<String, String> params = new HashMap<>();
        if(this.getFunction() != null){
            params.put("function", this.getFunction().getValue());
        }
        if(this.from != null){
            params.put("time_from", String.valueOf(this.from.getTime()));
        }
        if(this.to != null){
            params.put("time_to", String.valueOf(this.to.getTime()));
        }
        if(this.timeZone != null){
            params.put("time_zone", this.timeZone);
        }
        if(this.granularity != null){
            params.put("granularity", this.granularity);
        }

        try {
            return this.addRequestParams(url, params);
        } catch (URISyntaxException e){
            throw new ServiceException("Invalid URL "+e.getMessage());
        }
    }

    @Override
    public String toString(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return String.format("From: %s, To: %s, Timezone: %s, Granularity: %s, CustomKey: %s, Alias: %s, ObjectId: %s"
                , sdf.format(from), sdf.format(to), timeZone, granularity, getCustomKey(), getAlias(), getObjectId());
    }
}
