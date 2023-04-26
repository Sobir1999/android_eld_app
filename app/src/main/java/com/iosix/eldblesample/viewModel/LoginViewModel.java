package com.iosix.eldblesample.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.iosix.eldblesample.enums.StateLiveData;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.roomDatabase.repository.LoginRepository;
import com.iosix.eldblesample.utils.Constants;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginViewModel extends AndroidViewModel {

    private final LoginRepository loginRepository;
    private final CompositeDisposable disposables;
    public final StateLiveData<LoginResponse> studentMutableLiveData = new StateLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository();
        disposables = new CompositeDisposable();
    }

    public void makeLoginRequest(Student student) {
        Disposable disposable = loginRepository
                .getLoginResponse(student)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    if (data.isSuccessful()){
                        assert data.body() != null;
                        Constants.token = data.body().getAccessToken();
                        studentMutableLiveData.postSuccess(data.body());
                    }else {
                        studentMutableLiveData.postStatusCode(data.code());
                    }
                }, studentMutableLiveData::postError);
        studentMutableLiveData.postComplete();

        disposables.add(disposable);
    }

    public StateLiveData<LoginResponse> getLoginResponse(){
        return studentMutableLiveData;
    }

    @Override
    protected void onCleared() {
        disposables.dispose();
        disposables.clear();
        super.onCleared();
    }
}
