package com.iosix.eldblesample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

public class AddDefectFragment extends Fragment {
    private ImageView backView;
    private DayDaoViewModel daoViewModel;

    public static AddDefectFragment newInstance(DayDaoViewModel dayDaoViewModel) {
        AddDefectFragment fragment = new AddDefectFragment();
        Bundle args = new Bundle();
        fragment.daoViewModel = dayDaoViewModel;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_dvir, container, false);

        backView = v.findViewById(R.id.idImageBack);

        daoViewModel.getGetAllVehicles().observe(requireActivity(), dayEntities -> {
            for (int i=0; i<dayEntities.size(); i++) {
                Log.d("ADDDefect", "onChanged: " + dayEntities.get(i).getName());
            }
        });

        initButtons();

        return v;
    }

    @SuppressWarnings("deprecation")
    private void initButtons() {
        backView.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });
    }

}