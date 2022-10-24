package com.iosix.eldblesample.retrofit;


import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.enums.StateLiveData;
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
import com.iosix.eldblesample.models.eld_records.Eld;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.io.IOException;
import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private static APIInterface apiInterface;
    private final MutableLiveData<LoginResponse> refreshTokenResponse = new MutableLiveData<>();

    public ApiRepository() {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
    }

    public Single<LiveDataRecord> sendLive(LiveDataRecord record) {
        return apiInterface.sendLive(record);
    }

    public Single<String> sendPdf(RequestBody body) {
        return apiInterface.sendPdf(body);
    }

    public Single<Response<LoginResponse>> getLoginResponse(Student student) {
        return apiInterface.createUser(student);
    }

    public Single<User> getUser() {
        return apiInterface.getUser();
    }

    public Single<Data> getAllDrivers() {
        return apiInterface.getAllDrivers();
    }


    public Single<VehicleData> getAllVehicles() {
        return apiInterface.getAllVehicles();
    }

    public Single<LoginResponse> refreshToken(String refreshToken) {
        return apiInterface.refreshToken(refreshToken);
    }

    public Single<Status> postStatus(Status status) {
        return apiInterface.postStatus(status);
    }

    public Single<VehicleList> getVehicle() {
        return apiInterface.getVehicle();
    }

    public Single<User> getCoDriver() {
        return apiInterface.getCoDriver();
    }

    public Single<GeneralEntity> sendGenerelInfo(GeneralEntity entity) {
        return apiInterface.sendGeneralInfo(entity);
    }

    public Single<TrailersEntity> sendTrailer(TrailNubmer trailNubmer) {
        return apiInterface.sendTrailer(trailNubmer);
    }

    public Single<SendDvir> sendDvir(SendDvir sendDvir) {
        return apiInterface.sendDvir(sendDvir);
    }

    public Single<String> sendSignature(MultipartBody.Part body) {
        return apiInterface.sendSignature(body);
    }

    public Single<Eld> sendEldNum(Eld eld) {
        return apiInterface.sendEldNum(eld);
    }


    public Single<LiveDataEntitiy> sendLocalData(LiveDataEntitiy liveDataEntitiy) {
        return apiInterface.sendLocalData(liveDataEntitiy);
    }

    public Single<ApkVersion> sendApkVersion(ApkVersion apkVersion) {
        return apiInterface.sendApkVersion(apkVersion);
    }

    public Single<List<LogEntity>> getAllLogs(){
        return apiInterface.getAllLogs();
    }

    public Single<List<DvirEntity>> getAllDvirs(){
        return apiInterface.getAllDvirs();
    }

    public Single<List<SignatureEntity>> getAllSignatures(){
        return apiInterface.getAllSignatures();
    }

    public Single<List<GeneralEntity>> getAllGenerals(){
        return apiInterface.getAllGenerals();
    }

}


