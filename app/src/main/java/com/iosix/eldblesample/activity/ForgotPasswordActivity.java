package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;

public class ForgotPasswordActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @Override
    public void initView() {
        super.initView();

        ImageView imageView = findViewById(R.id.idImageViewBack);
        Button button = findViewById(R.id.idResetPasswordButton);
        EditText editText = findViewById(R.id.idEditTextLogin);

        imageView.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        button.setOnClickListener(v -> {
            if (editText.getText().toString().equals("")){
                Toast.makeText(ForgotPasswordActivity.this,"Please create your Email address",Toast.LENGTH_SHORT).show();
            }
        });
    }
}