package com.iosix.eldblesample.retrofit;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.Driver;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.SendDvir;
import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.Vehicle;
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
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private static APIInterface apiInterface;
    private final MutableLiveData<LoginResponse> studentMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<LoginResponse> refreshTokenResponse = new MutableLiveData<>();
    private final MutableLiveData<User> getUserInfo = new MutableLiveData<>();
    private final MutableLiveData<Data> getAllUsers = new MutableLiveData<>();
    private final MutableLiveData<VehicleData> getAllVehicles = new MutableLiveData<>();
    private final MutableLiveData<Status> getStatus = new MutableLiveData<>();
    private final MutableLiveData<VehicleList> getVehicle = new MutableLiveData<>();
    private final MutableLiveData<User> getCoDriver = new MutableLiveData<>();
    private final MutableLiveData<GeneralEntity> getGeneralInfo = new MutableLiveData<>();
    private final MutableLiveData<TrailersEntity> getTrailer = new MutableLiveData<>();
    private final MutableLiveData<SendDvir> dvir = new MutableLiveData<>();
    private final MutableLiveData<MultipartBody.Part> signature = new MutableLiveData<>();
    private final MutableLiveData<Eld> eldMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<LiveDataEntitiy> liveDataEntitiyMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<ApkVersion> apkVersionMutableLiveData = new MutableLiveData<>();

    private static ApiRepository newsRepository;

    public static ApiRepository getInstance(){
        if (newsRepository == null){
            newsRepository = new ApiRepository();
        }
        return newsRepository;
    }

    public ApiRepository(){
        apiInterface = ApiClient.getClient().create(APIInterface.class);
    }


    public void sendBufferRecord(BufferRecord record) {
        new sendBufferRecord(apiInterface).execute(record);
    }

    private static class sendBufferRecord extends AsyncTask<BufferRecord, Void, Void> {
        private APIInterface anInterface;

        public sendBufferRecord(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(BufferRecord... records) {
            anInterface.sendBuffer(records[0]);
            return null;
        }
    }


    public void sendEnginecache(CachedEngineRecord record) {
        new sendEnginecache(apiInterface).execute(record);
    }

    private static class sendEnginecache extends AsyncTask<CachedEngineRecord, Void, Void> {
        private APIInterface anInterface;

        public sendEnginecache(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(CachedEngineRecord... records) {
            anInterface.sendEnginecache(records[0]);
            return null;
        }
    }


    public void sendPoweron(PowerOnRecord record) {
        new sendPoweron(apiInterface).execute(record);
    }

    private static class sendPoweron extends AsyncTask<PowerOnRecord, Void, Void> {
        private APIInterface anInterface;

        public sendPoweron(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(PowerOnRecord... records) {
            anInterface.sendPoweron(records[0]);
            return null;
        }
    }

    public void sendNewtime(NewTimeRecord record) {
        new sendNewtime(apiInterface).execute(record);
    }

    private static class sendNewtime extends AsyncTask<NewTimeRecord, Void, Void> {
        private APIInterface anInterface;

        public sendNewtime(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(NewTimeRecord... records) {
            anInterface.sendNewtime(records[0]);
            return null;
        }
    }

    public void sendMotion(CashedMotionRecord record) {
        new sendMotion(apiInterface).execute(record);
    }

    private static class sendMotion extends AsyncTask<CashedMotionRecord, Void, Void> {
        private APIInterface anInterface;

        public sendMotion(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(CashedMotionRecord... records) {
            anInterface.sendMotion(records[0]);
            return null;
        }
    }

    public void sendLive(LiveDataRecord record) {
        new sendLive(apiInterface).execute(record);
    }

    private static class sendLive extends AsyncTask<LiveDataRecord, Void, Void> {
        private APIInterface anInterface;

        public sendLive(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(LiveDataRecord... records) {
            anInterface.sendLive(records[0]);
            return null;
        }
    }

    public void sendDriverbehavior(DriverBehaviorRecord record) {
        new sendDriverbehavior(apiInterface).execute(record);
    }

    private static class sendDriverbehavior extends AsyncTask<DriverBehaviorRecord, Void, Void> {
        private APIInterface anInterface;

        public sendDriverbehavior(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(DriverBehaviorRecord... records) {
            anInterface.sendDriverbehavior(records[0]);
            return null;
        }
    }

    public void sendEmission(EmissionsRecord record) {
        new sendEmission(apiInterface).execute(record);
    }

    private static class sendEmission extends AsyncTask<EmissionsRecord, Void, Void> {
        private APIInterface anInterface;

        public sendEmission(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(EmissionsRecord... records) {
            anInterface.sendEmission(records[0]);
            return null;
        }
    }

    public void sendEnginelive(EngineLiveRecord record) {
        new sendEnginelive(apiInterface).execute(record);
    }

    private static class sendEnginelive extends AsyncTask<EngineLiveRecord, Void, Void> {
        private APIInterface anInterface;

        public sendEnginelive(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(EngineLiveRecord... records) {
            anInterface.sendEnginelive(records[0]);
            return null;
        }
    }

    public void sendFuel(FuelRecord record) {
        new sendFuel(apiInterface).execute(record);
    }

    private static class sendFuel extends AsyncTask<FuelRecord, Void, Void> {
        private APIInterface anInterface;

        public sendFuel(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(FuelRecord... records) {
            anInterface.sendFuel(records[0]);
            return null;
        }
    }

    public void sendTransmission(TransmissionRecord record) {
        new sendTransmission(apiInterface).execute(record);
    }

    private static class sendTransmission extends AsyncTask<TransmissionRecord, Void, Void> {
        private APIInterface anInterface;

        public sendTransmission(APIInterface anInterface) {
            this.anInterface = anInterface;
        }

        @Override
        protected Void doInBackground(TransmissionRecord... records) {
            anInterface.sendTransmission(records[0]);
            return null;
        }
    }


    public MutableLiveData<LoginResponse> getLoginResponse(Student student){

        apiInterface.createUser(student).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    studentMutableLiveData.postValue(response.body());
                }else {
                    studentMutableLiveData.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                studentMutableLiveData.postValue(null);
            }
        });

        return studentMutableLiveData;
    }


    public MutableLiveData<User> getUser(){
        apiInterface.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    getUserInfo.postValue(response.body());
                }else {
                    getUserInfo.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getUserInfo.postValue(null);
            }
        });
        return getUserInfo;
    }

    public MutableLiveData<Data> getAllDrivers(){

        apiInterface.getAllDrivers().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()){
                    getAllUsers.postValue(response.body());
                }else {
                    getAllUsers.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                getAllUsers.postValue(null);
            }
        });
        return getAllUsers;
    }


    public MutableLiveData<VehicleData> getAllVehicles(){
        apiInterface.getAllVehicles().enqueue(new Callback<VehicleData>() {
            @Override
            public void onResponse(Call<VehicleData> call, Response<VehicleData> response) {
                if (response.isSuccessful()){
                    getAllVehicles.postValue(response.body());
                }else {
                    getAllVehicles.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VehicleData> call, Throwable t) {
                getAllVehicles.postValue(null);
            }
        });
        return getAllVehicles;
    }

    public MutableLiveData<LoginResponse> refreshToken(String refreshToken){
        apiInterface.refreshToken(refreshToken).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()){
                    refreshTokenResponse.postValue(response.body());
                }else refreshTokenResponse.postValue(null);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                refreshTokenResponse.postValue(null);
            }
        });
        return refreshTokenResponse;
    }

    public MutableLiveData<Status> postStatus(Status status){
        apiInterface.postStatus(status).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()){
                    getStatus.postValue(response.body());
                }else {
                    getStatus.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                getStatus.postValue(null);
            }
        });
        return getStatus;
    }

    public MutableLiveData<VehicleList> getVehicle(){
        apiInterface.getVehicle().enqueue(new Callback<VehicleList>() {
            @Override
            public void onResponse(Call<VehicleList> call, Response<VehicleList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        getVehicle.postValue(response.body());
                    }else {
                        getCoDriver.postValue(null);
                    }
                }else {
                    getVehicle.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<VehicleList> call, Throwable t) {
                getVehicle.postValue(null);
            }
        });
        return getVehicle;
    }

    public MutableLiveData<User> getCoDriver(){
        apiInterface.getCoDriver().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        getCoDriver.postValue(response.body());
                    }else {
                        getCoDriver.postValue(null);
                    }
                }else {
                    getCoDriver.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                getCoDriver.postValue(null);
            }
        });
        return getCoDriver;
    }

    public MutableLiveData<GeneralEntity> sendGenerelInfo(GeneralEntity entity){
        apiInterface.sendGeneralInfo(entity).enqueue(new Callback<GeneralEntity>() {
            @Override
            public void onResponse(Call<GeneralEntity> call, Response<GeneralEntity> response) {
                if (response.isSuccessful()){
                    getGeneralInfo.postValue(response.body());
                }else {
                    getGeneralInfo.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<GeneralEntity> call, Throwable t) {
                getGeneralInfo.postValue(null);
            }
        });
        return getGeneralInfo;
    }

    public MutableLiveData<TrailersEntity> sendTrailer(TrailNubmer trailNubmer){
       apiInterface.sendTrailer(trailNubmer).enqueue(new Callback<TrailersEntity>() {
           @Override
           public void onResponse(Call<TrailersEntity> call, Response<TrailersEntity> response) {
               if (response.isSuccessful()){
                    getTrailer.postValue(response.body());
               }else {
                   getTrailer.postValue(null);
               }
           }

           @Override
           public void onFailure(Call<TrailersEntity> call, Throwable t) {
                getTrailer.postValue(null);
           }
       });
       return getTrailer;
    }

    public MutableLiveData<SendDvir> sendDvir(SendDvir sendDvir){
        apiInterface.sendDvir(sendDvir).enqueue(new Callback<SendDvir>() {
            @Override
            public void onResponse(Call<SendDvir> call, Response<SendDvir> response) {
                if (response.isSuccessful()){
                    dvir.postValue(response.body());
                }else {
                    dvir.postValue(null);
                    try {
                        Log.d("Adverse Diving",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SendDvir> call, Throwable t) {
                dvir.postValue(null);
            }
        });
        return dvir;
    }

    public MutableLiveData<MultipartBody.Part> sendSignature(MultipartBody.Part body){
        apiInterface.sendSignature(body).enqueue(new Callback<MultipartBody.Part>() {
            @Override
            public void onResponse(Call<MultipartBody.Part> call, Response<MultipartBody.Part> response) {
                if (response.isSuccessful()){
                    signature.postValue(response.body());
                }else {
                    signature.postValue(null);
                }
            }

            @Override
            public void onFailure(Call<MultipartBody.Part> call, Throwable t) {
                signature.postValue(null);
            }
        });
        return signature;
    }

    public MutableLiveData<Eld> sendEldNum(Eld eld){
        apiInterface.sendEldNum(eld).enqueue(new Callback<Eld>() {
            @Override
            public void onResponse(Call<Eld> call, Response<Eld> response) {
                if (response.isSuccessful()){
                    eldMutableLiveData.postValue(response.body());
                }else eldMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<Eld> call, Throwable t) {
                eldMutableLiveData.postValue(null);
            }
        });
        return eldMutableLiveData;
    }


    public MutableLiveData<LiveDataEntitiy> sendLocalData(LiveDataEntitiy liveDataEntitiy){
        apiInterface.sendLocalData(liveDataEntitiy).enqueue(new Callback<LiveDataEntitiy>() {
            @Override
            public void onResponse(Call<LiveDataEntitiy> call, Response<LiveDataEntitiy> response) {
                if (response.isSuccessful()){
                    liveDataEntitiyMutableLiveData.postValue(response.body());
                }else liveDataEntitiyMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<LiveDataEntitiy> call, Throwable t) {
                liveDataEntitiyMutableLiveData.postValue(null);
            }
        });
        return liveDataEntitiyMutableLiveData;
    }

    public MutableLiveData<ApkVersion> sendApkVersion(ApkVersion apkVersion){
        apiInterface.sendApkVersion(apkVersion).enqueue(new Callback<ApkVersion>() {
            @Override
            public void onResponse(Call<ApkVersion> call, Response<ApkVersion> response) {
                if (response.isSuccessful()){
                    apkVersionMutableLiveData.postValue(response.body());
                }else apkVersionMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<ApkVersion> call, Throwable t) {
                apkVersionMutableLiveData.postValue(null);
            }
        });
        return apkVersionMutableLiveData;
    }

}
