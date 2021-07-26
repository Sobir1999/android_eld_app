package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;

public class LoginActivity extends BaseActivity {

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
        TextView forgot_password = findViewById(R.id.idTextViewForgotPassword);

        forgot_password.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (login.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"Please fill all free spaces!",Toast.LENGTH_SHORT).show();
                }else {
                    Intent intent = new Intent(LoginActivity.this,SplashActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        });
    }
}