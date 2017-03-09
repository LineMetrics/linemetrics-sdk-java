package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.types.ResponseType;

/**
 * Created by Klemens on 08.03.2017.
 */
public class DataWriteResponse {

    @SerializedName("response")
    private String _response;

    public ResponseType getResponseType() {
        return ResponseType.getByValue(this._response);
    }

    public void set_response(String _response) {
        this._response = _response;
    }

    public String toString(){
        return String.format("Response: %s", this._response.toString());
    }
}
