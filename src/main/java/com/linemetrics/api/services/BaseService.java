package com.linemetrics.api.services;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.ILMService;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.ObjectBaseDeserializer;
import com.linemetrics.api.rest.RestClient;
import com.linemetrics.api.returntypes.ObjectBase;
import com.linemetrics.api.returntypes.Template;
import com.sun.org.apache.xerces.internal.util.ShadowedSymbolTable;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 03.03.2017.
 */
public abstract class BaseService {

    public static final String DEFAULT_API_REV = "v1";
    public static String apirev = DEFAULT_API_REV;

    public static int OPERATION_TIME_OUT = 10 * 1000;
    public static int CONNECTION_TIME_OUT = 10 * 1000;
    public static int SOCKET_TIME_OUT = 10 * 1000;

    protected RestClient restClient = null;
    protected static final URI baseUrl =  URI.create("https://lm3api.linemetrics.com");
    protected ILMService serviceInstance = null;

    public BaseService(LineMetricsService serviceInstance){

        this.serviceInstance = serviceInstance;

        RequestConfig requestConfig =
                RequestConfig.custom()
                        .setConnectTimeout(OPERATION_TIME_OUT)
                        .setConnectionRequestTimeout(CONNECTION_TIME_OUT)
                        .setSocketTimeout(SOCKET_TIME_OUT)
                        .build();

        HttpClient httpclient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
        restClient = new RestClient(httpclient);
    }

    protected <T> T toObject(JsonObject json, Class<T> classOfT){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ObjectBase.class, new ObjectBaseDeserializer());
        Gson gson = builder.create();
        return gson.fromJson(json.toString(), classOfT);
    }

    protected <T> List<T> toObjectList(JsonArray json, Class<T> classOfT){
        final List<T> resultList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ObjectBase.class, new ObjectBaseDeserializer());
        Gson gson = builder.create();
        for(int i=0;i<json.size();i++){
            resultList.add(gson.fromJson(((JsonObject)json.get(i)).toString(), classOfT));
        }

        return resultList;
    }


    protected String toJsonString(Object object){
        final String result = new Gson().toJson(object);
        return result;
    }

    /**
     *
     * @param lList
     * @return
     */
    protected List<ObjectBase> addServiceInstance(final List<ObjectBase> lList){
        if(lList != null){
            for(final ObjectBase entry : lList){
                entry.setServiceInstance(this.serviceInstance);
            }
        }
        return lList;
    }

    protected List<Template> addTemplateServiceInstance(final List<Template> lList){
        if(lList != null){
            for(final Template entry : lList){
                entry.setServiceInstance(this.serviceInstance);
            }
        }
        return lList;
    }


    /**
     *
     * @param objectBase
     * @return
     */
    protected ObjectBase addServiceInstance(final ObjectBase objectBase){
        if(objectBase != null){
            objectBase.setServiceInstance(this.serviceInstance);
        }
        return objectBase;
    }

    /**
     *
     * @param data
     * @param type
     * @return
     */
    public Object loadObjectFromDictionary(Map<String, Object> data, Class<?> type) {
        Object result = null;
        try {
            result = type.newInstance();
            List<Field> fields = new ArrayList<>();
            fields.addAll(Arrays.asList(type.getDeclaredFields()));
            //read fields of super class
            if(type.getSuperclass() != null){
                fields.addAll(Arrays.asList(type.getSuperclass().getDeclaredFields()));
            }
            for(Field field  : fields){
                if (field.isAnnotationPresent(SerializedName.class)){
                    SerializedName annotation = field.getAnnotation(SerializedName.class);
                    String serializationValue = annotation!=null?annotation.value():null;
                    if(StringUtils.isNotEmpty(serializationValue)){
                        if(data.containsKey(serializationValue)){

                            //find setter for field
                            Method setter = type.getMethod(getSetterName(field.getName()), field.getType());
                            if(setter != null && data.get(serializationValue) != null){

                                //special handling for 'ts' field, Timestamp comes as String but should be set as Long
                                if(StringUtils.isNotEmpty(serializationValue) && serializationValue.equalsIgnoreCase("ts")){
                                    //set property ts
                                    setter.invoke(result, Long.valueOf((String)data.get(serializationValue)));
                                } else {
                                    //set property
                                    setter.invoke(result, data.get(serializationValue));
                                }

                            }
                        }
                    }
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    private String getSetterName(String fieldName){
        String result = null;
        if(StringUtils.isNotEmpty(fieldName) && fieldName.length() > 0){
            result = "set"+fieldName.replace(fieldName.substring(0,1), fieldName.substring(0,1).toUpperCase());
        }
        return result;
    }

   protected void handleException(Exception e) throws ServiceException {
        if(e instanceof RestException){
            if(((RestException) e).getHttpStatusCode() == 401){
                throw new AuthorizationException((RestException) e);
            } else {
                throw new ServiceException(((RestException) e).getMessage());
            }
        } else if(e instanceof IllegalArgumentException){
            throw (IllegalArgumentException)e;
        } else if(e instanceof NotImplementedException){
            throw (NotImplementedException)e;
        } else {
            throw new ServiceException(e.getMessage());
        }
   }
}
