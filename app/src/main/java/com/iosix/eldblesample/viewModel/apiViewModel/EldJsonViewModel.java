package com.iosix.eldblesample.viewModel.apiViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.SendDvir;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleData;
import com.iosix.eldblesample.models.VehicleList;
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
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import okhttp3.MultipartBody;

public class EldJsonViewModel extends AndroidViewModel {
    private final ApiRepository repository;

    public EldJsonViewModel(@NonNull Application application) {
        super(application);
        repository = ApiRepository.getInstance();
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

    public MutableLiveData<LoginResponse> getResponse(Student student) {
        return repository.getLoginResponse(student);
    }



    public MutableLiveData<User> getUser(){
        return repository.getUser();
    }

    public MutableLiveData<Data> getAllDrivers(){
        return repository.getAllDrivers();
    }

    public MutableLiveData<VehicleData> getAllVehicles(){
        return repository.getAllVehicles();
    }

    public MutableLiveData<LoginResponse> refreshToken(String token){
        return repository.refreshToken(token);
    }

    public MutableLiveData<Status> postStatus(Status status){
        return repository.postStatus(status);
    }

    public MutableLiveData<VehicleList> getVehicle(){
        return repository.getVehicle();
    }

    public MutableLiveData<User> getCoDriver(){
        return repository.getCoDriver();
    }

    public MutableLiveData<GeneralEntity> sendGeneralInfo(GeneralEntity entity){
        return repository.sendGenerelInfo(entity);
    }

    public MutableLiveData<TrailersEntity> sendTrailer(TrailNubmer trailNubmer){
        return repository.sendTrailer(trailNubmer);
    }

    public MutableLiveData<SendDvir> sendDvir(SendDvir sendDvir){
        return repository.sendDvir(sendDvir);
    }

    public MutableLiveData<MultipartBody.Part> sendSignature(MultipartBody.Part body){
        return repository.sendSignature(body);
    }

    public MutableLiveData<Eld> sendEldNum(Eld eld){
        return repository.sendEldNum(eld);
    }

    public MutableLiveData<LiveDataEntitiy> sendLocalData(LiveDataEntitiy liveDataEntitiy){
        return repository.sendLocalData(liveDataEntitiy);
    }

    public MutableLiveData<ApkVersion> sendApkVersion(ApkVersion apkVersion){
        return repository.sendApkVersion(apkVersion);
    }
}
