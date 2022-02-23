package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.MyApplication.userData;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;

import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    private RadioButton eng, es, fr, ru, uz;
    private Context context;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    public void initView() {
        super.initView();

        loadViews();
        setLocalLang(userData.getLang());
    }

    private void loadViews() {
        eng = findViewById(R.id.idRadioEnglish);
        es = findViewById(R.id.idRadioSpain);
        fr = findViewById(R.id.idRadioFrench);
        uz = findViewById(R.id.idRadioUzbek);
        ru = findViewById(R.id.idRadioRussian);

        //    private LinearLayout eng_l, es_l, fr_l, ru_l, uz_l;
        ImageView imageBack = findViewById(R.id.idImageBack);
        TextView saveLang = findViewById(R.id.idSaveLanguage);

        imageBack.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });

        saveLang.setOnClickListener(v -> {
            RadioGroup radioGroup = findViewById(R.id.idLangRadioGroup);
            Log.d("LAN", "onClick: " + findViewById(radioGroup.getCheckedRadioButtonId()));
            setSelectLang(radioGroup.getCheckedRadioButtonId());
            Intent intent =  new Intent(context, MainActivity.class);
            startActivityForResult(intent,1);
        });
    }

    private void setSelectLang(int radioId) {
        if (radioId == R.id.idRadioEnglish) {
            setLocale("en");
        } else if (radioId == R.id.idRadioFrench) {
            setLocale("fr");
        } else if (radioId == R.id.idRadioSpain) {
            setLocale("es");
        } else if (radioId == R.id.idRadioRussian) {
            setLocale("ru");
        } else if (radioId == R.id.idRadioUzbek) {
            setLocale("uz");
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

    private void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Locale.setDefault(myLocale);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        userData.saveLang(lang);
    }

//        public void setLocale(String lang) {
//            Locale myLocale = new Locale(lang);
//            Resources res = getResources();
//            DisplayMetrics dm = res.getDisplayMetrics();
//            Configuration conf = res.getConfiguration();
//            conf.locale = myLocale;
//            res.updateConfiguration(conf, dm);
//
//            saveLanguage(lang);
//        }

//        public void saveLanguage(String language) {
//            SharedPreferences pref = requireContext()
//                    .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putString("lan", language);
//            editor.apply();
//        }
//
//        private String loadLocal() {
//            SharedPreferences pref = requireContext()
//                    .getSharedPreferences("MyPref", Context.MODE_PRIVATE);
//            return pref.getString("lan", "en");
//        }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}