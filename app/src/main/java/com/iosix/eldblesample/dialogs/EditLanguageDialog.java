package com.iosix.eldblesample.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.EditLanguageDialogListener;
import com.iosix.eldblesample.models.MessageModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class EditLanguageDialog extends Dialog {
    private LinearLayout eng, es, fr;
    private RadioButton radioEng, radioSp, radioFr;
    private EditLanguageDialogListener listener;

    public EditLanguageDialog(@NonNull Context context) {
        super(context);
    }

    public void setListener(EditLanguageDialogListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_language_item);

        SharedPreferences pref = getContext()
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);

//        progressBar = findViewById(R.id.idProgressBar1);
        eng = findViewById(R.id.idEnglish);
        es = findViewById(R.id.idSpanish);
        fr = findViewById(R.id.idFrench);
        radioEng = findViewById(R.id.idLangEngIcon);
        radioSp = findViewById(R.id.idLangEsIcon);
        radioFr = findViewById(R.id.idLangFrIcon);

        Log.d("Lang", "onCreate: " + pref.getString("lan", "en"));

        selectedRadioButton(pref.getString("lan", "en"));

        eng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(R.id.idEng);
                }
            }
        });

        es.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(R.id.idEs);
                }
            }
        });

        fr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(R.id.idFr);
                }
            }
        });
    }

    private void selectedRadioButton(String lan) {
        if (lan.equals("fr")) {
            radioFr.setChecked(true);
        }
        if (lan.equals("es")) {
            radioSp.setChecked(true);
        } else {
            radioEng.setChecked(true);
        }
    }
}
