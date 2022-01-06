package com.iosix.eldblesample.activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.fragments.TimePickerFragment;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class AddDvirActivity extends BaseActivity  implements TimePickerDialog.OnTimeSetListener {

    private TextView addUnit, nextText;
    private TextView units;
    private TextView addTrailer;
    private TextView trailers;
    private TextView unitDefects, trailerDefects, unitDefectsTitle, trailerDefectsTitle;
    private TextView addTime;
    private TextView notes;
    private ImageView backView;
    private EditText locationEditText;
    String time,location,mNotes;
    private LinearLayout defects, addDefect;
    private DayDaoViewModel daoViewModel;
    private final String selectedUnit = "No Unit Selected";
    private ArrayList<TrailersEntity> selectedTrailers;
    private TrailerRecyclerAdapter adapter;
    private String unitDefectsString = "", trailerDefectsString = "";
    private ArrayList<String> arrayList;
    private String day;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_dvir2;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.clear();
    }

    @Override
    public void initView() {
        super.initView();

        addUnit = findViewById(R.id.idAddDvirUnitText);
        units = findViewById(R.id.idAddDvirUnitNumberText);
        addTrailer = findViewById(R.id.idAddDvirTrailerText);
        trailers = findViewById(R.id.idAddDvirTrailerNumberText);
        unitDefects = findViewById(R.id.unitDefects);
        trailerDefects = findViewById(R.id.trailerDefects);
        unitDefectsTitle = findViewById(R.id.unitDefectsTitle);
        trailerDefectsTitle = findViewById(R.id.trailerDefectsTitle);
        addTime = findViewById(R.id.idDefectTimeText);
        notes = findViewById(R.id.notes);
        addDefect = findViewById(R.id.addDefect);
        defects = findViewById(R.id.defects);
        backView = findViewById(R.id.idImageBack);
        nextText = findViewById(R.id.idAddDvirNext);
        locationEditText = findViewById(R.id.idDefectLocationEdit);

        day = getIntent().getStringExtra("currDay");

        RecyclerView recyclerView = findViewById(R.id.idTrailersRecyclerView);

        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        daoViewModel.getGetAllVehicles().observe(this, vehiclesEntities -> units.setText(vehiclesEntities.isEmpty() ? selectedUnit : vehiclesEntities.get(0).getName()));

        buttonClicks(getWindow().getDecorView().findViewById(R.id.fragment_container_add_dvir));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TrailerRecyclerAdapter(selectedTrailers);
        recyclerView.setAdapter(adapter);

        adapterListener();
    }

    private void adapterListener() {
        adapter.setUpdateListener((position) -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle("Select Trailers");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllTrailers().observe(this, trailersEntities -> {
                for (int i = 0; i < trailersEntities.size(); i++) {
                    arrayAdapter.add(trailersEntities.get(i).getNumber());
                }
            });

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                selectedTrailers.get(position).setNumber(strName);
                adapter.notifyDataSetChanged();
            });
            builderSingle.show();

            adapter.notifyDataSetChanged();
        });

        adapter.setDeleteListener((position) -> {
            selectedTrailers.remove(position);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {

                addDefect.setVisibility(View.GONE);
                defects.setVisibility(View.VISIBLE);

                unitDefectsString = data.getStringExtra("unitDefects");
                trailerDefectsString = data.getStringExtra("trailerDefects");

                unitDefects.setText(unitDefectsString);
                trailerDefects.setText(trailerDefectsString);

                if (unitDefectsString.equals("")) {
                    unitDefectsTitle.setVisibility(View.GONE);
                    unitDefects.setVisibility(View.GONE);
                }

                if (trailerDefectsString.equals("")) {
                    trailerDefectsTitle.setVisibility(View.GONE);
                    trailerDefects.setVisibility(View.GONE);
                }

                notes.setText(data.getStringExtra("notes"));

            } else {
                Toast.makeText(this, "Not selected item", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void buttonClicks(ViewGroup context) {
        selectedTrailers = new ArrayList<>();

        addUnit.setOnClickListener(v -> createDialog(context, "Add Unit", "Add Unit", 1));

        units.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle("Select Unit");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllVehicles().observe(this, vehiclesEntities -> {
                for (int i = 0; i < vehiclesEntities.size(); i++) {
                    arrayAdapter.add(vehiclesEntities.get(i).getName());
                }
            });

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                units.setText(strName);
            });
            builderSingle.show();
        });

        addTrailer.setOnClickListener(v -> createDialog(context, "Add Trailer", "Add Trailer", 2));

        addTime.setOnClickListener(v -> {
            DialogFragment timeFragment = new TimePickerFragment();
            timeFragment.show(getSupportFragmentManager(), "time picker");
        });

        trailers.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
            builderSingle.setTitle("Select Trailers");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllTrailers().observe(this, trailersEntities -> {
                for (int i = 0; i < trailersEntities.size(); i++) {
                    arrayAdapter.add(trailersEntities.get(i).getNumber());
                }
            });

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                selectedTrailers.add(new TrailersEntity(strName));
                adapter.notifyDataSetChanged();
            });
            builderSingle.show();
        });

        backView.setOnClickListener(v -> onBackPressed());

        nextText.setOnClickListener(v1 -> {

            if(addTime.getText().toString().equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Time missed")
                        .setMessage("Time not created!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else if (locationEditText.getText().toString().equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Location missed")
                        .setMessage("Location not created!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else{
                time = addTime.getText().toString() + " CDT";
                location = locationEditText.getText().toString();
                mNotes = notes.getText().toString();

                String unitDefectsString1;
                String trailerDefectsString1;

                if (unitDefectsString.equals("")){
                    unitDefectsString1 = "No unit selected";
                }else {
                    unitDefectsString1 = unitDefectsString;
                }

                if (trailerDefectsString.equals("")){
                    trailerDefectsString1 = "No trailer selected";
                }else {
                    trailerDefectsString1 = trailerDefectsString;
                }

                arrayList = new ArrayList<>();
                arrayList.add(units.getText().toString());
                arrayList.add(trailers.getText().toString());
                arrayList.add(unitDefectsString1);
                arrayList.add(trailerDefectsString1);
                arrayList.add(time);
                arrayList.add(location);
                arrayList.add(mNotes);
                arrayList.add(day);

                Log.d("day","day:" + arrayList.get(7));
                Log.d("day","day: " + day);

                Intent intent = new Intent(this, SignatureActivity.class);
                intent.putExtra("isTruckSelected", !unitDefectsString.equalsIgnoreCase("") || !trailerDefectsString.equalsIgnoreCase(""));
                intent.putExtra("arrayList",arrayList);
                intent.putExtra("day",day);
                startActivity(intent);
            }

        });

        addDefect.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, AddDefectActivity.class);
            intent.putExtra("isTruckSelected", !selectedTrailers.isEmpty());
            startActivityForResult(intent, 100);
        });

        defects.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, AddDefectActivity.class);
            intent.putExtra("isTruckSelected", !selectedTrailers.isEmpty());
            startActivityForResult(intent, 100);
        });
    }

    private void createDialog(ViewGroup group, String title, String hint, int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);

        View v = getLayoutInflater().inflate(R.layout.add_unit_trailer_dialog_view, group, false);
        EditText editText = v.findViewById(R.id.idAddUnitTrailerEdit);
        editText.setHint(hint);

        dialog.setPositiveButton("Ok", (dialog12, which) -> {
            if (type == 1 && !editText.getText().toString().equalsIgnoreCase("")) {
                daoViewModel.insertVehicle(new VehiclesEntity(editText.getText().toString()));
            } else if (type == 2 && !editText.getText().toString().equalsIgnoreCase("")) {
                try {
                    daoViewModel.insertTrailer(new TrailersEntity(editText.getText().toString()));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (type == 1 && editText.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Insert Unit Number!!!", Toast.LENGTH_SHORT).show();
            } else if (type == 2 && editText.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(this, "Insert Trailer Number!!!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());

        dialog.setView(v);
        dialog.show();

//        editText.getText().toString().trim();
    }

    private String getDialogString() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
        builderSingle.setTitle("Select Trailers");
        final String[] strName = new String[1];

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_selectable_list_item);
        daoViewModel.getGetAllTrailers().observe(this, trailersEntities -> {
            for (int i = 0; i < trailersEntities.size(); i++) {
                arrayAdapter.add(trailersEntities.get(i).getNumber());
            }
        });

        builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            strName[0] = arrayAdapter.getItem(which);
            adapter.notifyDataSetChanged();
        });
        builderSingle.show();

        return strName[0];
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        addTime = findViewById(R.id.idDefectTimeText);
        addTime.setText(String.format("%s:%s", timeString(hourOfDay), timeString(minute)));
    }

    private String timeString(int digit) {
        String s = "" + digit;
        if (s.length() == 1) s = "0" + s;
        return s;
    }
}