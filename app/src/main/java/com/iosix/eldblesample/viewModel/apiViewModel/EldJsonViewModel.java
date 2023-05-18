package com.iosix.eldblesample.viewModel.apiViewModel;

import static com.iosix.eldblesample.enums.Day.getCalculatedDate;
import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.getDayTime1;
import static com.iosix.eldblesample.enums.Day.getDayTimeFromZ;
import static com.iosix.eldblesample.enums.Day.stringToDate;
import static com.iosix.eldblesample.utils.Utils.getStatus;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.enums.Day;
import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.models.Signature;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.VehicleData;
import com.iosix.eldblesample.models.VehicleList;
import com.iosix.eldblesample.models.eld_records.Eld;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.retrofit.ApiRepository;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.LastStatusData;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EldJsonViewModel extends AndroidViewModel {

    private final ApiRepository repository;
    private final CompositeDisposable disposables;

    public EldJsonViewModel(@NonNull Application application) {
        super(application);
        repository = new ApiRepository();
        disposables = new CompositeDisposable();
    }

    public void getUser(DriverSharedPrefs driverSharedPrefs){
        Disposable disposable = repository
                .getUser()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    driverSharedPrefs.saveLastUsername(user.getName());
                    driverSharedPrefs.saveLastSurname(user.getLastName());
                    driverSharedPrefs.saveCompany(user.getCompany());
                    driverSharedPrefs.saveLastHomeTerAdd(user.getHomeTerminalAddress());
                    driverSharedPrefs.saveLastMainOffice(user.getMainOffice());
                    driverSharedPrefs.saveLastHomeTerTimeZone(user.getTimeZone());
                    driverSharedPrefs.saveLastPhoneNumber(user.getPhone());
                    driverSharedPrefs.saveLastDriverId(user.getDriverId());
                }, throwable -> {
                });

        disposables.add(disposable);
    }

    public void sendLive(LiveDataRecord data) {
        Disposable disposable = repository
                .sendLive(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{

                }, throwable -> {
                    Log.d("Adverse",throwable.getMessage());
                });

        disposables.add(disposable);
    }

    public void getAllDrivers(UserViewModel userViewModel){
        Disposable disposable = repository
                .getAllDrivers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users ->{
                    for (User user: users.getDriver().getUser()) {
                        userViewModel.insertUser(user);
                    }
                }, throwable -> {
                });
        disposables.add(disposable);
    }

    public void getAllVehicles(UserViewModel userViewModel){
        Disposable disposable2 = repository
                .getAllVehicles()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    if (s != null){
                        for (VehicleList vehicle: s.getVehicle().getVehicleList()) {
                            userViewModel.insertVehicle(vehicle);
                        }
                    }
                }, throwable -> {

                });
        disposables.add(disposable2);
    }

    public void postStatus(Status status){
        Disposable disposable = repository
                .postStatus(status)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    Log.d("Adverse",status.getStatus());
                }, throwable -> {
                    Log.d("Adverse",throwable.getMessage());
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
                .subscribe(s -> {
                }, throwable -> {
                });

        disposables.add(disposable);
    }

    public void sendPdf(RequestBody email,MultipartBody.Part body) {
        Disposable disposable = repository
                .sendPdf(email,body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Log.d("PDF_LOG", String.format("Success with response %s", s));
                    // TODO on success
                }, throwable -> Log.d("PDF_LOG", "Failure with" + throwable.getMessage()));

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

    public void sendDvir(Dvir sendDvir){
        Disposable disposable = repository
                .sendDvir(sendDvir)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s ->{
                    Log.d("Adverse",s.getUnit());
                }, throwable -> {
                    Log.d("Adverse",throwable.getMessage());
                    Log.d("Adverse",throwable.getLocalizedMessage());
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

    public void getAllLogs(StatusDaoViewModel statusDaoViewModel, LastStatusData lastStatusData,String today){
        Disposable disposable = repository
                .getAllLogs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    List<Status> filteredStatuses = new ArrayList<>();
                    if (!statuses.isEmpty()){
                        for (Status status: statuses) {
                            if (stringToDate(status.getTime()).compareTo(getCalculatedDate(9)) > 0){
                                if (getStatus(status.getStatus()) < 6){
                                    filteredStatuses.add(status);
                                }
                                statusDaoViewModel.insertStatus(status);
                            }
                        }
                        if (!filteredStatuses.isEmpty()){
                            if (getDayTimeFromZ(stringToDate(filteredStatuses.get(filteredStatuses.size()-1).getTime())).equals(today)){
                                lastStatusData.saveLasStatus(filteredStatuses.get(filteredStatuses.size()-1).getStatus(),stringToDate(filteredStatuses.get(filteredStatuses.size()-1).getTime()).toLocalTime().toSecondOfDay(),today);
                            }
                        }
                    }
                }, throwable -> {
                });

        disposables.add(disposable);
    }

    public void getAllDvirs(DvirViewModel dvirViewModel){
        Disposable disposable = repository
                .getAllDvirs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(dvirs -> {
                    for (Dvir dvir: dvirs) {
                        dvirViewModel.insertDvir(dvir);
                    }
                }, throwable -> {
                });

        disposables.add(disposable);
    }

    public void getAllSignatures(SignatureViewModel signatureViewModel){
        Disposable disposable = repository
                .getAllSignatures()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(signatureEntities -> {
                    for (Signature entity: signatureEntities) {
                        Picasso.get().load(entity.getSignatureBitmap()).into(new Target() {
                            @Override
                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                signatureViewModel.insertSignature(new SignatureEntity(bitmap,entity.getDay().toString()));
                            }

                            @Override
                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                            }

                            @Override
                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                            }
                        });
                    }
                }, throwable -> {
                });

        disposables.add(disposable);

    }

    public void getAllGenerals(GeneralViewModel generalViewModel){
        List<GeneralEntity> allGenEnteties = new ArrayList<>();
        Disposable disposable = repository
                .getAllGenerals()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(generalEntities -> {
                    for (GeneralEntity generalEntity:generalEntities) {
                        generalViewModel.insertGeneral(generalEntity);
                    }
                }, throwable -> {

                });

        disposables.add(disposable);
    }
    @Override
    protected void onCleared() {
        disposables.clear();
        disposables.dispose();
        super.onCleared();
    }
}
