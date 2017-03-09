package com.linemetrics.api.returntypes;

import com.linemetrics.api.datatypes.Base;

/**
 * Created by Klemens on 08.03.2017.
 */
public class RawDataReadResponse extends DataReadResponse {

    private Base data;

    public RawDataReadResponse(Base data){
        this.data = data;
    }

    public Base getData() {
        return data;
    }

    public void setData(Base data) {
        this.data = data;
    }

    @Override
    public String toString(){
        return data!=null?data.toString():super.toString();
    }
}
