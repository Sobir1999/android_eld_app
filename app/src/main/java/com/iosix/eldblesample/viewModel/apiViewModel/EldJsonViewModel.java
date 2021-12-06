package com.iosix.eldblesample.viewModel.apiViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.eld_records.BufferRecord;
import com.iosix.eldblesample.models.eld_records.CachedEngineRecord;
import com.iosix.eldblesample.models.eld_records.CashedMotionRecord;
import com.iosix.eldblesample.models.eld_records.DriverBehaviorRecord;
import com.iosix.eldblesample.models.eld_records.EmissionsRecord;
import com.iosix.eldblesample.models.eld_records.EngineLiveRecord;
import com.iosix.eldblesample.models.eld_records.FuelRecord;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.NewTimeRecord;
import com.iosix.eldblesample.models.eld_records.PowerOnRecord;
import com.iosix.eldblesample.models.eld_records.TransmissionRecord;
import com.iosix.eldblesample.retrofit.ApiRepository;

public class EldJsonViewModel extends AndroidViewModel {
    private ApiRepository repository;

    public EldJsonViewModel(@NonNull Application application) {
        super(application);
        repository = new ApiRepository(application);
    }

    public void sendBufferRecord(BufferRecord data) {
        repository.sendBufferRecord(data);
    }

    public void sendEnginecache(CachedEngineRecord data) {
        repository.sendEnginecache(data);
    }

    public void sendPoweron(PowerOnRecord data) {
        repository.sendPoweron(data);
    }

    public void sendNewtime(NewTimeRecord data) {
        repository.sendNewtime(data);
    }

    public void sendMotion(CashedMotionRecord data) {
        repository.sendMotion(data);
    }

    public void sendLive(LiveDataRecord data) {
        repository.sendLive(data);
    }

    public void sendDriverbehavior(DriverBehaviorRecord data) {
        repository.sendDriverbehavior(data);
    }

    public void sendEmission(EmissionsRecord data) {
        repository.sendEmission(data);
    }

    public void sendEnginelive(EngineLiveRecord data) {
        repository.sendEnginelive(data);
    }

    public void sendFuel(FuelRecord data) {
        repository.sendFuel(data);
    }

    public void sendTransmission(TransmissionRecord data) {
        repository.sendTransmission(data);
    }


    public void createUser(Student student) {
        repository.createUser(student);
    }
}
