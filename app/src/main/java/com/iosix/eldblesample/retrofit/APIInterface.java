package com.iosix.eldblesample.retrofit;

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

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @OPTIONS("api/drivers/")
    Call<User> getUser();

    @POST("api/token/")
    Call<LoginResponse> createUser(@Body Student student);

    @FormUrlEncoded
    @POST("api/token/refresh/")
    Call<LoginResponse> refreshToken(
            @Field("refresh") String refreshToken
    );

    @POST("api/event/apk_version/")
    Call<ApkVersion> sendApkVersion(@Body ApkVersion apkVersion);

    @POST("api/event/local_data/")
    Call<LiveDataEntitiy> sendLocalData(@Body LiveDataEntitiy liveDataEntitiy);

    @POST("api/event/eld_confirmation/")
    Call<Eld> sendEldNum(@Body Eld eld);

    @POST("api/event/trailer_of_driver/")
    Call<TrailersEntity> sendTrailer(@Body TrailNubmer trailer_number);

    @Multipart
    @POST("api/event/sign/")
    Call<MultipartBody.Part> sendSignature(@Part MultipartBody.Part uri);

    @POST("api/event/dvir/")
    Call<SendDvir> sendDvir(@Body SendDvir sendDvir);

    @POST("api/event/general_info/")
    Call<GeneralEntity> sendGeneralInfo(@Body GeneralEntity generalEntity);

    @GET("api/event/vehicle_of_driver/")
    Call<VehicleList> getVehicle();

    @GET("api/event/co_driver/")
    Call<User> getCoDriver();

    @POST("api/event/status/")
    Call<Status> postStatus(@Body Status status);

    @GET("api/drivers/")
    Call<Data> getAllDrivers();

    @GET("api/vehicles/")
    Call<VehicleData> getAllVehicles();

    @POST("api/event/live/")
    Call<LiveDataRecord> sendLive(@Body LiveDataRecord record);

    @POST("api/event/inspect/")
    Single<String> sendPdf(
            @Body RequestBody body
    );

    @GET("/api/event/general_last/")
    void getAllGenerals();

    @GET("/api/event/logs_last/")
    void getAllLogs();

    @GET("/api/event/dvir_last/")
    void getAllDvirs();

    @GET("/api/event/sign_last/")
    void getAllSignatures();
}















