package com.iosix.eldblesample.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AddDefectActivity extends AppCompatActivity {
    private final String[] maintitle = {
            "Buzuq",
            "Siniq",
            "Ishlamaydi",
            "Dabdala",
    };
    private List<String> selectedList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_defect);

        ImageView backButton = findViewById(R.id.idImageBack);
        TextView save = findViewById(R.id.save);
        backButton.setOnClickListener(v -> onBackPressed());
        save.setOnClickListener(v-> createDialog("Add a note to your defect", "Notes", 1));

        initView();
    }

    private void initView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, maintitle);

        ListView list = findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        list.setOnItemClickListener((parent, view, position, id) -> selectedItems(maintitle[position]));

    }

    private void selectedItems(String s) {
        if (selectedList.contains(s)) {
            selectedList.remove(s);
        } else {
            selectedList.add(s);
        }
    }

    private void back(String note) {
        Intent intent = getIntent();
        Bundle sendList = new Bundle();
        sendList.putStringArrayList("list", (ArrayList<String>) selectedList);
        if (selectedList.isEmpty()) {
            setResult(RESULT_CANCELED, intent);
        } else {
            intent.putExtra("defectsList", defects());
            intent.putExtra("notes", note);
            intent.putStringArrayListExtra("list", (ArrayList<String>) selectedList);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

        String defects(){
        StringBuilder defects = new StringBuilder();
        for(int i = 0; i < selectedList.size(); i++){
            defects.append(selectedList.get(i)).append("\n");
        }
        return defects.toString();
    }

    private void createDialog(String title, String hint, int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);

        View v = getLayoutInflater().inflate(R.layout.add_unit_trailer_dialog_view, null, false);
        EditText editText = v.findViewById(R.id.idAddUnitTrailerEdit);
        editText.setHint(hint);

        dialog.setPositiveButton("Ok", (dialog12, which) -> {
            if (type == 1 && !editText.getText().toString().equalsIgnoreCase("")) {
                back(editText.getText().toString());
            }
        });

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());

        dialog.setView(v);
        dialog.show();

//        editText.getText().toString().trim();
    }
}