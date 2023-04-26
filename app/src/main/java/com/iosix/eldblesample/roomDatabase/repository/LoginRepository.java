package com.iosix.eldblesample.roomDatabase.repository;

import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Response;

public class LoginRepository {
    private final APIInterface apiInterface;
    ApiClient apiClient;

    public LoginRepository(){
        apiClient = new ApiClient();
        apiInterface = apiClient.getClient().create(APIInterface.class);
    }

    public Single<Response<LoginResponse>> getLoginResponse(Student student) {
        return apiInterface.createUser(student);
    }
}
