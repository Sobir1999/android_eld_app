package com.iosix.eldblesample.roomDatabase.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "main_office_info")
public class MainOfficeEntity {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "carrier")
    private String carrier;
    @ColumnInfo(name = "main_office_forst_num")
    private String main_office_first_num;
    @ColumnInfo (name = "main_office_place")
    private String main_office_place;
    @ColumnInfo(name = "main_office_rag")
    private String main_office_rag;
    @ColumnInfo(name = "main_office_sec_num")
    private String main_office_sec_num;
    @ColumnInfo(name = "home_ter_first")
    private String home_ter_first;
    @ColumnInfo(name = "home_ter_place")
    private String home_ter_place;
    @ColumnInfo(name = "home_ter_rag")
    private String home_ter_rag;
    @ColumnInfo(name = "homer_ter_sec_num")
    private String home_ter_sec_num;

    public MainOfficeEntity(int id, String carrier, String main_office_first_num, String main_office_place, String main_office_rag, String main_office_sec_num, String home_ter_first, String home_ter_place, String home_ter_rag, String home_ter_sec_num) {
        this.id = id;
        this.carrier = carrier;
        this.main_office_first_num = main_office_first_num;
        this.main_office_place = main_office_place;
        this.main_office_rag = main_office_rag;
        this.main_office_sec_num = main_office_sec_num;
        this.home_ter_first = home_ter_first;
        this.home_ter_place = home_ter_place;
        this.home_ter_rag = home_ter_rag;
        this.home_ter_sec_num = home_ter_sec_num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getMain_office_first_num() {
        return main_office_first_num;
    }

    public void setMain_office_first_num(String main_office_first_num) {
        this.main_office_first_num = main_office_first_num;
    }

    public String getMain_office_place() {
        return main_office_place;
    }

    public void setMain_office_place(String main_office_place) {
        this.main_office_place = main_office_place;
    }

    public String getMain_office_rag() {
        return main_office_rag;
    }

    public void setMain_office_rag(String main_office_rag) {
        this.main_office_rag = main_office_rag;
    }

    public String getMain_office_sec_num() {
        return main_office_sec_num;
    }

    public void setMain_office_sec_num(String main_office_sec_num) {
        this.main_office_sec_num = main_office_sec_num;
    }

    public String getHome_ter_first() {
        return home_ter_first;
    }

    public void setHome_ter_first(String home_ter_first) {
        this.home_ter_first = home_ter_first;
    }

    public String getHome_ter_place() {
        return home_ter_place;
    }

    public void setHome_ter_place(String home_ter_place) {
        this.home_ter_place = home_ter_place;
    }

    public String getHome_ter_rag() {
        return home_ter_rag;
    }

    public void setHome_ter_rag(String home_ter_rag) {
        this.home_ter_rag = home_ter_rag;
    }

    public String getHome_ter_sec_num() {
        return home_ter_sec_num;
    }

    public void setHome_ter_sec_num(String home_ter_sec_num) {
        this.home_ter_sec_num = home_ter_sec_num;
    }
}
