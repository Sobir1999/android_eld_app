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
    private TextView connect, cancel,idEldConnection,idIsConnected,idEngineState,disconnect;
    private AlertDialogItemClickInterface alerrtDialogItemClickInterface;
    private final int eldConnectionState;

    public ConnectToEldDialog(@NonNull Context context,int eldConnectionState) {
        super(context);
        this.eldConnectionState = eldConnectionState;
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
        idEngineState = findViewById(R.id.idEngineState);
        disconnect = findViewById(R.id.idEldDialDisconnect);



        if (eldConnectionState == 0){
            idIsConnected.setText("DISCONNECTED");
            disconnect.setClickable(false);
            connect.setClickable(true);
            disconnect.setTextColor(getContext().getResources().getColor(R.color.colorStatusON));
            connect.setTextColor(getContext().getResources().getColor(R.color.colorStatusONBold));
            connect.setOnClickListener(v -> {
                if (alerrtDialogItemClickInterface != null) {
                    alerrtDialogItemClickInterface.onClickConnect();
                }
            });
        }else if (eldConnectionState == 2){
            idIsConnected.setText("CONNECTED");
            disconnect.setClickable(true);
            connect.setClickable(false);
            connect.setTextColor(getContext().getResources().getColor(R.color.colorStatusON));
            disconnect.setTextColor(getContext().getResources().getColor(R.color.colorStatusONBold));
            disconnect.setOnClickListener(v -> {
                if (alerrtDialogItemClickInterface != null) {
                    alerrtDialogItemClickInterface.onClickDisCocnnect();
                }
            });
        }

        cancel.setOnClickListener(v -> {
            if (alerrtDialogItemClickInterface != null) {
                alerrtDialogItemClickInterface.onClickCancel();
            }
        });
    }
}
