package com.iosix.eldblesample.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;

public class HomeTerminalDialog extends Dialog implements android.view.View.OnClickListener {

    public Context c;
    public Dialog d;
    public TextView save, cancel,selectState;
    public EditText idinformationEdit,idCityEdit,idZipEdit;
    public String information,city,zip,state;
    OnMyDialogResult mDialogResult;

    public HomeTerminalDialog(Context context) {
        super(context);
        c = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.home_terminal_diolog);
        save = findViewById(R.id.idSaveText);
        cancel = findViewById(R.id.idCancelText);
        selectState = findViewById(R.id.idSelectStateText);
        idinformationEdit = findViewById(R.id.idinformationEdit);
        idCityEdit = findViewById(R.id.idCityEdit);
        idZipEdit = findViewById(R.id.idZipEdit);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
        selectState.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.idSaveText:
                if (idinformationEdit.getText() != null){
                    information = idinformationEdit.getText().toString();
                }
                if (idCityEdit.getText() != null){
                    city = idCityEdit.getText().toString();
                }
                if (idZipEdit.getText() != null){
                    zip = idZipEdit.getText().toString();
                }if(selectState.getText() != null){
                state = selectState.getText().toString();
                }if(mDialogResult != null){
                    mDialogResult.finish(information,city,zip,state);
                }
                break;
            case R.id.idCancelText:
                dismiss();
                break;
//            case R.id.idSelectStateText:
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(c);
//                alertDialog.setTitle("Select State");
//
//                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
//
//                alertDialog.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
//
//                alertDialog.setAdapter(arrayAdapter, (dialog, which) -> {
//                    String strName = arrayAdapter.getItem(which);
//                    selectState.setText(strName);
//                });
//                alertDialog.show();
//                break;
//            default:
//                break;
        }
        dismiss();
    }

    public void setDialogResult(OnMyDialogResult dialogResult){
        mDialogResult = dialogResult;
    }

    public interface OnMyDialogResult{
        void finish(String information,String city,String zip,String state);
    }
}
