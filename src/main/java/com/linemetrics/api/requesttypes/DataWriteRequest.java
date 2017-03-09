package com.linemetrics.api.requesttypes;

import com.linemetrics.api.datatypes.Base;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemens on 07.03.2017.
 */
public class DataWriteRequest extends BaseDataWriteRequest {

    public DataWriteRequest(){}

    public DataWriteRequest(String objectId){
        this.setObjectId(objectId);
    }

    public DataWriteRequest(String customKey, String alias){
        this.setCustomKey(customKey);
        this.setAlias(alias);
    }

    private List<Base> payload;

    public List<Base> getPayload() {
        if(this.payload == null){
            this.payload = new ArrayList<>();
        }
        return payload;
    }

    public void setPayload(List<Base> payload) {
        this.payload = payload;
    }
}
