package com.iosix.eldblesample.models.eld_records;

import com.google.gson.annotations.SerializedName;

public class BufferRecord {

    @SerializedName("sequence_start")
    private int sequence_start;

    @SerializedName("sequence_end")
    private int sequence_end;

    @SerializedName("total")
    private int total;

    @SerializedName("storage")
    private int storage;

    public BufferRecord(int sequence_start,int sequence_end,int total,int storage){
        this.sequence_start = sequence_start;
        this.sequence_end = sequence_end;
        this.total = total;
        this.storage = storage;
    }

    public int getSequenceStart() {
        return sequence_start;
    }

    public void setSequenceStart(int sequence_start) {
        this.sequence_start = sequence_start;
    }

    public int getSequenceEnd() {
        return sequence_end;
    }

    public void setSequenceEnd(int sequence_end) {
        this.sequence_end = sequence_end;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getStorage() {
        return storage;
    }

    public void setStorage(int storage) {
        this.storage = storage;
    }

}
