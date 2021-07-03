package com.iosix.eldblesample.roomDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "unit_defects")
public class UnitDefectsEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "name")
    private String name;
    @ColumnInfo(name = "dvir_id")
    private int dvir_id;

    public UnitDefectsEntity(int id, String name, int dvir_id) {
        this.id = id;
        this.name = name;
        this.dvir_id = dvir_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDvir_id() {
        return dvir_id;
    }

    public void setDvir_id(int dvir_id) {
        this.dvir_id = dvir_id;
    }
}
