package com.iosix.eldblesample.roomDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chipping_docs")
public class ShippingDocsEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    private String number;
    private int general_id;

    public ShippingDocsEntity(int id, String number, int general_id) {
        this.id = id;
        this.number = number;
        this.general_id = general_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getGeneral_id() {
        return general_id;
    }

    public void setGeneral_id(int general_id) {
        this.general_id = general_id;
    }
}
