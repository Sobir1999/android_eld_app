package com.iosix.eldblesample.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.shared_prefs.UserData;

import java.util.Locale;

public class LanguageActivity extends BaseActivity {

    private RadioButton eng, es, fr, ru, uz;
    private UserData userData;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_language;
    }

    @Override
    public void initView() {
        super.initView();
        userData = new UserData(this);
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
        ImageView imageView = findViewById(R.id.idImageBack);
        TextView saveLang = findViewById(R.id.idSaveLanguage);

        imageView.setOnClickListener(v -> onBackPressed());

        saveLang.setOnClickListener(v -> {
            RadioGroup radioGroup = findViewById(R.id.idLangRadioGroup);
            setSelectLang(radioGroup.getCheckedRadioButtonId());
            Intent intent =  new Intent(LanguageActivity.this, MainActivity.class);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}