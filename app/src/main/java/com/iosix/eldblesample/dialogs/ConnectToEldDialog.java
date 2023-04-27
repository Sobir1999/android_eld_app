package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.iosix.eldblelib.EldBleDataCallback;
import com.iosix.eldblelib.EldBleError;
import com.iosix.eldblelib.EldBleScanCallback;
import com.iosix.eldblelib.EldBroadcast;
import com.iosix.eldblelib.EldBroadcastTypes;
import com.iosix.eldblelib.EldDataRecord;
import com.iosix.eldblelib.EldEngineStates;
import com.iosix.eldblelib.EldManager;
import com.iosix.eldblelib.EldScanObject;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;
import com.iosix.eldblesample.shared_prefs.LastStatusData;

import java.util.ArrayList;
import java.util.Objects;

public class ConnectToEldDialog extends Dialog {
    private LinearLayout vehicleInfo, sendLog;
    private ConstraintLayout undintifiedDriving, faultCode;
    private TextView connect, cancel,idEldConnection,idIsConnected,idEngineState,disconnect;
    private AlertDialogItemClickInterface alerrtDialogItemClickInterface;
    private TextView gpsConnect;
    boolean isConnectedToGps;
    private LastStatusData lastStatusData;

    public ConnectToEldDialog(@NonNull Context context, boolean isConnectedToGps,LastStatusData lastStatusData) {
        super(context);
        this.isConnectedToGps = isConnectedToGps;
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
        idEngineState = findViewById(R.id.idEngineState);
        disconnect = findViewById(R.id.idEldDialDisconnect);
        gpsConnect = findViewById(R.id.idGpsConnect);

        if (isConnectedToGps) {
            gpsConnect.setText(R.string.on);
        } else {
            gpsConnect.setText(R.string.off);
        }

        if (Objects.equals(lastStatusData.getLastConnState(), String.valueOf(R.string.connected))) {
            idIsConnected.setText(R.string.connected);
        } else {
            idIsConnected.setText(R.string.not_connected);
        }

        if (lastStatusData.getLastEngineState().equals(String.valueOf(R.string.on))){
            idEngineState.setText(R.string.on);
        }else {
            idEngineState.setText(R.string.off);
        }

        new EldBleDataCallback(){
            @Override
            public void OnDataRecord(EldBroadcast dataRec, EldBroadcastTypes RecordType) {
                if (RecordType == EldBroadcastTypes.ELD_DATA_RECORD){
                    if (((EldDataRecord)dataRec).getEngineState() == EldEngineStates.ENGINE_ON){
                        idEngineState.setText("ON");
                    }else {
                        idEngineState.setText("OFF");
                    }
                }
            }
        };

        connect.setOnClickListener(v -> {
            if (alerrtDialogItemClickInterface != null) {
                alerrtDialogItemClickInterface.onClickConnect();
            }
        });

        disconnect.setOnClickListener(v -> {
            if (alerrtDialogItemClickInterface != null) {
                alerrtDialogItemClickInterface.onClickDisCocnnect();
            }
        });


        cancel.setOnClickListener(v -> {
            if (alerrtDialogItemClickInterface != null) {
                alerrtDialogItemClickInterface.onClickCancel();
            }
        });
    }
}
