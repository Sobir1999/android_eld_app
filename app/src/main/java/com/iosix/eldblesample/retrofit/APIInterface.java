package com.iosix.eldblesample.retrofit;

import com.iosix.eldblesample.models.SendExampleModelData;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {

    @GET("students")
    Call<ArrayList<Student>> getStudents();

    @POST("api/token/")
    Call<Student> createUser(@Body Student student);

    @POST("event/")
    Call<SendExampleModelData> sendData(@Body SendExampleModelData model);
}
