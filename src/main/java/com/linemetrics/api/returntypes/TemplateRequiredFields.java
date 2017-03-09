package com.linemetrics.api.returntypes;

/**
 * Created by Klemens on 08.03.2017.
 */
public class TemplateRequiredFields {

    private String dataType;
    private String id;
    private String alias;


    public Class<?> getDataType() throws ClassNotFoundException{
        return Class.forName("com.linemetrics.api.datatypes."+this.dataType);
    }

    public void setDataType(Class<?> type){
        this.dataType = type.getName().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString(){
        return String.format("Id: %s, Alias: %s, DataType: %s", this.id, this.alias, this.dataType.toString());
    }
}
