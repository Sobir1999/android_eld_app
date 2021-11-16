package com.iosix.eldblesample.retrofit;

import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.models.User;

import java.util.ArrayList;

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

    @OPTIONS("drivers/")
    Call<User> getUser();

    @POST("api/token/")
    Call<ResponseBody> createUser(@Body Student student);

    @POST("api/token/refresh/")
    Call<ResponseBody> refreshToken(
            @Field("refresh") String refreshToken
    );

        @POST("event/")
    Call<SendExampleModelData> sendData(@Body SendExampleModelData model);

}
