package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.iosix.eldblesample.R;

public class InspectionModuleFragment extends Fragment {

    Button beginInspection,sendLogs,sendELDFile;
    ImageView img;

    public InspectionModuleFragment() {
        // Required empty public constructor
    }

    public static InspectionModuleFragment newInstance() {
        InspectionModuleFragment fragment = new InspectionModuleFragment();
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
        View view = inflater.inflate(R.layout.fragment_inspection_module, container, false);
        loadViews(view);
        return view;
    }

    private void loadViews(View view) {

        img = view.findViewById(R.id.idImageBack);
        beginInspection = view.findViewById(R.id.idBeginInspection);
        sendLogs = view.findViewById(R.id.idSendLogs);
        sendELDFile = view.findViewById(R.id.idSendELDFile);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

    }
}