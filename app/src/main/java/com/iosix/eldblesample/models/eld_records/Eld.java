package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class Eld {

    @SerializedName("eld_serial")
    String eld_num;

    @SerializedName("eld_version")
    String eld_ver;

    public Eld(String eld_num,String eld_ver) {
        this.eld_num = eld_num;
        this.eld_ver = eld_ver;
    }


    public String getEld_num() {
        return eld_num;
    }

    public void setEld_num(String eld_num) {
        this.eld_num = eld_num;
    }

    public String getEld_ver() {
        return eld_ver;
    }

    public void setEld_ver(String eld_ver) {
        this.eld_ver = eld_ver;
    }
}
