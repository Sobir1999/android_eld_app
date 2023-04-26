package com.iosix.eldblesample.activity;

import androidx.annotation.NonNull;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.shared_prefs.LastStatusData;

public class SettingActivity extends BaseActivity {

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
        TextView account = findViewById(R.id.idEditProfile);
        TextView language = findViewById(R.id.idLanguage);
        TextView nightMode = findViewById(R.id.idNightMode);
        TextView idCarrier = findViewById(R.id.idCarrier);
        TextView inspectionLock = findViewById(R.id.idInspectionModeLock);
        ImageView imageView = findViewById(R.id.idImageBack);

        LastStatusData lastStatusData = LastStatusData.getInstance(getApplicationContext());

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

        nightMode.setOnClickListener(view -> {
            Intent intent = new Intent(this,NightModeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        idCarrier.setOnClickListener(view -> {
            Intent intent = new Intent(this,CarrierInfoActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        inspectionLock.setOnClickListener(view -> {
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            TextView header = dialog.findViewById(R.id.idHeaderTv);
            TextView idGeneralTitleTv = dialog.findViewById(R.id.idGeneralTitleTv);
            EditText editText =  dialog.findViewById(R.id.idDialogEdit);
            TextView save = dialog.findViewById(R.id.idDialogSave);
            TextView cancel = dialog.findViewById(R.id.idDialogCancel);

            header.setText(R.string.inspection_mode_lock);
            idGeneralTitleTv.setText("Please,Create password");
            editText.setHint("Please,Create password");
            save.setOnClickListener(view1 -> {
                if (!editText.getText().toString().equals("")){
                    lastStatusData.saveLastInspectionLockPassword(editText.getText().toString());
                    Toast.makeText(this,"Password saved successfully!",Toast.LENGTH_SHORT).show();
                    dialog.cancel();
                }else {
                    Toast.makeText(this,"Please Create password!",Toast.LENGTH_SHORT).show();
                }
            });

            cancel.setOnClickListener(view1 -> {
                dialog.cancel();
            });

            dialog.show();
        });

    }
}