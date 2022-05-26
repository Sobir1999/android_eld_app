package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DefectAdapter extends ArrayAdapter<String> {

    private final List<String> selectedList = new ArrayList<>();
    String[] defectsList;
    RadioButton radioButton;

    public DefectAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
