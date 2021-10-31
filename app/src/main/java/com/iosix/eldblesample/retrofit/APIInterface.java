package com.iosix.eldblesample.retrofit;

import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("students")
    Call<ArrayList<Student>> getStudents();

    @POST("api/token/")
    Call<ResponseBody> createUser(@Body Student student);

    @POST("eld/")
    Call<SendExampleModelData> sendData(@Body SendExampleModelData model);

//    @POST("event/")
//    Call<SendExampleModelData> sendDataEx(@Body ExampleSendModels model);
}
