package com.iosix.eldblesample.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.LanguageFragment;

public class SettingActivity extends BaseActivity {

    private TextView account,language;
    private ImageView imageView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();

        clickButtons();
    }

    void clickButtons(){
        account = findViewById(R.id.idEditProfile);
        language = findViewById(R.id.idLanguage);
        imageView = findViewById(R.id.idImageBack);

        imageView.setOnClickListener(view -> onBackPressed());

        account.setOnClickListener(view -> {
            Intent intent = new Intent(this,AccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        language.setOnClickListener(view -> {
            Intent intent = new Intent(this,LanguageActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}