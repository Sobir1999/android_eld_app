package com.iosix.eldblesample.viewModel.apiViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.enums.StateLiveData;
import com.iosix.eldblesample.models.ApkVersion;
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
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EldJsonViewModel extends AndroidViewModel {

    private final ApiRepository repository;
    private final CompositeDisposable disposables;
    private final StateLiveData<LoginResponse> studentMutableLiveData = new StateLiveData<>();
    private final MutableLiveData<List<User>> driverMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<VehicleList>> vehicleMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<LogEntity>> logsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<DvirEntity>> dvirsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<SignatureEntity>> signaturesMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<GeneralEntity>> generalsMutableLiveData = new MutableLiveData<>();

    public EldJsonViewModel(@NonNull Application application) {
        super(application);
        repository = new ApiRepository();
        disposables = new CompositeDisposable();
    }

    public void sendLive(LiveDataRecord data) {
        Disposable disposable = repository
                .sendLive(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void makeLoginRequest(Student student) {
        studentMutableLiveData.postLoading();
        Disposable disposable = repository
                .getLoginResponse(student)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.isSuccessful()){
                        studentMutableLiveData.postSuccess(data.body());
                    }else {
                        studentMutableLiveData.postStatusCode(data.code());
                    }
                }, studentMutableLiveData::postError);

        disposables.add(disposable);
    }

    public StateLiveData<LoginResponse> getLoginResponse(){
        return studentMutableLiveData;
    }

    public void getUser(){
        Disposable disposable = repository
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(userMutableLiveData::postValue, throwable -> {

                });

        disposables.add(disposable);
    }

   public MutableLiveData<User> getUserMutableLiveData(){
        return userMutableLiveData;
   }

    public void getAllDrivers(){
        Disposable disposable = repository
                .getAllDrivers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users ->{
                    driverMutableLiveData.postValue(users.getDriver().getUser());
                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public MutableLiveData<List<User>> getDriverMutableLiveData() {
        return driverMutableLiveData;
    }

    public void getAllVehicles(){
        Disposable disposable = repository
                .getAllVehicles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    vehicleMutableLiveData.postValue(s.getVehicle().getVehicleList());
                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public MutableLiveData<List<VehicleList>> getVehicleLiveData() {
        return vehicleMutableLiveData;
    }

    public void postStatus(Status status){
        Disposable disposable = repository
                .postStatus(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void sendSignature(MultipartBody.Part body){
        Disposable disposable = repository
                .sendSignature(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void sendEldNum(Eld eld){
        Disposable disposable = repository
                .sendEldNum(eld)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void sendLocalData(LiveDataEntitiy liveDataEntitiy) {
        Disposable disposable = repository
                .sendLocalData(liveDataEntitiy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void sendApkVersion(ApkVersion apkVersion) {
        Disposable disposable = repository
                .sendApkVersion(apkVersion)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
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



    public void sendGeneralInfo(GeneralEntity entity){
        Disposable disposable = repository
                .sendGenerelInfo(entity)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void sendTrailer(TrailNubmer trailNubmer){
        Disposable disposable = repository
                .sendTrailer(trailNubmer)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void sendDvir(SendDvir sendDvir){
        Disposable disposable = repository
                .sendDvir(sendDvir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void getVehicle(){
        Disposable disposable = repository
                .getVehicle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void getCoDriver(){
        Disposable disposable = repository
                .getCoDriver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {

                });

        disposables.add(disposable);
    }

    public void getAllLogs(){
        Disposable disposable = repository
                .getAllLogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(logsMutableLiveData::postValue, throwable -> {

                });

        disposables.add(disposable);
    }

    public MutableLiveData<List<LogEntity>> getLogsMutableLiveData(){
        return logsMutableLiveData;
    }

    public void getAllDvirs(){
        Disposable disposable = repository
                .getAllDvirs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dvirsMutableLiveData::postValue, throwable -> {

                });

        disposables.add(disposable);
    }

    public MutableLiveData<List<DvirEntity>> getDvirsMutableLiveData(){
        return dvirsMutableLiveData;
    }

    public void getAllSignatures(){
        Disposable disposable = repository
                .getAllSignatures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signaturesMutableLiveData::postValue, throwable -> {

                });

        disposables.add(disposable);
    }

    public MutableLiveData<List<SignatureEntity>> getSignaturesMutableLiveData(){
        return signaturesMutableLiveData;
    }

    public void getAllGenerals(){
        Disposable disposable = repository
                .getAllGenerals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalsMutableLiveData::postValue, throwable -> {

                });

        disposables.add(disposable);
    }

    public MutableLiveData<List<GeneralEntity>> getGeneralsMutableLiveData(){
        return generalsMutableLiveData;
    }


    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }
}
