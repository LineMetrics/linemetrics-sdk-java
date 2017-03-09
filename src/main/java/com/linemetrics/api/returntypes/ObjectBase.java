package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.ILMService;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.ServiceException;
import org.apache.commons.lang3.NotImplementedException;

import java.util.Map;

/**
 * Created by Klemens on 03.03.2017.
 */
public abstract class ObjectBase {

    private ILMService serviceInstance;

    private Asset parent;

    @SerializedName("object_id")
    private String objectId;

    @SerializedName("object_type")
    private String type;

    @SerializedName("template_id")
    private String templateId;

    @SerializedName("parent_id")
    private String parentId;

    @SerializedName("payload")
    private Map<String, Object> payload;

    public ILMService getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ILMService serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public Asset getParent() {
        return parent;
    }

    public void setParent(Asset parent) {
        this.parent = parent;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }

    public abstract String getId();

    public abstract void save() throws ServiceException, AuthorizationException;

    public String getTitle() {
        if(this.payload != null && this.payload.containsKey("title")){
            return (String)this.payload.get("title");
        }
        return null;
    }

    public void setTitle(String value){
        if (this.payload != null) {
            this.payload.put("title", value);
        }
    }

    public String getIcon(){
        if(this.payload != null && this.payload.containsKey("icon")){
            return (String)this.payload.get("icon");
        }
        return null;
    }

    public void setIcon(String value){
        throw new NotImplementedException("Icon can not be updated");
      /*  if (this.payload != null) {
            this.payload.put("icon", value);
        } */
    }

    public String toString(){
        return String.format("Id: %s", getId());
    }
}
