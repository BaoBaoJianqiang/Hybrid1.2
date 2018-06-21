package jianqiang.com.hook3;

import java.util.HashMap;

public class PageInfo {
    private String uri;
    private HashMap<String, Integer> fields;

    public HashMap<String, Integer> getFields() {
        return fields;
    }

    public void setFields(HashMap<String, Integer> fields) {
        this.fields = fields;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public PageInfo(String uri, HashMap<String, Integer> fields) {
        this.uri = uri;
        this.fields = fields;
    }
}