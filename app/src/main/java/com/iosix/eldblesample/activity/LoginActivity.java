package com.iosix.eldblesample.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.se.omapi.Session;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.Gson;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.models.LoginResponse;
import com.iosix.eldblesample.models.Student;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private EldJsonViewModel eldJsonViewModel;
    private APIInterface apiInterface;
    private SessionManager sessionManager;
    private ProgressDialog mProgress;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        super.initView();
        getWindow().setStatusBarColor(ActivityCompat.getColor(this,R.color.example));

        Button button = findViewById(R.id.idLoginButton);
        EditText login = findViewById(R.id.idEditTextLogin);
        EditText password = findViewById(R.id.idEditTextPassword);
        TextView forgot_password = findViewById(R.id.idTextViewForgotPassword);

        eldJsonViewModel = new EldJsonViewModel(this.getApplication());
        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);

        apiInterface = ApiClient.getClient().create(APIInterface.class);

        sessionManager = new SessionManager(this);

        mProgress = new ProgressDialog(this);
        mProgress.setTitle("Processing...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        forgot_password.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.show();
                if (isConnected()){

                    if (login.getText().toString().equals("") || password.getText().toString().equals("")){
                        mProgress.cancel();
                        Toast.makeText(LoginActivity.this,"Please fill all free spaces!",Toast.LENGTH_SHORT).show();
                    }else {
                        apiInterface.createUser(new Student(login.getText().toString(),password.getText().toString()))
                            .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                Gson gson = new Gson();
                                try {
                                    assert response.body() != null;

                                    if(response.isSuccessful()){
                                        LoginResponse loginResponse = gson.fromJson(response.body().string(), LoginResponse.class);
                                        Log.d("JSON", "onResponse: " +loginResponse.getrefreshToken());
                                        Log.d("JSON", "onResponse: " +loginResponse.getAccessToken());
                                        sessionManager.saveAccessToken(loginResponse.getAccessToken());
                                        sessionManager.saveToken(loginResponse.getrefreshToken());
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        intent.putExtra("JSON",1);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }else {
                                        mProgress.cancel();
                                        Toast.makeText(LoginActivity.this, "No active account found with the given credentials", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                Log.d("JSON", "onResponse: " + t.getMessage());

                            }
                        });
                    }
                }else {
                    mProgress.cancel();
                    Toast.makeText(LoginActivity.this,"CHeck internet connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}