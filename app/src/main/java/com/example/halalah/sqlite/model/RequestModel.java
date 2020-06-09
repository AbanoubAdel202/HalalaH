package com.example.halalah.sqlite.model;


import com.example.halalah.sqlite.repository.SqliteGenericObject;
import com.example.halalah.sqlite.repository.annotation.SqliteNotNull;
import com.example.halalah.sqlite.repository.annotation.SqlitePrimaryKey;
import com.example.halalah.sqlite.repository.annotation.SqliteTableName;

import java.io.Serializable;

@SqliteTableName("testdb")
public class RequestModel implements SqliteGenericObject, Serializable {

    @SqlitePrimaryKey
    @SqliteNotNull
    private String requestId;

    @SqliteNotNull
    private String url;

    @SqliteNotNull
    private String body;

    @SqliteNotNull
    private String status;

    public RequestModel() {
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getId() {
        return getRequestId();
    }
}
