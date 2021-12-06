package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class PowerOnRecord {

    @SerializedName("hard_boots")
    private int hardBoots;

    @SerializedName("crashes")
    private int crashes;

    @SerializedName("time")
    private long time;

    @SerializedName("sequence_number")
    private int sequenceNum;

    public PowerOnRecord(int hardBoots,int crashes,long time,int sequenceNum){

        this.hardBoots = hardBoots;
        this.crashes = crashes;
        this.time = time;
        this.sequenceNum = sequenceNum;

    }

    public int getHardBoots() {
        return hardBoots;
    }

    public void setHardBoots(int hardBoots) {
        this.hardBoots = hardBoots;
    }

    public int getCrashes() {
        return crashes;
    }

    public void setCrashes(int crashes) {
        this.crashes = crashes;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }
}
