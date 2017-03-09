package com.linemetrics.api.requesttypes;

import com.linemetrics.api.datatypes.Base;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Klemens on 08.03.2017.
 */
public class KeyValueDataWriteRequest extends BaseDataWriteRequest {

    private Map<String, Base> payload;

    public Map<String, Base> getPayload() {
        if(this.payload == null){
            this.payload = new HashMap<>();
        }
        return payload;
    }

    public void setPayload(Map<String, Base> payload) {
        this.payload = payload;
    }
}
