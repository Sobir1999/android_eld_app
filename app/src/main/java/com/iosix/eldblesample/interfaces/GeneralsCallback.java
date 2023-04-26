package com.iosix.eldblesample.interfaces;

import com.iosix.eldblesample.models.Vehicle;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.util.List;

public interface GeneralsCallback {

    void getGenerals(List<String> vehicles, List<String> trailersEntities,List<String> co_drivers,List<String> shippingDocs,String from,String to,String notes);
}
