package com.linemetrics.api.helper;

import com.google.gson.*;
import com.linemetrics.api.returntypes.ObjectBase;
import com.linemetrics.api.types.ResourceType;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemens on 08.03.2017.
 */
public class ObjectBaseDeserializer implements JsonDeserializer<ObjectBase> {

    private static final String CLASS_META_KEY = "object_type";

    @Override
    public ObjectBase deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext)
            throws JsonParseException {
        JsonObject jsonObj = jsonElement.getAsJsonObject();
        String className = jsonObj.get(CLASS_META_KEY).getAsString();
        Class<?> clz = ResourceType.getByValue(className).getClazz();
        return jsonDeserializationContext.deserialize(jsonElement, clz);
    }
}
