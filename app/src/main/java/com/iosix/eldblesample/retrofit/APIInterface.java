package com.iosix.eldblesample.retrofit;

import android.net.Uri;

import androidx.lifecycle.LiveData;

import com.google.gson.annotations.SerializedName;
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

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

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

    @POST("api/event/poweron/")
    Call<PowerOnRecord> sendPoweron(@Body PowerOnRecord record);

    @POST("api/event/newtime/")
    Call<NewTimeRecord> sendNewtime(@Body NewTimeRecord record);

    @POST("api/event/enginecache/")
    Call<CachedEngineRecord> sendEnginecache(@Body CachedEngineRecord record);

    @POST("api/event/motion/")
    Call<CashedMotionRecord> sendMotion(@Body CashedMotionRecord record);

    @POST("api/event/buffer/")
    Call<BufferRecord> sendBuffer(@Body BufferRecord record);

    @POST("api/event/live/")
    Call<LiveDataRecord> sendLive(@Body LiveDataRecord record);

    @POST("api/event/driverbehavior/")
    Call<DriverBehaviorRecord> sendDriverbehavior(@Body DriverBehaviorRecord record);

    @POST("api/event/emission/")
    Call<EmissionsRecord> sendEmission(@Body EmissionsRecord record);

    @POST("api/event/enginelive/")
    Call<EngineLiveRecord> sendEnginelive(@Body EngineLiveRecord record);

    @POST("api/event/fuel/")
    Call<FuelRecord> sendFuel(@Body FuelRecord record);

    @POST("api/event/transmission/")
    Call<TransmissionRecord> sendTransmission(@Body TransmissionRecord record);
}















