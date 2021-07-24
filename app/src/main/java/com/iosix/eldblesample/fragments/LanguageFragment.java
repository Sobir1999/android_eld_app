    package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.R;

import java.util.Locale;

import static com.iosix.eldblesample.MyApplication.userData;

    public class LanguageFragment extends Fragment {
    private RadioButton eng, es, fr, ru, uz;
        private Context context;

    public LanguageFragment() {
        // Required empty public constructor
    }

    public static LanguageFragment newInstance() {
        return new LanguageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        assert container != null;
        context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_language, container, false);
        loadViews(view);
        setLocalLang(userData.getLang());
        Log.d("LAN", "onCreateView: " + userData.getLang());
//        eng.setChecked(true);
        return view;
    }

    private void loadViews(View view) {
        eng = view.findViewById(R.id.idRadioEnglish);
        es = view.findViewById(R.id.idRadioSpain);
        fr = view.findViewById(R.id.idRadioFrench);
        uz = view.findViewById(R.id.idRadioUzbek);
        ru = view.findViewById(R.id.idRadioRussian);

        //    private LinearLayout eng_l, es_l, fr_l, ru_l, uz_l;
        ImageView imageBack = view.findViewById(R.id.idImageBack);
        TextView saveLang = view.findViewById(R.id.idSaveLanguage);

        imageBack.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });

        saveLang.setOnClickListener(v -> {
            RadioGroup radioGroup = view.findViewById(R.id.idLangRadioGroup);
            Log.d("LAN", "onClick: " + view.findViewById(radioGroup.getCheckedRadioButtonId()));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}