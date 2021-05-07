package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.iosix.eldblesample.R;

public class LGDDFragment extends Fragment {

    public static android.app.Fragment getInstance() {
        return new android.app.Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_l_g_d_d, container, false);
    }
}