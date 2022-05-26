package com.iosix.eldblesample.viewModel.apiViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.SendDvir;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.eld_records.BufferRecord;
import com.iosix.eldblesample.models.eld_records.CachedEngineRecord;
import com.iosix.eldblesample.models.eld_records.CashedMotionRecord;
import com.iosix.eldblesample.models.eld_records.DriverBehaviorRecord;
import com.iosix.eldblesample.models.eld_records.Eld;
import com.iosix.eldblesample.models.eld_records.EmissionsRecord;
import com.iosix.eldblesample.models.eld_records.EngineLiveRecord;
import com.iosix.eldblesample.models.eld_records.FuelRecord;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.NewTimeRecord;
import com.iosix.eldblesample.models.eld_records.PowerOnRecord;
import com.iosix.eldblesample.models.eld_records.TransmissionRecord;
import com.iosix.eldblesample.retrofit.ApiRepository;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;

import okhttp3.MultipartBody;

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

    public void getUser(){
        repository.getUser();
    }

    public void getAllDrivers(){
        repository.getAllDrivers();
    }

    public void getAllVehicles(){
        repository.getAllVehicles();
    }

    public void refreshToken(String token){
        repository.refreshToken(token);
    }

    public void postStatus(Status status){
        repository.postStatus(status);
    }

    public void getVehicle(){
        repository.getVehicle();
    }

    public void getCoDriver(){
        repository.getCoDriver();
    }

    public void sendGeneralInfo(GeneralEntity entity){
        repository.sendGenerelInfo(entity);
    }

    public void sendTrailer(TrailNubmer trailNubmer){
        repository.sendTrailer(trailNubmer);
    }

    public void sendDvir(SendDvir sendDvir){
        repository.sendDvir(sendDvir);
    }

    public void sendSignature(MultipartBody.Part body){
        repository.sendSignature(body);
    }

    public void sendEldNum(Eld eld){
        repository.sendEldNum(eld);
    }

    public void sendLocalData(LiveDataEntitiy liveDataEntitiy){
        repository.sendLocalData(liveDataEntitiy);
    }

    public void sendApkVersion(ApkVersion apkVersion){
        repository.sendApkVersion(apkVersion);
    }
}
