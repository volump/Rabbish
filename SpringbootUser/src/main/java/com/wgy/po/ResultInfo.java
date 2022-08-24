package com.wgy.po;

public class ResultInfo {
    private int state;
    private String retInfo;

    public void setResultInfo(int state, String retInfo) {
        this.state = state;  //失败设为200；
        this.retInfo = retInfo;
    }

    @Override
    public String toString() {
        return "ResultInfo{" +
                "state=" + state +
                ", retInfo='" + retInfo + '\'' +
                '}';
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getRetInfo() {
        return retInfo;
    }

    public void setRetInfo(String retInfo) {
        this.retInfo = retInfo;
    }
}
