package com.linemetrics.api;

import com.linemetrics.api.datatypes.*;
import com.linemetrics.api.datatypes.Double;
import com.linemetrics.api.exceptions.AuthorizationException;
import com.linemetrics.api.exceptions.ServiceException;
import com.linemetrics.api.returntypes.*;
import com.linemetrics.api.types.FunctionType;
import com.linemetrics.api.types.ResourceType;
import com.linemetrics.api.types.ResponseType;
import org.junit.Assert;
import org.junit.Test;

import java.lang.String;
import java.util.*;

import static com.linemetrics.api.TestConstants.VALID_CLIENTID;
import static com.linemetrics.api.TestConstants.VALID_OBJECT_ID;
import static com.linemetrics.api.TestConstants.VALID_CLIENTSECRET;
import static com.linemetrics.api.TestConstants.VALID_OBJECT_ALIAS;
import static com.linemetrics.api.TestConstants.VALID_CUSTOMKEY;
import static com.linemetrics.api.TestConstants.VALID_OBJECTID_DATASTREAM_TOUPDATE;


/**
 * Created by Klemens on 06.03.2017.
 */
public class TestLineMetricsService {

    @Test(expected = AuthorizationException.class)
    public void testInstantiate_Invalid() throws ServiceException {
        final ILMService service = new LineMetricsService("testClientId", "testClientSecret");
    }

    @Test(expected = ServiceException.class)
    public void testInstantiate_Invalid_1() throws ServiceException {
        final ILMService service = new LineMetricsService(null, "testClientSecret");
    }

    @Test(expected = ServiceException.class)
    public void testInstantiate_Invalid_2() throws ServiceException {
        final ILMService service = new LineMetricsService("testClientId", null);
    }

    @Test
    public void testInstantiate() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        Assert.assertNotNull(service.getAuthenticationToken());
        Assert.assertNotNull(service.getAuthenticationToken().getAccessToken());
        Assert.assertNotNull(service.getAuthenticationToken().getTokenType());
        Assert.assertNotNull(service.getAuthenticationToken().getExpiresIn());
        System.out.println(service.getAuthenticationToken().toString());
    }

    @Test
    public void testLoadAssets() throws ServiceException {
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
    public void testLoadObjectById() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getObjectId());
        Assert.assertNotNull(base.getPayload());
        Assert.assertTrue(base.getPayload().size() > 0);
    }

    @Test(expected = ServiceException.class)
    public void testLoadObjectByIdAndAlias_Invalid() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID, "WRONG");
        Assert.assertNull(base);
        service.logout();
    }

    @Test
    public void testLoadObjectByIdAndAlias() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObject(VALID_OBJECT_ID, VALID_OBJECT_ALIAS);
        System.out.println(base.toString());
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getPayload());
        Assert.assertNotNull(base.getParentId());
    }

    @Test
    public void testLoadObjectByCustomKey() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObjectByCustomKey(VALID_CUSTOMKEY);
        Assert.assertNotNull(base);
        Assert.assertNotNull(base.getPayload());
    }

    @Test(expected = ServiceException.class)
    public void testLoadObjectByCustomKey_Invalid() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);
        final ObjectBase base = service.loadObjectByCustomKey("asdf");
        Assert.assertNull(base);
    }

    @Test
    public void testLoadAssetsByTypeAndId_Datastream() throws ServiceException {
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
    public void testLoadAssetsOfParentObject() throws ServiceException {
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
    public void testLoadPropertiesOfParentObject() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
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
    public void testLoadDatastreamOfParentObject() throws ServiceException {
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
    public void testLoadDatastreamRaw() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
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
    public void testLoadDatastreamAverage() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
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
    public void testUpdateObjectById() throws ServiceException {
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
            if(entry.getObjectId().equalsIgnoreCase(VALID_OBJECTID_DATASTREAM_TOUPDATE)){
                System.out.println(entry.toString());
                Base b = entry.loadLastValue();
                DoubleAverage a = new DoubleAverage(new java.lang.Double(55),new java.lang.Double(10), new java.lang.Double(100), new Date());
                DataWriteResponse response = entry.saveData(a);
                Assert.assertNotNull(response);
                System.out.println("response: "+response);
                Assert.assertTrue(ResponseType.equals(response.getResponseType(), ResponseType.SUCCESS));
            }
        }
    }

    @Test
    public void testUpdateData() throws ServiceException {
        final ILMService service = new LineMetricsService(VALID_CLIENTID, VALID_CLIENTSECRET);
        Assert.assertNotNull(service);

        ObjectBase base = service.loadObject(VALID_OBJECTID_DATASTREAM_TOUPDATE);
        Assert.assertNotNull(base);
        Assert.assertTrue(base instanceof DataStream);

        Calendar from = Calendar.getInstance();
        Calendar to = Calendar.getInstance();

        from.set(Calendar.HOUR_OF_DAY, 0);
        from.set(Calendar.MINUTE, 0);

        to.set(Calendar.HOUR_OF_DAY, 17);
        to.set(Calendar.MINUTE, 59);

  //      DataWriteResponse resp = ((DataStream)base).saveData(new DoubleAverage(new java.lang.Double(85), new java.lang.Double(0), new java.lang.Double(100), new Date()));
  //      Assert.assertNotNull(resp);

        List<DataReadResponse> res = ((DataStream)base).loadData(from.getTime(), to.getTime(), "Europe/Vienna", "PT1M", FunctionType.RAW);
        for(DataReadResponse entry : res){
            System.out.println(((RawDataReadResponse)entry).toString());
        }
    }
}
