package com.iosix.eldblesample.retrofit;

import com.iosix.eldblesample.models.ApkVersion;
import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.LogList;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Dvir;
import com.iosix.eldblesample.models.Signature;
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
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface APIInterface {


    // Authentication
    @POST("api/token/")
    Single<Response<LoginResponse>> createUser(@Body Student student);

    @POST("api/token/refresh/")
    Single<LoginResponse> refreshToken(
            @Query("refresh") String refreshToken
    );

    @OPTIONS("api/eld/drivers/")
    Single<User> getUser();

    @POST("api/event/apk_version/")
    Single<ApkVersion> sendApkVersion(@Body ApkVersion apkVersion);

    @POST("api/event/local_data/")
    Single<LiveDataEntitiy> sendLocalData(@Body LiveDataEntitiy liveDataEntitiy);

    @POST("api/event/eld_confirmation/")
    Single<Eld> sendEldNum(@Body Eld eld);

    @POST("api/event/trailer_of_driver/")
    Single<TrailersEntity> sendTrailer(@Body TrailNubmer trailer_number);

    @Multipart
    @POST("api/event/sign/")
    Single<String> sendSignature(@Part MultipartBody.Part uri);

    @POST("api/event/dvir/")
    Single<Dvir> sendDvir(@Body Dvir sendDvir);

    @POST("api/event/general_info/")
    Single<GeneralEntity> sendGeneralInfo(@Body GeneralEntity generalEntity);

    @GET("api/event/vehicle_of_driver/")
    Single<VehicleList> getVehicle();

    @GET("api/event/co_driver/")
    Single<User> getCoDriver();

    @POST("api/event/status/")
    Single<Status> postStatus(@Body Status status);

    @GET("api/eld/drivers/")
    Single<Data> getAllDrivers();

    @GET("api/eld/vehicles/")
    Single<VehicleData> getAllVehicles();

    @POST("api/event/live/")
    Single<LiveDataRecord> sendLive(@Body LiveDataRecord record);

    @Multipart
    @POST("api/event/inspect/")
    Single<String> sendPdf(
            @Body RequestBody body
    );

    @GET("/api/event/general_last/")
    Single<List<GeneralEntity>> getAllGenerals();

    @GET("/api/event/logs_last/")
    Single<List<Status>> getAllLogs();

    @GET("/api/event/dvir_last/")
    Single<List<Dvir>> getAllDvirs();

    @GET("/api/event/sign_last/")
    Single<List<Signature>> getAllSignatures();
}















