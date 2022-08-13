package com.iosix.eldblesample.enums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class StateData<T> {

    @NonNull
    private DataStatus status;

    @Nullable
    private T data;

    @Nullable
    private Throwable error;

    @NonNull long statusCode;

    public StateData() {
        this.status = DataStatus.CREATED;
        this.data = null;
        this.error = null;
        this.statusCode = 0;
    }

    public StateData<T> loading() {
        this.status = DataStatus.LOADING;
        this.data = null;
        this.error = null;
        return this;
    }

    public StateData<T> success(@NonNull T data) {
        this.status = DataStatus.SUCCESS;
        this.data = data;
        this.error = null;
        return this;
    }

    public StateData<T> error(@NonNull Throwable error) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.error = error;
        return this;
    }

    public StateData<T> statusCode(@NonNull long statusCode) {
        this.status = DataStatus.ERROR;
        this.data = null;
        this.statusCode = statusCode;
        return this;
    }

    public StateData<T> complete() {
        this.status = DataStatus.COMPLETE;
        return this;
    }

    @NonNull
    public DataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }

    @Nullable
    public long getStatusCode() {
        return statusCode;
    }

    public enum DataStatus {
        CREATED,
        SUCCESS,
        ERROR,
        LOADING,
        COMPLETE
    }
}
