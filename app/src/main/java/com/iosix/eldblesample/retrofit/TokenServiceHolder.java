package com.iosix.eldblesample.retrofit;

import androidx.annotation.Nullable;

public class TokenServiceHolder {

    APIInterface tokenService = null;

    @Nullable
    public APIInterface get() {
        return tokenService;
    }

    public void set(APIInterface tokenService) {
        this.tokenService = tokenService;
    }
}

