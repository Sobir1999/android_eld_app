package com.iosix.eldblesample.retrofit;

import androidx.lifecycle.LiveData;

import com.iosix.eldblesample.models.Data;
import com.iosix.eldblesample.models.Driver;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.User;
import com.iosix.eldblesample.models.Vehicle;
import com.iosix.eldblesample.models.VehicleData;
import com.iosix.eldblesample.models.eld_records.BufferRecord;
import com.iosix.eldblesample.models.eld_records.CachedEngineRecord;
import com.iosix.eldblesample.models.eld_records.CashedMotionRecord;
import com.iosix.eldblesample.models.eld_records.DriverBehaviorRecord;
import com.iosix.eldblesample.models.eld_records.EmissionsRecord;
import com.iosix.eldblesample.models.eld_records.EngineLiveRecord;
import com.iosix.eldblesample.models.eld_records.FuelRecord;
import com.iosix.eldblesample.models.eld_records.LiveDataRecord;
import com.iosix.eldblesample.models.eld_records.NewTimeRecord;
import com.iosix.eldblesample.models.eld_records.PowerOnRecord;
import com.iosix.eldblesample.models.eld_records.TransmissionRecord;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.OPTIONS;
import retrofit2.http.POST;

public interface APIInterface {

    @OPTIONS("api/drivers/")
    Call<User> getUser();

    @POST("api/token/")
    Call<ResponseBody> createUser(@Body Student student);

    @FormUrlEncoded
    @POST("api/token/refresh/")
    Call<ResponseBody> refreshToken(
            @Field("refresh") String refreshToken
    );

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















