package com.iosix.eldblesample.interfaces;

import com.iosix.eldblesample.models.Status;

import java.util.List;

public interface FetchStatusCallback {

    void onSuccess(Status status,List<Status> statuses);
    void onError(Throwable throwable);
}
