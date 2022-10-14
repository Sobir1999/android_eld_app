package com.iosix.eldblesample.viewModel.apiViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
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
import com.iosix.eldblesample.retrofit.ApiRepository;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EldJsonViewModel extends AndroidViewModel {

    private final ApiRepository repository;
    private final CompositeDisposable disposables;

    private StateLiveData<LoginResponse> stateLiveData;

    public EldJsonViewModel(@NonNull Application application) {
        super(application);
        repository = new ApiRepository();
        disposables = new CompositeDisposable();
    }

    public void sendLive(LiveDataRecord data) {
        repository.sendLive(data);
    }

    public void makeLoginRequest(Student student) {
        repository.getLoginResponse(student);
    }

    public MutableLiveData<User> getUser(){
        return repository.getUser();
    }

    public StateLiveData<LoginResponse> getLoginResponse(){
        return repository.getLoginState();
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

    public MutableLiveData<LiveDataEntitiy> sendLocalData(LiveDataEntitiy liveDataEntitiy) {
        return repository.sendLocalData(liveDataEntitiy);
    }

    public MutableLiveData<ApkVersion> sendApkVersion(ApkVersion apkVersion) {
        return repository.sendApkVersion(apkVersion);
    }

    public void sendPdf(RequestBody body) {
        Disposable disposable = repository
                .sendPdf(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Log.d("PDF_LOG", String.format("Success with response %s", s));
                    // TODO on success
                }, throwable -> {
                    Log.d("PDF_LOG", "Failure with", throwable);
                });

        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
