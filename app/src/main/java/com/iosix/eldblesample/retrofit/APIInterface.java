package com.iosix.eldblesample.retrofit;

import android.graphics.Bitmap;

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

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
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
    Single<User> getUser();

    @POST("api/token/")
    Single<Response<LoginResponse>> createUser(@Body Student student);

    @POST("api/token/refresh/")
    Single<LoginResponse> refreshToken(
            @Field("refresh") String refreshToken
    );

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
    Single<SendDvir> sendDvir(@Body SendDvir sendDvir);

    @POST("api/event/general_info/")
    Single<GeneralEntity> sendGeneralInfo(@Body GeneralEntity generalEntity);

    @GET("api/event/vehicle_of_driver/")
    Single<VehicleList> getVehicle();

    @GET("api/event/co_driver/")
    Single<User> getCoDriver();

    @POST("api/event/status/")
    Single<Status> postStatus(@Body Status status);

    @GET("api/drivers/")
    Single<Data> getAllDrivers();

    @GET("api/vehicles/")
    Single<VehicleData> getAllVehicles();

    @POST("api/event/live/")
    Single<LiveDataRecord> sendLive(@Body LiveDataRecord record);

    @POST("api/event/inspect/")
    Single<String> sendPdf(
            @Body RequestBody body
    );

    @GET("/api/event/general_last/")
    Single<List<GeneralEntity>> getAllGenerals();

    @GET("/api/event/logs_last/")
    Single<List<LogEntity>> getAllLogs();

    @GET("/api/event/dvir_last/")
    Single<List<DvirEntity>> getAllDvirs();

    @GET("/api/event/sign_last/")
    Single<List<SignatureEntity>> getAllSignatures();
}















