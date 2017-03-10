package com.linemetrics.api.returntypes;

import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.UpdateData;
import com.linemetrics.api.requesttypes.UpdateObjectRequest;
import com.linemetrics.api.types.ResourceType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 03.03.2017.
 */
public class Asset extends ObjectBase {

    @SerializedName("custom_key")
    private String customKey;

    @SerializedName("children_info")
    private Map<String, Object> childrenInfo;

    /**
     *
     * @return
     * @throws ServiceException
     */
    public List<Asset> loadAssets() throws ServiceException {
        final List<Asset> assets = (List<Asset>)(List<?>)this.getServiceInstance().loadAssets(ResourceType.ASSET, this.getObjectId());
        this.setParentToList(assets);
        return assets;
    }

    /**
     *
     * @return
     * @throws ServiceException
     */
    public List<Property> loadProperties() throws ServiceException {
        final List<Property> properties = (List<Property>)(List<?>)this.getServiceInstance().loadAssets(ResourceType.PROPERTY, this.getObjectId());
        this.setParentToList(properties);
        return properties;
    }

    /**
     *
     * @return
     * @throws ServiceException
     */
    public List<DataStream> loadDataStreams() throws ServiceException {
        final List<DataStream> streams = (List<DataStream>)(List<?>)this.getServiceInstance().loadAssets(ResourceType.DATASTREAM, this.getObjectId());
        this.setParentToList(streams);
        return streams;
    }

    /**
     * saves object
     */
    public void save() throws ServiceException {
        final UpdateObjectRequest request = new UpdateObjectRequest(this.getObjectId(), new UpdateData(this.customKey, null, getTitle(), getParentId()));
        request.getData().setIcon(getIcon());
        this.getServiceInstance().getObjectService().update(getServiceInstance().getAuthenticationToken(), request);
    }

    private void setParentToList(final List<?> lList){
        if(lList != null && lList.size() > 0){
            for(final ObjectBase entry : (List<ObjectBase>)lList){
                entry.setParent(this);
            }
        }
    }

    public String getCustomKey() {
        return customKey;
    }

    public void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    public Map<String, Object> getChildrenInfo() {
        return childrenInfo;
    }

    public void setChildrenInfo(Map<String, Object> childrenInfo) {
        this.childrenInfo = childrenInfo;
    }

    public String getImage() {
        if(this.getPayload() != null && this.getPayload().containsKey("image")){
            return (String)this.getPayload().get("image");
        }
        return null;
    }

    @Override
    public String getId(){
        if(StringUtils.isNotEmpty(customKey)){
            return customKey;
        } else {
            return getObjectId();
        }
    }

    @Override
    public String toString(){
        return String.format("Id: %s, Type: %s, CustomKey: %s, TemplateId: %s, ParentId: %s, Title: %s, Icon: %s, Image: %s", getId(), getType(), customKey, getTemplateId(), getParentId(), getTitle(), getIcon(), getImage());
    }

}
