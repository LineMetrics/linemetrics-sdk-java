package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.*;
import com.linemetrics.api.types.FunctionType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Klemens on 06.03.2017.
 */
public class DataStream extends ObjectBase {

    @SerializedName("alias")
    private String alias;

    @SerializedName("input")
    private String inputId;

    private DataStreamType dataType;


    @Override
    public String getId() {
        return StringUtils.isNotEmpty(alias)?alias:getObjectId();
    }

    @Override
    public void save() throws ServiceException {
        UpdateObjectRequest request = new UpdateObjectRequest(getObjectId(), new UpdateData(null, getAlias(), getTitle(), getParentId()));
        this.getServiceInstance().getObjectService().update(getServiceInstance().getAuthenticationToken(), request);
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public void setDataType(DataStreamType dataType) {
        this.dataType = dataType;
    }

    public DataStreamType getDataType() throws ServiceException {
        final DataStreamConfigRequest request = new DataStreamConfigRequest(this.getParentId(), getAlias(), this.getObjectId());
        if(this.dataType == null){
            this.dataType = this.getServiceInstance().getDataService().loadDataStreamConfig(this.getServiceInstance().getAuthenticationToken(), request);
        }
        return this.dataType;
    }

    public List<DataReadResponse> loadData(Date from, Date to, String timeZone, String granularity, FunctionType function) throws ServiceException{
        if(function == null){
            function = FunctionType.RAW;
        }
        final DataReadRequest request = new DataReadRequest(getParentId(), getAlias(), getObjectId(), getDataType().getOutput(), from, to, timeZone, granularity, function);
        return this.getServiceInstance().getDataService().read(getServiceInstance().getAuthenticationToken(), request);
    }

    public Base loadLastValue() throws ServiceException {
        LastValueDataReadRequest request = new LastValueDataReadRequest(getParentId(), getId(), getObjectId(), getDataType().getOutput());
        return getServiceInstance().getDataService().readLastValue(getServiceInstance().getAuthenticationToken(), request);
    }

    public List<DataWriteResponse> saveData(List<Base> data) throws ServiceException {
        DataWriteRequest request = new DataWriteRequest(getObjectId());
        for(Base entry : data){
            if(!entry.getClass().getName().equals(getDataType().getInput().getName().toString())){
                throw new ServiceException(String.format("Input type: %s was expected, given %s !", getDataType().getInput().getName().toString(), entry.getClass().getName().toString()));
            }
            request.getPayload().add(entry);
        }
        return getServiceInstance().getDataService().write(getServiceInstance().getAuthenticationToken(), request);
    }


    public DataWriteResponse saveData(Base data) throws ServiceException {
        final List<Base> list = new ArrayList<>();
        list.add(data);
        final List<DataWriteResponse> resultList = this.saveData(list);
        return (resultList!=null&&resultList.size()>0)?resultList.get(0):null;
    }

    @Override
    public String toString(){
        return super.toString() + String.format(", Alias: %s, InputId: %s", this.alias, this.inputId);
    }

}
