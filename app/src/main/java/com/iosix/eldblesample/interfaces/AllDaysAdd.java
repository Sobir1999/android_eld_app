package com.iosix.eldblesample.interfaces;

import com.iosix.eldblesample.roomDatabase.entities.DayEntity;

import java.io.IOException;
import java.util.List;

public interface AllDaysAdd {

    void getAllDays(List<DayEntity> dayEntities) throws IOException;
}
