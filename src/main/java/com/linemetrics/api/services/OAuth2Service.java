package com.linemetrics.api.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.linemetrics.api.LineMetricsService;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.returntypes.OAuth2Token;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Klemens on 06.03.2017.
 */
public class OAuth2Service extends BaseService {

    private static final Logger logger = LoggerFactory.getLogger(OAuth2Service.class);

    public OAuth2Service(LineMetricsService serviceInstance){
        super(serviceInstance);
    }

    /**
     *
     * @param clientId
     * @param clientSecret
     * @return
     * @throws ServiceException
     */
    public OAuth2Token authenticate(String clientId, String clientSecret) throws ServiceException {

        logger.debug(String.format("Call authenticate with clientId: %s, clientSecret: %s", clientId, clientSecret));

        try {
            //validate
            if (StringUtils.isEmpty(clientId)){
                throw new IllegalArgumentException("clientId must not be null or empty!");
            }
            if (StringUtils.isEmpty(clientSecret)) {
                throw new IllegalArgumentException("clientSecret must not be null or empty!");
            }

            //build the request
            URIBuilder ub = new URIBuilder(this.baseUrl).setPath("/oauth/access_token");

            List<NameValuePair> params = new ArrayList<NameValuePair>(3);
            params.add(new BasicNameValuePair("client_id", clientId));
            params.add(new BasicNameValuePair("client_secret", clientSecret));
            params.add(new BasicNameValuePair("grant_type", "client_credentials"));

            //return
            JsonElement result = this.restClient.post(ub.build(), false, new UrlEncodedFormEntity(params, "UTF-8"));
            return toObject((JsonObject)result, OAuth2Token.class);

        } catch(Exception e){
            this.handleException(e);
        }
        return null;
    }

    /**
     *
     * @param clientId
     * @param clientSecret
     * @param email
     * @param password
     * @return
     * @throws ServiceException
     */
    public OAuth2Token authenticate(String clientId, String clientSecret, String email, String password) throws ServiceException {

        logger.debug(String.format("Call authenticate with clientId: %s, clientSecret: %s, email: %s, password: %s", clientId, clientSecret, email, password));

        try {
            if (StringUtils.isEmpty(clientId)){
                throw new IllegalArgumentException("clientId must not be null or empty!");
            }
            if (StringUtils.isEmpty(clientSecret)) {
                throw new IllegalArgumentException("clientSecret must not be null or empty!");
            }
            if (StringUtils.isEmpty(email)){
                throw new IllegalArgumentException("email must not be null or empty!");
            }
            if (StringUtils.isEmpty(password)) {
                throw new IllegalArgumentException("password must not be null or empty!");
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
            JsonElement result = this.restClient.post(ub.build(), false, new UrlEncodedFormEntity(params, "UTF-8"));
            return toObject((JsonObject)result, OAuth2Token.class);

        } catch(Exception e){
            this.handleException(e);
        }
        return null;
    }
}
