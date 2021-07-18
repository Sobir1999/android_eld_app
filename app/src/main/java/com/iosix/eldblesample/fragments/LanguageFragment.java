    package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.R;

import java.util.Locale;

public class LanguageFragment extends Fragment {
    private RadioButton eng, es, fr, ru, uz;
//    private LinearLayout eng_l, es_l, fr_l, ru_l, uz_l;
    private SharedPreferences pref;
    private ImageView imageBack;
    private TextView saveLang;
    private Context context;

    public LanguageFragment() {
        // Required empty public constructor
    }

    public static LanguageFragment newInstance() {
        LanguageFragment fragment = new LanguageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        pref = container != null ? container.getContext()
                .getSharedPreferences("MyPref", Context.MODE_PRIVATE) : null;

        View view = inflater.inflate(R.layout.fragment_language, container, false);
        loadViews(view);
        setLocalLang(loadLocal(pref));
        Log.d("LAN", "onCreateView: " + loadLocal(pref));
//        eng.setChecked(true);
        return view;
    }

    private void loadViews(View view) {
        eng = view.findViewById(R.id.idRadioEnglish);
        es = view.findViewById(R.id.idRadioSpain);
        fr = view.findViewById(R.id.idRadioFrench);
        uz = view.findViewById(R.id.idRadioUzbek);
        ru = view.findViewById(R.id.idRadioRussian);

        imageBack = view.findViewById(R.id.idImageBack);
        saveLang = view.findViewById(R.id.idSaveLanguage);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        saveLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radioGroup = view.findViewById(R.id.idLangRadioGroup);
                Log.d("LAN", "onClick: " + view.findViewById(radioGroup.getCheckedRadioButtonId()));
                setSelectelang(radioGroup.getCheckedRadioButtonId());
                Intent intent =  new Intent(context, MainActivity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    private void setSelectelang(int radioId) {
        if (radioId == R.id.idRadioEnglish) {
            setLocale("en", pref);
        } else if (radioId == R.id.idRadioFrench) {
            setLocale("fr", pref);
        } else if (radioId == R.id.idRadioSpain) {
            setLocale("es", pref);
        } else if (radioId == R.id.idRadioRussian) {
            setLocale("ru", pref);
        } else if (radioId == R.id.idRadioUzbek) {
            setLocale("uz", pref);
        }
    }

    private void setLocalLang(String lang){
        if (lang.equalsIgnoreCase("en")) {
            eng.setChecked(true);
        } else if (lang.equalsIgnoreCase("es")) {
            es.setChecked(true);
        } else if (lang.equalsIgnoreCase("fr")) {
            fr.setChecked(true);
        }
        else if (lang.equalsIgnoreCase("ru")) {
            ru.setChecked(true);
        }
        else if (lang.equalsIgnoreCase("uz")) {
            uz.setChecked(true);
        }
    }

    private void setLocale(String lang, SharedPreferences pref) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        saveLanguage(lang, pref);
    }

    private void saveLanguage(String language, SharedPreferences pref) {
//        SharedPreferences pref = getApplicationContext()
//                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("lan", language);
        editor.apply();
    }

    private String loadLocal(SharedPreferences pref) {
//        SharedPreferences pref = getA()
//                .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        return pref.getString("lan", "en");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}