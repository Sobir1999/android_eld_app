package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;
import com.iosix.eldblesample.shared_prefs.LastStatusData;

public class ConnectToEldDialog extends Dialog {
    private LinearLayout vehicleInfo, sendLog;
    private ConstraintLayout undintifiedDriving, faultCode;
    private TextView connect, cancel,idEldConnection,idIsConnected;
    private AlertDialogItemClickInterface alerrtDialogItemClickInterface;
    private String lastStatusData;

    public ConnectToEldDialog(@NonNull Context context, String lastStatusData) {
        super(context);
        this.lastStatusData = lastStatusData;
    }

    public void setAlerrtDialogItemClickInterface(AlertDialogItemClickInterface alerrtDialogItemClickInterface) {
        this.alerrtDialogItemClickInterface = alerrtDialogItemClickInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eld_custom_dialog_item);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        vehicleInfo = findViewById(R.id.idVehicleInfo);
        idEldConnection = findViewById(R.id.idEldConnection);
        idIsConnected = findViewById(R.id.idIsConnected);
        undintifiedDriving = findViewById(R.id.idUndefinedDR);
        cancel = findViewById(R.id.idEldDialCancel);
        connect = findViewById(R.id.idEldDialConnect);

        idIsConnected.setText(lastStatusData);

        if (lastStatusData.equals("Conncected to Eld")){
            connect.setText(R.string.disconnect);
        }else {
            connect.setText(R.string.connect);
        }

        connect.setOnClickListener(v -> {
            if (alerrtDialogItemClickInterface != null) {
                alerrtDialogItemClickInterface.onClick();
            }
        });

        cancel.setOnClickListener(v -> cancel());
    }
}
