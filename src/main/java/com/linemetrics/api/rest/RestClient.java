/**
 * Copyright (c) 2015 by LineMetrics GmbH
 * Author: Thomas Pillmayr <t.pillmayr@linemetrics.com>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package com.linemetrics.api.rest;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.linemetrics.api.exceptions.RestException;
import com.linemetrics.api.returntypes.OAuth2Token;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class RestClient {

    private static final Logger logger = LoggerFactory.getLogger(RestClient.class);

    private HttpClient httpClient = null;
    private URI uri = null;
    private OAuth2Token creds = null;

    public RestClient(HttpClient httpclient) {
        this.httpClient = httpclient;
        this.uri = null;
    }

    public RestClient(HttpClient httpclient, URI uri) {
        this.httpClient = httpclient;
        this.uri = uri;
    }

    private JsonElement request(HttpRequestBase req, Boolean attachAuthHeader) throws RestException {
        logger.debug(String.format("Call request with URL: %s", req.getURI().toString()));
        req.addHeader("Accept", "application/json");

        if (creds != null && attachAuthHeader) {
            creds.authenticate(req);
        }

        StringBuilder result = new StringBuilder();

        try {
            HttpResponse resp = httpClient.execute(req);

            HttpEntity ent = resp.getEntity();

            if (ent != null) {
                String encoding = null;
                if (ent.getContentEncoding() != null) {
                    encoding = ent.getContentEncoding().getValue();
                }

                if (encoding == null) {
                    Header contentTypeHeader = resp.getFirstHeader("content-type");
                    HeaderElement[] contentTypeElements = contentTypeHeader.getElements();
                    for (HeaderElement he : contentTypeElements) {
                        NameValuePair nvp = he.getParameterByName("charset");
                        if (nvp != null) {
                            encoding = nvp.getValue();
                        }
                    }
                }

                InputStreamReader isr = encoding != null ?
                    new InputStreamReader(ent.getContent(), encoding) :
                    new InputStreamReader(ent.getContent());
                BufferedReader br = new BufferedReader(isr);
                String line = "";

                while ((line = br.readLine()) != null)
                    result.append(line);
            }

            StatusLine sl = resp.getStatusLine();

            if (sl.getStatusCode() >= 300) {
                throw new RestException(sl.getReasonPhrase(), sl.getStatusCode(), result.toString());
            }
        } catch(Exception e) {
            if(! (e instanceof RestException)){
                throw new RestException(e.getMessage(), -1, "");
            } else {
                throw (RestException)e;
            }
        } finally {
            req.releaseConnection();
        }

        logger.debug(String.format("Finish request with result: %s", result.toString()));

        return new Gson().fromJson((!result.toString().isEmpty() ? result.toString() : "{\"data\":[]}"), JsonElement.class);
    }

    public JsonElement get(URI uri) throws RestException {
        return get(uri, true);
    }

    public JsonElement get(URI uri, boolean attachAuthHeader) throws RestException {
        return request(new HttpGet(uri), attachAuthHeader);
    }

    public JsonElement post(URI uri, boolean attachAuthHeader, HttpEntity entity) throws RestException{
        HttpPost post = new HttpPost(uri);
        post.setEntity(entity);
        return request(post, attachAuthHeader);
    }

    public JsonElement delete(URI uri, boolean attachAuthHeader) throws RestException{
        HttpDelete delete = new HttpDelete(uri);
        return request(delete, attachAuthHeader);
    }

    public JsonElement post(URI uri, HttpEntity entity)throws RestException{
        return post(uri, true, entity);
    }

    public void setCreds(OAuth2Token creds) {
        this.creds = creds;
    }
}

