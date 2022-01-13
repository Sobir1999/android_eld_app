package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class Eld {

    @SerializedName("eld_id")
    String eld_id;

    @SerializedName("eld_num")
    String eld_num;

    public Eld(String eld_id, String eld_num) {
        this.eld_id = eld_id;
        this.eld_num = eld_num;
    }

    public String getEld_id() {
        return eld_id;
    }

    public void setEld_id(String eld_id) {
        this.eld_id = eld_id;
    }

    public String getEld_num() {
        return eld_num;
    }

    public void setEld_num(String eld_num) {
        this.eld_num = eld_num;
    }
}
