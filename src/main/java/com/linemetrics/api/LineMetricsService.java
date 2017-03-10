package com.linemetrics.api;

import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.requesttypes.AssetRequest;
import com.linemetrics.api.requesttypes.DeleteObjectRequest;
import com.linemetrics.api.requesttypes.TemplateRequest;
import com.linemetrics.api.returntypes.Asset;
import com.linemetrics.api.returntypes.OAuth2Token;
import com.linemetrics.api.returntypes.ObjectBase;
import com.linemetrics.api.returntypes.Template;
import com.linemetrics.api.services.DataService;
import com.linemetrics.api.services.OAuth2Service;
import com.linemetrics.api.services.ObjectService;
import com.linemetrics.api.services.TemplateService;
import com.linemetrics.api.types.ResourceType;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Created by User on 03.03.2017.
 */
public class LineMetricsService implements ILMService {

    //services
    private OAuth2Service authenticationService;
    private DataService dataService;
    private ObjectService objectService;
    private TemplateService templateService;

    private OAuth2Token auth2Token = null;

    private String clientId = null;
    private String clientSecret = null;
    private String email = null;
    private String password = null;

    /**
     *
     * @param clientId
     * @param clientSecret
     * @throws ServiceException
     */
    public LineMetricsService(String clientId, String clientSecret) throws ServiceException {
        this.initialize();

        if (StringUtils.isEmpty(clientId)){
            throw new ServiceException("clientId must not be null or empty!");
        }

        if (StringUtils.isEmpty(clientSecret)) {
            throw new ServiceException("clientSecret must not be null or empty!");
        }

        this.clientId = clientId;
        this.clientSecret = clientSecret;

        this.authenticate(false);
    }

    /**
     *
     * @param clientId
     * @param clientSecret
     * @param email
     * @param password
     * @throws ServiceException
     */
    public LineMetricsService(String clientId, String clientSecret, String email, String password) throws ServiceException{
        this.initialize();

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

        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.email = email;
        this.password = password;

        this.authenticate(true);
    }

    private void initialize(){
        this.authenticationService = new OAuth2Service(this);
        this.dataService = new DataService(this);
        this.objectService = new ObjectService(this);
        this.templateService = new TemplateService(this);
    }

    @Override
    public ObjectBase loadObject(String id) throws ServiceException {
        return this.objectService.loadObject(this.auth2Token, new AssetRequest(id));
    }

    @Override
    public ObjectBase loadObject(String id, String alias) throws ServiceException {
        return this.objectService.loadObject(this.auth2Token, new AssetRequest(id , alias));
    }

    @Override
    public ObjectBase loadObjectByCustomKey(String customKey) throws ServiceException {
        return this.loadObject(customKey);
    }

    @Override
    public List<Asset> loadAssets() throws ServiceException {
        return this.objectService.loadRootAssets(this.auth2Token, new AssetRequest ());
    }

    @Override
    public List<ObjectBase> loadAssets(ResourceType type, String parentId) throws ServiceException {
        return this.objectService.loadObjects(this.auth2Token, new AssetRequest (parentId, null, type.getValue() ));
    }

    @Override
    public List<Template> loadTemplates() throws ServiceException {
        return templateService.loadTemplates(auth2Token, new TemplateRequest());
    }

    @Override
    public String deleteObject(ObjectBase obj, Boolean recursive) throws ServiceException {
        return objectService.delete(auth2Token, new DeleteObjectRequest(obj.getObjectId(), recursive));
    }

    @Override
    public void logout() {
        this.auth2Token = null;
    }

    @Override
    public void checkToken() throws ServiceException {
        throw new NotImplementedException("checkToken not implemented");
    }

    @Override
    public ObjectService getObjectService() {
        return this.objectService;
    }

    @Override
    public DataService getDataService() {
        return this.dataService;
    }

    @Override
    public TemplateService getTemplateService() {
        return this.templateService;
    }

    @Override
    public OAuth2Token getAuthenticationToken(){
        return this.auth2Token;
    }

    private void authenticate(Boolean passwordGrant) throws ServiceException {
        if (passwordGrant) {
            this.auth2Token = authenticationService.authenticate(clientId, clientSecret, email, password);
        } else {
            this.auth2Token  = authenticationService.authenticate(clientId, clientSecret);
        }
    }


}
