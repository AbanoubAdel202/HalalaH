package com.example.halalah.util;


public class ResultsBean {
    private String rescode = null;//代码
    private String meaning = null;//意义
    private String type = null;//类别
    private String cause = null;//原因
    private String show = null;//显示

    public ResultsBean( String meaning, String type, String cause, String show) {
        this.meaning = meaning;
        this.type = type;
        this.cause = cause;
        this.show = show;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public String getRescode() {
        return rescode;
    }

    public void setRescode(String rescode) {
        this.rescode = rescode;
    }
}
