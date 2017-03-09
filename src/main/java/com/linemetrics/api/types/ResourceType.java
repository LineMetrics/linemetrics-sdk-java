package com.linemetrics.api.types;

import com.linemetrics.api.returntypes.Asset;
import com.linemetrics.api.returntypes.DataStream;
import com.linemetrics.api.returntypes.ObjectBase;
import com.linemetrics.api.returntypes.Property;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Klemens on 06.03.2017.
 */
public enum ResourceType {

    ASSET("object", Asset.class),
    PROPERTY("property", Property.class),
    DATASTREAM("attribute", DataStream.class);

    private ResourceType(String value, Class<? extends ObjectBase> clazz){
        this.clazz = clazz;
        this.value = value;
    }

    private String value;
    private Class<? extends ObjectBase> clazz;

    public String getValue() {
        return value;
    }

    public Class<? extends ObjectBase> getClazz() {
        return clazz;
    }

    public static ResourceType getByValue(String value){
        if(StringUtils.isNotEmpty(value)){
            for(ResourceType entry : values()) {
                if (entry.getValue().equalsIgnoreCase(value)) {
                    return entry;
                }
            }
        }
        return null;
    }
}
