package com.linemetrics.api.helper;

import com.linemetrics.api.exceptions.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.net.URI;

/**
 * Created by Klemens on 07.03.2017.
 */
public class URIHelper {

    public static URI buildDefaultURI(final URI baseUri, final String uriPath, final String objectId, final String customKey, final String alias, final String uriPathAppendix) throws ServiceException{
        URI result = null;
        if(baseUri != null){
            final StringBuilder builder = new StringBuilder(uriPath);
            if(StringUtils.isNotEmpty(objectId)){
                builder.append("/");
                builder.append(objectId);
            } else {
                builder.append(URIHelper.buildCustomKeyAndAlias(customKey, alias));
            }

            if(StringUtils.isNotEmpty(uriPathAppendix)){
                builder.append("/");
                builder.append(uriPathAppendix);
            }

            final URIBuilder ub = new URIBuilder(baseUri);
            ub.setPath(ub.getPath() + builder.toString());

            try {
                result = ub.build();
            } catch(Exception e){
                e.printStackTrace();
                throw new ServiceException("Invalid URL "+e.getMessage());
            }
        }
        return result;
    }

    private static String buildCustomKeyAndAlias(final String customKey, final String alias) throws ServiceException{
        final StringBuilder builder = new StringBuilder();

        if(StringUtils.isEmpty(customKey) && StringUtils.isNotEmpty(alias)){
            throw new ServiceException("No CustomKey given, can not resolve Alias");
        }
        if(StringUtils.isNotEmpty(customKey)){
            builder.append("/");
            builder.append(customKey);
        }

        if(StringUtils.isNotEmpty(alias)){
            builder.append("/");
            builder.append(alias);
        }

        return builder.toString();
    }
}
