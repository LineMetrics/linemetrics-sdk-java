package com.linemetrics.api.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.linemetrics.api.ILMService;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.helper.ObjectBaseDeserializer;
import com.linemetrics.api.rest.RestClient;
import com.linemetrics.api.returntypes.ObjectBase;
import com.linemetrics.api.returntypes.Template;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Klemens on 03.03.2017.
 */
public abstract class BaseService {

    public static final String DEFAULT_API_REV = "v1";
    public static String apirev = DEFAULT_API_REV;

    public static int MAX_RETRIES = 720;
    public static int WAIT_MILLIS_AFTER_ERROR = 20 * 1000;

    public static int OPERATION_TIME_OUT = 10 * 1000;
    public static int CONNECTION_TIME_OUT = 10 * 1000;
    public static int SOCKET_TIME_OUT = 10 * 1000;
    public static int WAIT_BETWEEN_OPTIMIZE_QUERY = 1 * 1000;

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

    protected <T> T toObject(JSONObject json, Class<T> classOfT){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ObjectBase.class, new ObjectBaseDeserializer());
   //     builder.registerTypeAdapter(Base.class, new BaseTypeDeserializer());
        Gson gson = builder.create();
        return gson.fromJson(json.toJSONString(), classOfT);
    }

    protected <T> List<T> toObjectList(JSONArray json, Class<T> classOfT){
        final List<T> resultList = new ArrayList<>();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ObjectBase.class, new ObjectBaseDeserializer());
      //  builder.registerTypeAdapter(Base.class, new BaseTypeDeserializer());
        Gson gson = builder.create();
        for(int i=0;i<json.size();i++){
            resultList.add(gson.fromJson(((JSONObject)json.get(i)).toJSONString(), classOfT));
        }
        return resultList;
    }


    protected String toJsonString(Object object){
        final String result = new Gson().toJson(object);
        System.out.println("toJsonString: "+result);
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

    public Object loadObjectFromDictionary(Map<String, Object> data, Class<?> type){
        Object result = null;
        try {
            result = type.newInstance();
            for(Field field  : type.getDeclaredFields()){
                if (field.isAnnotationPresent(SerializedName.class)){
                    System.out.println("Name: "+field.getName());
                }
            }

        } catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

   /* internal T LoadObjectFromDictionary<T>(Dictionary<string, object> data) where T : ObjectBase
    {
        return LoadObjectFromDictionary(data, typeof(T)) as T;
    }



    internal object LoadObjectFromDictionary(Dictionary<string, object> data, Type targetType)
    {
        // TODO check if type is assetbase?

        var instance = Activator.CreateInstance(targetType);

        foreach (var propInfo in targetType.GetProperties(BindingFlags.Public | BindingFlags.Instance))
        {
            DataMemberAttribute attr = (DataMemberAttribute)propInfo.GetCustomAttributes(typeof(DataMemberAttribute), false).FirstOrDefault();

            if (attr != null && data.ContainsKey(attr.Name))
            {
                propInfo.SetValue(instance, data[attr.Name], null);
            }
        }

        foreach (var fieldInfo in targetType.GetFields(BindingFlags.NonPublic | BindingFlags.Instance))
        {
            DataMemberAttribute attr = (DataMemberAttribute)fieldInfo.GetCustomAttributes(typeof(DataMemberAttribute), false).FirstOrDefault();

            if (attr != null && data.ContainsKey(attr.Name))
            {
                fieldInfo.SetValue(instance, data[attr.Name]);
            }
        }

        return instance;
    } */

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
