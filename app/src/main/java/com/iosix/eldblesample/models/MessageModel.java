package com.iosix.eldblesample.models;

public class MessageModel {
    private String statusView;
    private String dataView;

    public MessageModel(String statusView, String dataView) {
        this.statusView = statusView;
        this.dataView = dataView;
    }

    public String getStatusView() {
        return statusView;
    }

    public void setStatusView(String statusView) {
        this.statusView = statusView;
    }

    public String getDataView() {
        return dataView;
    }

    public void setDataView(String dataView) {
        this.dataView = dataView;
    }
}
