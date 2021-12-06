package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class NewTimeRecord {

    @SerializedName("previous_time")
    private long previousTime;

    @SerializedName("time")
    private long newTime;

    @SerializedName("sequence_number")
    private int sequenceNum;

    public NewTimeRecord(long previousTime,long newTime,int sequenceNum){
        this.previousTime = previousTime;
        this.newTime = newTime;
        this.sequenceNum = sequenceNum;
    }

    public long getPreviousTime() {
        return previousTime;
    }

    public void setPreviousTime(long previousTime) {
        this.previousTime = previousTime;
    }

    public long getNewTime() {
        return newTime;
    }

    public void setNewTime(long newTime) {
        this.newTime = newTime;
    }

    public int getSequenceNum() {
        return sequenceNum;
    }

    public void setSequenceNum(int sequenceNum) {
        this.sequenceNum = sequenceNum;
    }

}
