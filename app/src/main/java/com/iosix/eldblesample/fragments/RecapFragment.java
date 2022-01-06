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
import com.iosix.eldblesample.adapters.RecapListAdapter;
import com.iosix.eldblesample.interfaces.LinearLayoutTouchListener;

public class RecapFragment extends Fragment {

    RecyclerView recyclerView;
    RecapListAdapter adapter;

    public static RecapFragment newInstance() {
        RecapFragment fragment = new RecapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recap_fragment,container,false);
        LinearLayout linearLayout = view.findViewById(R.id.idRecapLinear);
        recyclerView = view.findViewById(R.id.idRecapRecyclerView);
        adapter = new RecapListAdapter(getContext());
        recyclerView.setAdapter(adapter);

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
