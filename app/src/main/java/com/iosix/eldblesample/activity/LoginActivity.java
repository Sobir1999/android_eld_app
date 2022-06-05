package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.broadcasts.NetworkConnectionLiveData;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.shared_prefs.LastStopSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private APIInterface apiInterface;
    private EldJsonViewModel eldJsonViewModel;
    private SessionManager sessionManager;
    private ProgressDialog mProgress;
    private LastStopSharedPrefs lastStopSharedPrefs;
    private String time = "" + Calendar.getInstance().getTime();
    private String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private NetworkConnectionLiveData networkConnectionLiveData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();

        Button button = findViewById(R.id.idLoginButton);
        EditText login = findViewById(R.id.idEditTextLogin);
        EditText password = findViewById(R.id.idEditTextPassword);

        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);

        apiInterface = ApiClient.getClient().create(APIInterface.class);
        lastStopSharedPrefs = new LastStopSharedPrefs(this);
        networkConnectionLiveData = new NetworkConnectionLiveData(getApplicationContext());

        sessionManager = SessionManager.getInstance(getApplicationContext());
        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        button.setOnClickListener(v -> {

            networkConnectionLiveData.observe(LoginActivity.this,isConnected ->{
                if (isConnected){
                    mProgress.show();

                    if (login.getText().toString().equals("") || password.getText().toString().equals("")){
                        mProgress.cancel();
                        Toast.makeText(LoginActivity.this,"Please fill all free spaces!",Toast.LENGTH_SHORT).show();
                    }else {

                        eldJsonViewModel.getResponse(new Student(login.getText().toString(),password.getText().toString())).observe(this,loginResponse -> {
                            if (loginResponse != null){
                                sessionManager.saveAccessToken(loginResponse.getAccessToken());
                                sessionManager.saveToken(loginResponse.getrefreshToken());
                                sessionManager.saveEmail(login.getText().toString());
                                sessionManager.savePassword(password.getText().toString());
                                lastStopSharedPrefs.saveLastStopTime(getCurrentSeconds());
                                lastStopSharedPrefs.saveLastStopDate(today);
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtra("JSON",1);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                mProgress.cancel();
                            }else {
                                mProgress.cancel();
                                Toast.makeText(LoginActivity.this, "No active account found with the given credentials", Toast.LENGTH_SHORT).show();
                            }
                        });
//                        apiInterface.createUser(new Student(login.getText().toString(),password.getText().toString()))
//                                .enqueue(new Callback<ResponseBody>() {
//                                    @Override
//                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                                        Gson gson = new Gson();
//                                        try {
//
//                                            if(response.isSuccessful()){
//                                                LoginResponse loginResponse = gson.fromJson(response.body().string(), LoginResponse.class);
//                                                sessionManager.saveAccessToken(loginResponse.getAccessToken());
//                                                sessionManager.saveToken(loginResponse.getrefreshToken());
//                                                sessionManager.saveEmail(login.getText().toString());
//                                                sessionManager.savePassword(password.getText().toString());
//                                                lastStopSharedPrefs.saveLastStopTime(getCurrentSeconds());
//                                                lastStopSharedPrefs.saveLastStopDate(today);
//                                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                                                intent.putExtra("JSON",1);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                startActivity(intent);
//                                                mProgress.cancel();
//                                            }else {
//                                                mProgress.cancel();
//                                                Toast.makeText(LoginActivity.this, "No active account found with the given credentials", Toast.LENGTH_SHORT).show();
//                                            }
//                                            mProgress.cancel();
//                                        } catch (IOException e) {
//                                            e.printStackTrace();
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                                        mProgress.cancel();
//                                    }
//                                });
                    }
                }else {
                    mProgress.cancel();
                    Toast.makeText(LoginActivity.this,"CHeck internet connection",Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override public void onStop() {
        super.onStop();
        if (mProgress != null) {
            mProgress.dismiss();
            mProgress = null;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
    }
}