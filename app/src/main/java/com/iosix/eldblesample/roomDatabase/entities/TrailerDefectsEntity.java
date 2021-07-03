package com.iosix.eldblesample.roomDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "trailer_defects")
public class TrailerDefectsEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String number;
    @ColumnInfo(name = "dvir_id")
    private int dvir_id;

    public TrailerDefectsEntity(int id, String number, int dvir_id) {
        this.id = id;
        this.number = number;
        this.dvir_id = dvir_id;
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

    public int getDvir_id() {
        return dvir_id;
    }

    public void setDvir_id(int dvir_id) {
        this.dvir_id = dvir_id;
    }
}
