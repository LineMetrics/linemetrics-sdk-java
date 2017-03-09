package com.linemetrics.api;

import com.linemetrics.api.datatypes.Base;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.*;
import com.linemetrics.api.returntypes.*;
import com.linemetrics.api.services.DataService;
import com.linemetrics.api.services.ObjectService;
import com.linemetrics.api.services.TemplateService;
import com.linemetrics.api.types.ResourceType;

import java.util.List;
import java.util.Map;

/**
 * Created by User on 03.03.2017.
 */
public interface ILMService {

    public ObjectBase loadObject(String id) throws ServiceException;
    public ObjectBase loadObject(String id, String alias) throws ServiceException;
    public ObjectBase loadObjectByCustomKey(String customKey) throws ServiceException;

    public List<Asset> loadAssets() throws ServiceException, AuthorizationException;
    public List<ObjectBase> loadAssets(ResourceType type, String parentId) throws ServiceException;

    public List<Template> loadTemplates() throws ServiceException, AuthorizationException;

    public String deleteObject(ObjectBase obj, Boolean recursive) throws ServiceException;

    public void logout();
    public void checkToken() throws ServiceException;

    public ObjectService getObjectService();
    public DataService getDataService();
    public TemplateService getTemplateService();
    public OAuth2Token getAuthenticationToken();
}
