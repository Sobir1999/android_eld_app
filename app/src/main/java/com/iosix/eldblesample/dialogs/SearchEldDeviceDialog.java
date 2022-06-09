package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.models.MessageModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SearchEldDeviceDialog extends Dialog {
    private TextView resultText;
    private Context context;

    public SearchEldDeviceDialog(@NonNull Context context) {
        super(context);
        this.context = context;
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
