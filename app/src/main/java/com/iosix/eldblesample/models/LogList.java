package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LogList {

    @SerializedName("results")
    List<Status> logList;

    public LogList(List<Status> logList){
        this.logList = logList;
    }

    public List<Status> getLogList() {
        return logList;
    }

    public void setLogList(List<Status> logList) {
        this.logList = logList;
    }
}
