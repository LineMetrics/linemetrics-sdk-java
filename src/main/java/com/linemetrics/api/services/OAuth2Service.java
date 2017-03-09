package com.linemetrics.api.services;

import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.returntypes.OAuth2Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.simple.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemens on 06.03.2017.
 */
public class OAuth2Service extends BaseService {

    public OAuth2Service(LineMetricsService serviceInstance){
        super(serviceInstance);
    }

    public OAuth2Token authenticate(String clientId, String clientSecret) throws ServiceException {
        try {
            //validate
            if (StringUtils.isEmpty(clientId)){
                throw new ServiceException("clientId must not be null or empty!");
            }
            if (StringUtils.isEmpty(clientSecret)) {
                throw new ServiceException("clientSecret must not be null or empty!");
            }

            //build the request
            URIBuilder ub = new URIBuilder(this.baseUrl).setPath("/oauth/access_token");

            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));

            //return
            JSONObject result = (JSONObject)this.restClient.post(ub.build(), false, new UrlEncodedFormEntity(params, "UTF-8"));
            return toObject(result, OAuth2Token.class);

        } catch(Exception e){
            this.handleException(e);
        }
        return null;
    }

    public OAuth2Token authenticate(String clientId, String clientSecret, String email, String password) throws ServiceException {
        try {
            if (StringUtils.isEmpty(clientId)){
                throw new ServiceException("clientId must not be null or empty!");
            }
            if (StringUtils.isEmpty(clientSecret)) {
                throw new ServiceException("clientSecret must not be null or empty!");
            }
            if (StringUtils.isEmpty(email)){
                throw new ServiceException("email must not be null or empty!");
            }
            if (StringUtils.isEmpty(password)) {
                throw new ServiceException("password must not be null or empty!");
            }

            //build the request
            URIBuilder ub = new URIBuilder(this.baseUrl).setPath("/oauth/access_token");

            List<NameValuePair> params = new ArrayList<NameValuePair>(5);
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));

            //return
            JSONObject result = (JSONObject)this.restClient.post(ub.build(), false, new UrlEncodedFormEntity(params, "UTF-8"));
            System.out.println(result.toString());
            return toObject(result, OAuth2Token.class);

        } catch(Exception e){
            this.handleException(e);
        }
        return null;
    }
}
