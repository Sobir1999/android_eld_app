package com.iosix.eldblesample.retrofit;

import com.iosix.eldblesample.models.Student;

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
}
