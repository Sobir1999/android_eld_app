package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;

public class ConnectToEldDialog extends Dialog {
    private LinearLayout vehicleInfo, sendLog;
    private ConstraintLayout undintifiedDriving, faultCode;
    private TextView connect, cancel;
    private AlertDialogItemClickInterface alerrtDialogItemClickInterface;

    public ConnectToEldDialog(@NonNull Context context) {
        super(context);
    }

    public void setAlerrtDialogItemClickInterface(AlertDialogItemClickInterface alerrtDialogItemClickInterface) {
        this.alerrtDialogItemClickInterface = alerrtDialogItemClickInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.eld_custom_dialog_item);
        vehicleInfo = findViewById(R.id.idVehicleInfo);
        sendLog = findViewById(R.id.idSendDebugLog);
        undintifiedDriving = findViewById(R.id.idUndefinedDR);
        faultCode = findViewById(R.id.idFaultCode);
        cancel = findViewById(R.id.idEldDialCancel);
        connect = findViewById(R.id.idEldDialConnect);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alerrtDialogItemClickInterface != null) {
                    alerrtDialogItemClickInterface.onClick();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }
}
