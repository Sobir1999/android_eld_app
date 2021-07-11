package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iosix.eldblesample.R;

public class BeginInspectionFragment extends Fragment {

    public BeginInspectionFragment() {
        // Required empty public constructor
    }

    public static BeginInspectionFragment newInstance() {
        BeginInspectionFragment fragment = new BeginInspectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_begin_inspection, container, false);
    }
}