package com.iosix.eldblesample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.shared_prefs.TinyDB;

import java.util.ArrayList;
import java.util.List;

import static com.iosix.eldblesample.MyApplication.context;
import static com.iosix.eldblesample.MyApplication.userData;
import static com.iosix.eldblesample.utils.Constants.trailerDefects;
import static com.iosix.eldblesample.utils.Constants.unitDefects;

public class AddDefectFragment extends Fragment {

    private final ArrayList<String> selectedList = new ArrayList<>();
    final int defectType;// 1- Unit Defects, 2- Trailer Defects
    String[] defectsList;
    TinyDB tinyDB;
    RadioButton radioButton;

    public AddDefectFragment(int defectType) {
        this.defectType = defectType;
        if(defectType == 1) defectsList = unitDefects;
        else defectsList = trailerDefects;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tinyDB = new TinyDB(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_defect, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    void initView(View view) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(view.getContext(),
                R.layout.simple_defect_item, R.id.idRadioDefect, defectsList);

        ListView list = view.findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        list.setOnItemClickListener((parent, view1, position, id) -> {
                    selectedItems(defectsList[position]);
                }
        );
    }

    private void selectedItems(String s) {
        if (selectedList.contains(s)) {
            selectedList.remove(s);
        } else {
            selectedList.add(s);
        }
        tinyDB.putListString(defectType,selectedList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
    }
}