package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.*;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

/**
 * Created by Klemens on 06.03.2017.
 */
public class Property extends ObjectBase {

    @SerializedName(value = "alias")
    private String alias;

    @SerializedName(value = "data")
    private Map<String, Object> dataDictionary;

    private DataStreamType dataType;

    private Base value;

    public Base getValue(){
        if(this.dataDictionary == null){
            LastValueDataReadRequest request = new LastValueDataReadRequest(getDataType().getOutput(), this.getObjectId());
            try {
                this.value = getServiceInstance().getDataService().readLastValue(getServiceInstance().getAuthenticationToken(), request);
            } catch(Exception e){
                e.printStackTrace();
            }
        } else {
            if(value == null){
                //TODO Klemens, fragen wo "data" herkommen kann und was das ist
                throw new NotImplementedException("loadObjectFromDictionary not implemented");
                //this.value = (Base)getServiceInstance().getDataService().loadObjectFromDictionary(this.dataDictionary, getDataType().getOutput());
            }
        }
        return value;
    }

    public void setValue(Base value) throws ServiceException {
        if(!value.getClass().equals(getDataType().getInput().getClass())){
            throw new ServiceException(String.format("Input type: %s was expected, given %s !", getDataType().getInput().getName().toString(), value.getClass().getName().toString()));
        }
        this.value = value;
        DataWriteRequest request = new DataWriteRequest(getObjectId());
        request.getPayload().add(value);
        getServiceInstance().getDataService().write(getServiceInstance().getAuthenticationToken(), request);

    }

    public DataStreamType getDataType(){
        final DataStreamConfigRequest request = new DataStreamConfigRequest(this.getParentId(), getAlias(), this.getObjectId());
        if(this.dataType == null){
            try {
                this.dataType = this.getServiceInstance().getDataService().loadDataStreamConfig(this.getServiceInstance().getAuthenticationToken(), request);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
        return this.dataType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, Object> getDataDictionary() {
        return dataDictionary;
    }

    public void setDataDictionary(Map<String, Object> dataDictionary) {
        this.dataDictionary = dataDictionary;
    }

    @Override
    public String getId() {
        return org.apache.commons.lang3.StringUtils.isNotEmpty(alias)?alias:getObjectId();
    }

    @Override
    public String toString(){
        return super.toString() + String.format(", Alias: %s, Data: %s", getId(), getValue());
    }

    @Override
    public void save() throws ServiceException {
        UpdateObjectRequest request = new UpdateObjectRequest(getObjectId(), new UpdateData(null, alias, getTitle(), getParentId()));
        getServiceInstance().getObjectService().update(getServiceInstance().getAuthenticationToken(), request);
    }
}
