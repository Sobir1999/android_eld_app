package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.models.MessageModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchEldDeviceDialog extends Dialog {
    private ProgressBar progressBar;
    private String resultSearching = "";
    private TextView resultText;

    public SearchEldDeviceDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.connect_eld_progress_dialog);

        resultText = findViewById(R.id.idScanResult);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageModel s) {
        resultText.append(s.getStatusView());
        if (resultText.getText() != "" && resultText.getText() != "Bluetooth enabled - now scanning for ELD\n"){
            final Handler handler = new Handler();
            handler.postDelayed(this::dismiss,5000);
        }
    }

    public void setResultText(TextView resultText) {
        this.resultText = resultText;
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
