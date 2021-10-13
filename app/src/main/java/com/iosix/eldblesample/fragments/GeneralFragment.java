package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.GeneralFragmentPagerAdapter;
import com.iosix.eldblesample.models.ExampleSMSModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class GeneralFragment extends Fragment {
    private ViewPager pager;
    private GeneralFragmentPagerAdapter adapter;
    private TextView driverFullName;
    private EditText driverName,driverSurname;
    private String driverNameString,driverSurnameString;
    private LinearLayout driverInfo;

    public static GeneralFragment newInstance() {
        GeneralFragment fragment = new GeneralFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_general, container, false);
//        pager = view.findViewById(R.id.idGeneralFragmentPage);
//        adapter = new GeneralFragmentPagerAdapter(getContext());
//        pager.setAdapter(adapter);
//        pager.setCurrentItem(adapter.getCount(), true);
//
//        onPager();
        driverFullName = view.findViewById(R.id.idDriverNameText);
        driverName = view.findViewById(R.id.idDriverNameEdit);
        driverSurname = view.findViewById(R.id.idDriverFamilyEdit);
        driverInfo = view.findViewById(R.id.idDriverInfo);

        return view;
    }

    private void getGeneralInfo(){

        driverNameString = "Murad";
        driverNameString = "Babatov";

        driverInfo.setOnClickListener(v -> {

            if (v.findViewById(R.id.idDriverInfoEdit).getVisibility() == View.GONE){
                v.findViewById(R.id.idDriverInfoEdit).setVisibility(View.VISIBLE);
            }else {
                if (!driverName.getText().toString().equals("")){
                    driverNameString = driverName.getText().toString();
                }else {
//                    driverNameString = driverName.
                }

                if (!driverSurname.getText().toString().equals("")){
                    driverSurnameString = driverSurname.getText().toString();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSMSHandler(ExampleSMSModel sendModels){

    }
}