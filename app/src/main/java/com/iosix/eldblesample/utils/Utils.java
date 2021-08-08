package com.iosix.eldblesample.utils;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;

import androidx.core.content.ContextCompat;

import com.iosix.eldblesample.MyApplication;
import com.iosix.eldblesample.R;


public class Utils {

    public static void setBluetoothDataEnabled(Context context) {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (!adapter.isEnabled()) {
            AlertDialogInit.Companion.showDialogOkWithMessage(context,
                    R.string.bluetooth_not_work, R.string.you_want_to_turn_on,
                    (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        dialog.dismiss();
                        adapter.enable();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                }
            });
        }
    }

    public static void statusCheck() {
        final LocationManager manager = (LocationManager) MyApplication.context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    private static void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MyApplication.context);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> MyApplication.context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)))
                .setNegativeButton("No", (dialog, id) -> dialog.cancel());
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public static int getColors(int id) {
        return ContextCompat.getColor(MyApplication.context, id);
    }
}
