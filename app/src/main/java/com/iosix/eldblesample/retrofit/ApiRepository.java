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
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LiveDataEntitiy;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.io.IOException;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private static APIInterface apiInterface;
    private final StateLiveData<LoginResponse> studentMutableLiveData = new StateLiveData<>();
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

    public ApiRepository() {
        apiInterface = ApiClient.getClient().create(APIInterface.class);
    }

    public void sendLive(LiveDataRecord record) {
        apiInterface.sendLive(record).enqueue(new Callback<LiveDataRecord>() {
            @Override
            public void onResponse(Call<LiveDataRecord> call, Response<LiveDataRecord> response) {
            }

            @Override
            public void onFailure(Call<LiveDataRecord> call, Throwable t) {
            }
        });
    }

    public Single<String> sendPdf(RequestBody body) {
        return apiInterface.sendPdf(body);
    }

    public void getLoginResponse(Student student) {
        studentMutableLiveData.postLoading();
        apiInterface.createUser(student).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    studentMutableLiveData.postSuccess(response.body());
                } else {
                    studentMutableLiveData.postStatusCode(response.code());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                studentMutableLiveData.postError(t);
            }
        });
    }

    public StateLiveData<LoginResponse> getLoginState() {
        return studentMutableLiveData;
    }

    public MutableLiveData<User> getUser() {
        apiInterface.getUser().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    getUserInfo.postValue(response.body());
                } else {
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

    public MutableLiveData<Data> getAllDrivers() {

        apiInterface.getAllDrivers().enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful()) {
                    getAllUsers.postValue(response.body());
                } else {
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


    public MutableLiveData<VehicleData> getAllVehicles() {
        apiInterface.getAllVehicles().enqueue(new Callback<VehicleData>() {
            @Override
            public void onResponse(Call<VehicleData> call, Response<VehicleData> response) {
                if (response.isSuccessful()) {
                    getAllVehicles.postValue(response.body());
                } else {
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

    public MutableLiveData<LoginResponse> refreshToken(String refreshToken) {
        apiInterface.refreshToken(refreshToken).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    refreshTokenResponse.postValue(response.body());
                } else refreshTokenResponse.postValue(null);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                refreshTokenResponse.postValue(null);
            }
        });
        return refreshTokenResponse;
    }

    public MutableLiveData<Status> postStatus(Status status) {
        apiInterface.postStatus(status).enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    getStatus.postValue(response.body());
                } else {
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

    public MutableLiveData<VehicleList> getVehicle() {
        apiInterface.getVehicle().enqueue(new Callback<VehicleList>() {
            @Override
            public void onResponse(Call<VehicleList> call, Response<VehicleList> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        getVehicle.postValue(response.body());
                    } else {
                        getCoDriver.postValue(null);
                    }
                } else {
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

    public MutableLiveData<User> getCoDriver() {
        apiInterface.getCoDriver().enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        getCoDriver.postValue(response.body());
                    } else {
                        getCoDriver.postValue(null);
                    }
                } else {
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

    public MutableLiveData<GeneralEntity> sendGenerelInfo(GeneralEntity entity) {
        apiInterface.sendGeneralInfo(entity).enqueue(new Callback<GeneralEntity>() {
            @Override
            public void onResponse(Call<GeneralEntity> call, Response<GeneralEntity> response) {
                if (response.isSuccessful()) {
                    getGeneralInfo.postValue(response.body());
                } else {
                    getGeneralInfo.postValue(null);
                    try {
                        Log.d("Adverse Diving", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralEntity> call, Throwable t) {
                getGeneralInfo.postValue(null);
            }
        });
        return getGeneralInfo;
    }

    public MutableLiveData<TrailersEntity> sendTrailer(TrailNubmer trailNubmer) {
        apiInterface.sendTrailer(trailNubmer).enqueue(new Callback<TrailersEntity>() {
            @Override
            public void onResponse(Call<TrailersEntity> call, Response<TrailersEntity> response) {
                if (response.isSuccessful()) {
                    getTrailer.postValue(response.body());
                } else {
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

    public MutableLiveData<SendDvir> sendDvir(SendDvir sendDvir) {
        apiInterface.sendDvir(sendDvir).enqueue(new Callback<SendDvir>() {
            @Override
            public void onResponse(Call<SendDvir> call, Response<SendDvir> response) {
                if (response.isSuccessful()) {
                    dvir.postValue(response.body());
                } else {

                }
            }

            @Override
            public void onFailure(Call<SendDvir> call, Throwable t) {
                dvir.postValue(null);
            }
        });
        return dvir;
    }

    public MutableLiveData<MultipartBody.Part> sendSignature(MultipartBody.Part body) {
        apiInterface.sendSignature(body).enqueue(new Callback<MultipartBody.Part>() {
            @Override
            public void onResponse(Call<MultipartBody.Part> call, Response<MultipartBody.Part> response) {
                if (response.isSuccessful()) {
                    signature.postValue(response.body());
                } else {
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

    public MutableLiveData<Eld> sendEldNum(Eld eld) {
        apiInterface.sendEldNum(eld).enqueue(new Callback<Eld>() {
            @Override
            public void onResponse(Call<Eld> call, Response<Eld> response) {
                if (response.isSuccessful()) {
                    eldMutableLiveData.postValue(response.body());
                } else eldMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<Eld> call, Throwable t) {
                eldMutableLiveData.postValue(null);
            }
        });
        return eldMutableLiveData;
    }


    public MutableLiveData<LiveDataEntitiy> sendLocalData(LiveDataEntitiy liveDataEntitiy) {
        apiInterface.sendLocalData(liveDataEntitiy).enqueue(new Callback<LiveDataEntitiy>() {
            @Override
            public void onResponse(Call<LiveDataEntitiy> call, Response<LiveDataEntitiy> response) {
                if (response.isSuccessful()) {
                    liveDataEntitiyMutableLiveData.postValue(response.body());
                } else liveDataEntitiyMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<LiveDataEntitiy> call, Throwable t) {
                liveDataEntitiyMutableLiveData.postValue(null);
            }
        });
        return liveDataEntitiyMutableLiveData;
    }

    public MutableLiveData<ApkVersion> sendApkVersion(ApkVersion apkVersion) {
        apiInterface.sendApkVersion(apkVersion).enqueue(new Callback<ApkVersion>() {
            @Override
            public void onResponse(Call<ApkVersion> call, Response<ApkVersion> response) {
                if (response.isSuccessful()) {
                    apkVersionMutableLiveData.postValue(response.body());
                } else apkVersionMutableLiveData.postValue(null);
            }

            @Override
            public void onFailure(Call<ApkVersion> call, Throwable t) {
                apkVersionMutableLiveData.postValue(null);
            }
        });
        return apkVersionMutableLiveData;
    }

}


