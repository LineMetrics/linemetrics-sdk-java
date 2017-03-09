package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.ILMService;
import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.CreateAssetRequest;
import com.linemetrics.api.requesttypes.TemplateRequiredFieldsRequest;

import javax.sql.rowset.serial.SerialException;
import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 06.03.2017.
 */
public class Template {

    private ILMService serviceInstance = null;

    private List<TemplateRequiredFields> requiredFields;

    @SerializedName("uid")
    private String id;

    @SerializedName("name")
    private String name;

    public List<TemplateRequiredFields> getRequiredFields() {
        if(requiredFields == null){
            try {
                requiredFields = serviceInstance.getTemplateService().loadRequiredFields(serviceInstance.getAuthenticationToken(), new TemplateRequiredFieldsRequest(getId()));
            } catch(Exception e){}
        }
        return requiredFields;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String createAsset(Map<String, Base> data) throws ServiceException {
        if((this.requiredFields==null?0:this.requiredFields.size()) != data.size()){
            throw new ServiceException("Please provide data for all required fields!");
        }
        CreateAssetRequest request = new CreateAssetRequest();
        request.setTemplateId(this.id);
        request.getPayload().putAll(data);
        return serviceInstance.getTemplateService().createAsset(serviceInstance.getAuthenticationToken(), request);
    }

    public String toString(){
        return String.format("Id: %s, Name: %s", id, name);
    }

    public void setServiceInstance(ILMService serviceInstance) {
        this.serviceInstance = serviceInstance;
    }
}
