package com.iosix.eldblesample.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.AlertDialogItemClickInterface;

public class CustomDialogClass extends Dialog {

    public Activity c;
    public Dialog d;
    public String title,hint;
    public TextView save,cancel,textView;
    public EditText editText;
    private AlertDialogItemClickInterface alerrtDialogItemClickInterface;

    public CustomDialogClass(Activity a,String title,String hint) {
        super(a);
        this.c = a;
        this.title = title;
        this.hint = hint;
    }

    public void setAlerrtDialogItemClickInterface(AlertDialogItemClickInterface alerrtDialogItemClickInterface) {
        this.alerrtDialogItemClickInterface = alerrtDialogItemClickInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        save = findViewById(R.id.idDialogSave);
        cancel = findViewById(R.id.idDialogCancel);
        editText = findViewById(R.id.idDialogEdit);
        textView = findViewById(R.id.idHeaderTv);
        save.setOnClickListener(view -> {
            if (alerrtDialogItemClickInterface != null) {
                alerrtDialogItemClickInterface.onClick();
            }
        });

        cancel.setOnClickListener(view -> cancel());
        textView.setText(title);
        editText.setHint(hint);
    }

}
