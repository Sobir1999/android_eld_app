package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
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
    private TextView resultText, dataView;

    public SearchEldDeviceDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.connect_eld_progress_dialog);

//        progressBar = findViewById(R.id.idProgressBar1);
        resultText = findViewById(R.id.idScanResult);
        dataView = findViewById(R.id.idDataResult);

//        progressBar.setProgress(100);

//        resultText.setText(resultSearching);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageModel s) {
        resultText.append(s.getStatusView());
        dataView.append(s.getDataView());
    }

    public void setResultText(TextView resultText) {
        this.resultText = resultText;
    }

    public void setDataView(TextView dataView) {
        this.dataView = dataView;
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
