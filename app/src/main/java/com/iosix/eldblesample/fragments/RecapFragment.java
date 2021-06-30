package com.iosix.eldblesample.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.LinearLayoutTouchListener;

public class RecapFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RecapFragment() {
        // Required empty public constructor
    }

    public static RecapFragment newInstance() {
        RecapFragment fragment = new RecapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recap_fragment,container,false);
        LinearLayout linearLayout = view.findViewById(R.id.idRecapLinear);

        linearLayout.setOnTouchListener(new LinearLayoutTouchListener(this.getContext()) {
            @SuppressWarnings("deprecation")
            @Override
            public void onLeftToRightSwipe() {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onTouch() {
                assert getFragmentManager() != null;
                getFragmentManager().popBackStack();
            }
        });

//        linearLayout.swip
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
