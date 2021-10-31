package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LoginResponse {


    @SerializedName("refresh")
    private String refreshToken;

    @SerializedName("access")
    private String accessToken;

    public LoginResponse(String refreshToken,String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getrefreshToken() {
        return refreshToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setrefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
