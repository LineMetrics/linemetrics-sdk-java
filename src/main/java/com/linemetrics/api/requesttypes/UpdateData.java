package com.linemetrics.api.requesttypes;

/**
 * Created by Klemens on 07.03.2017.
 */
public class UpdateData {

    public UpdateData(){}

    public UpdateData(String customKey, String alias, String name, String parent){
        this.customKey = customKey;
        this.alias = alias;
        this.name = name;
        this.parent = parent;
    }

    private String customKey;
    private String alias;
    private String name;
    private String parent;
    private String icon;

    public String getCustomKey() {
        return customKey;
    }

    public void setCustomKey(String customKey) {
        this.customKey = customKey;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}