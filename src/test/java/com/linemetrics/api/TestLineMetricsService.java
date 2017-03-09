package com.linemetrics.api;

import com.linemetrics.api.datatypes.*;
import com.linemetrics.api.datatypes.Double;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.returntypes.*;
import com.linemetrics.api.types.FunctionType;
import com.linemetrics.api.types.ResourceType;
import com.linemetrics.api.types.ResponseType;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.String;
import java.util.*;

/**
 * Created by Klemens on 06.03.2017.
 */
public class TestLineMetricsService {

    private final String VALID_CLIENTID = "api_57251703d60";
    private final String VALID_CLIENTSECRET = "174c3423821f0982910c17b9f5c3faa1";

    private final String VALID_OBJECT_ID = "fdf2a5a1337942dab854eaf0ee4dec01";
    private final String VALID_OBJECT_ALIAS = "luftfeuchte";
    private final String VALID_CUSTOMKEY = "gebaeude_a";

    @Test(expected = AuthorizationException.class)
    public void testInstantiate_Invalid() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService("testClientId", "testClientSecret");
    }

    @Test(expected = ServiceException.class)
    public void testInstantiate_Invalid_1() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService(null, "testClientSecret");
    }

    @Test(expected = ServiceException.class)
    public void testInstantiate_Invalid_2() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService("testClientId", null);
    }

    @Test
    public void testInstantiate() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        Assert.assertNotNull(service.getAuthenticationToken());
        Assert.assertNotNull(service.getAuthenticationToken().getAccessToken());
        Assert.assertNotNull(service.getAuthenticationToken().getTokenType());
        Assert.assertNotNull(service.getAuthenticationToken().getExpiresIn());
        System.out.println(service.getAuthenticationToken().toString());
    }

    @Test
    public void testLoadAssets() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final List<Asset> assets = service.loadAssets();
        Assert.assertNotNull(assets);
        Assert.assertTrue(assets.size() > 0);
        for(final Asset entry : assets){
            System.out.println(entry.toString());
            Assert.assertNotNull(entry.getPayload());
            Assert.assertNotNull(entry.getChildrenInfo());
            Assert.assertTrue(entry.getChildrenInfo().size() > 0);
        }
    }

    @Test
    public void testLoadObjectById() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);
    }

    @Test(expected = ServiceException.class)
    public void testLoadObjectByIdAndAlias_Invalid() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID, "WRONG");
        Assert.assertNull(base);
        service.logout();
    }

    @Test
    public void testLoadObjectByIdAndAlias() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID, VALID_OBJECT_ALIAS);
        System.out.println(base.toString());
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getPayload());
        Assert.assertNotNull(base.getParentId());
    }

    @Test
    public void testLoadObjectByCustomKey() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObjectByCustomKey(VALID_CUSTOMKEY);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getPayload());
    }

    @Test(expected = ServiceException.class)
    public void testLoadObjectByCustomKey_Invalid() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObjectByCustomKey("asdf");
        Assert.assertNull(base);
    }

    @Test
    public void testLoadAssetsByTypeAndId_Datastream() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final List<ObjectBase> result = service.loadAssets(ResourceType.DATASTREAM, VALID_OBJECT_ID);
        Assert.assertNotNull(result);
        Assert.assertTrue(result.size() > 0);
        for(final ObjectBase entry : result){
            System.out.println(entry.toString());
            Assert.assertNotNull(entry.getType());
            Assert.assertEquals(entry.getType(), ResourceType.DATASTREAM.getValue());
        }
    }

    @Test
    public void testLoadAssetsOfParentObject() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);
        Assert.assertTrue(base instanceof Asset);
        final List<Asset> children = ((Asset)base).loadAssets();
        Assert.assertNotNull(children);
        Assert.assertTrue(children.size() > 0);

        for(Asset entry : children){
            System.out.println(entry.toString());
        }
    }

    @Test
    public void testLoadPropertiesOfParentObject() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);
        Assert.assertTrue(base instanceof Asset);
        final List<Property> children = ((Asset)base).loadProperties();
        Assert.assertNotNull(children);
        Assert.assertTrue(children.size() > 0);

        for(Property entry : children){
            System.out.println(entry.toString());
        }
    }

    @Test
    public void testLoadDatastreamOfParentObject() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);
        Assert.assertTrue(base instanceof Asset);
        final List<DataStream> children = ((Asset)base).loadDataStreams();
        Assert.assertNotNull(children);
        Assert.assertTrue(children.size() > 0);

        for(DataStream entry : children){
            System.out.println(entry.toString());
        }
    }

    @Test
    public void testLoadDatastreamRaw() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertTrue(base instanceof Asset);
        final List<DataStream> children = ((Asset)base).loadDataStreams();
        Assert.assertNotNull(children);
        Assert.assertTrue(children.size() > 0);

        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -2);


        for(DataStream entry : children){
            final List<DataReadResponse> stream = entry.loadData(from.getTime(), Calendar.getInstance().getTime(), "", null, FunctionType.RAW );

            Assert.assertTrue(stream != null && stream.size() > 0);

            for(DataReadResponse entry1 : stream){
                RawDataReadResponse r = ((RawDataReadResponse)entry1);
                if(r.getData() instanceof DoubleAverage){
                    Assert.assertNotNull(((DoubleAverage)r.getData()).getMaximum());
                    Assert.assertNotNull(((DoubleAverage)r.getData()).getMinimum());
                    Assert.assertNotNull(((DoubleAverage)r.getData()).getValue());
                    System.out.println(((DoubleAverage)r.getData()).toString());
                } else if(r.getData() instanceof Double){
                    Assert.assertNotNull(((Double)r.getData()).getValue());
                } else if(r.getData() instanceof GeoAddress){
                    Assert.assertNotNull(((GeoAddress)r.getData()).getAddress());
                } else if(r.getData() instanceof GeoCoord){
                    Assert.assertNotNull(((GeoCoord)r.getData()).getLatitude());
                    Assert.assertNotNull(((GeoCoord)r.getData()).getLongitude());
                    Assert.assertNotNull(((GeoCoord)r.getData()).getPrecision());
                } else if(r.getData() instanceof com.linemetrics.api.datatypes.String){
                    Assert.assertNotNull(((com.linemetrics.api.datatypes.String)r.getData()).getValue());
                } else if(r.getData() instanceof Timestamp){
                    Assert.assertNotNull(((Timestamp)r.getData()).getValue());
                }
            }
        }
    }

    @Test
    public void testLoadDatastreamAverage() throws ServiceException, AuthorizationException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertTrue(base instanceof Asset);
        final List<DataStream> children = ((Asset)base).loadDataStreams();
        Assert.assertNotNull(children);
        Assert.assertTrue(children.size() > 0);

        Calendar from = Calendar.getInstance();
        from.add(Calendar.DATE, -2);

        for(DataStream entry : children){
            final List<DataReadResponse> stream = entry.loadData(from.getTime(), Calendar.getInstance().getTime(), "", null, FunctionType.AVERAGE );
            if(stream != null){
                for(DataReadResponse entry1 : stream){
                    AggregatedDataReadResponse r = ((AggregatedDataReadResponse)entry1);
                    Assert.assertNotNull(r.toString());
                    System.out.println(r.toString());
                }
            }
        }
    }

    @Test
    public void testUpdateObjectById() throws ServiceException, AuthorizationException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);

        final String oldTitle = base.getTitle();

        base.setTitle("Testtitel14");
        base.save();

        final ObjectBase newbase = service.loadObject(VALID_OBJECT_ID);
        Assert.assertEquals(newbase.getTitle(), "Testtitel14");

        newbase.setTitle(oldTitle);
        newbase.save();
    }

    @Test
    public void testLoadTemplates() throws ServiceException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        final List<Template> templates = service.loadTemplates();
        Assert.assertTrue(templates != null && templates.size() > 0);
        for(final Template entry : templates){
            System.out.println(entry.toString());
            List<TemplateRequiredFields> fields = entry.getRequiredFields();
            if(fields != null && fields.size() > 0){
                for(final TemplateRequiredFields field : fields){
                    System.out.println(field.toString());
                }
            }
        }
    }

    @Test
    public void createObjectByTemplate() throws ServiceException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        final List<Template> templates = service.loadTemplates();
        Assert.assertTrue(templates != null && templates.size() > 0);
        final Map<String, Base> map = new HashMap<>();
   //    map.put("Testkey", new Double());
        String result = templates.get(0).createAsset(map);
        System.out.println("Result: "+result);
    }

    @Test
    public void createObjectByTemplateAndDelete() throws ServiceException{
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        final List<Template> templates = service.loadTemplates();
        Assert.assertTrue(templates != null && templates.size() > 0);
        final Map<String, Base> map = new HashMap<>();
        //    map.put("Testkey", new Double());
        String id = templates.get(0).createAsset(map);

        Assert.assertNotNull(id);
        final ObjectBase base = service.loadObject(id);
        Assert.assertNotNull(base);
        String result = service.deleteObject(base, true);

        System.out.println("Result: "+result);
    }

    @Test
    public void testLoadDatastreamUpdateStream() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        System.out.println("Bearer "+service.getAuthenticationToken().getAccessToken());
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);

        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);
        Assert.assertTrue(base instanceof Asset);
        final List<DataStream> children = ((Asset)base).loadDataStreams();
        Assert.assertNotNull(children);
        Assert.assertTrue(children.size() > 0);

        for(DataStream entry : children){
            if(entry.getObjectId().equalsIgnoreCase("2c7c94eb76df497d90e33cdf9f97c5f4")){
                System.out.println(entry.toString());
                Base b = entry.loadLastValue();
                DoubleAverage a = new DoubleAverage();
                a.setMaximum(new java.lang.Double(100));
                a.setMinimum(new java.lang.Double(10));
                a.setValue(new java.lang.Double(55));
                DataWriteResponse response = entry.saveData(a);
                Assert.assertNotNull(response);
                System.out.println("response: "+response);
                Assert.assertTrue(ResponseType.equals(response.getResponseType(), ResponseType.SUCCESS));
            }
        }
    }
}
