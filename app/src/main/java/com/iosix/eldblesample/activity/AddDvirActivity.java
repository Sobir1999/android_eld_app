package com.iosix.eldblesample.activity;

import static com.iosix.eldblesample.utils.Utils.defects;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.TrailerListAdapter;
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.adapters.UnitListAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.GPSTracker;
import com.iosix.eldblesample.fragments.TimePickerFragment;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.retrofit.ApiClient;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDvirActivity extends BaseActivity  implements TimePickerDialog.OnTimeSetListener {

    private TextView idSelectedUnitText, nextText;
    private TextView units;
    private TextView addTrailer;
    private TextView trailers;
    private TextView unitDefects, trailerDefects, unitDefectsTitle, trailerDefectsTitle;
    private TextView addTime;
    private TextView notes;
    private TextView idDefectLocationText;
    private ImageView backView,idSelectedUnitDelete;
    private EditText locationEditText;
    String time,location,mNotes;
    private LinearLayout defects, addDefect;
    private ConstraintLayout idSelectedUnitLayout;
    private DayDaoViewModel daoViewModel;
    private final String selectedUnit = "No Unit Selected";
    private ArrayList<TrailersEntity> selectedTrailers;
    private TrailerRecyclerAdapter adapter;
    private ArrayList<String> unitDefectsString = new ArrayList<>();
    private ArrayList<String> trailerDefectsString = new ArrayList<>();
    private ArrayList<String> arrayList;
    private String day;
    private APIInterface apiInterface;
    private DvirViewModel dvirViewModel;
    RecyclerView selectedTrailersRecyclerView;
    private UserViewModel userViewModel;

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

//        addUnit = findViewById(R.id.idAddDvirUnitText);
        units = findViewById(R.id.idAddDvirUnitNumberText);
        idSelectedUnitText = findViewById(R.id.idSelectedUnitText);
        idSelectedUnitDelete = findViewById(R.id.idSelectedUnitDelete);
        idSelectedUnitLayout = findViewById(R.id.idSelectedUnitLayout);
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
        idDefectLocationText = findViewById(R.id.idDefectLocationText);
        selectedTrailersRecyclerView = findViewById(R.id.idTrailersRecyclerView);


        day = getIntent().getStringExtra("currDay");

        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);

        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        buttonClicks(getWindow().getDecorView().findViewById(R.id.fragment_container_add_dvir));
        getCurrentLocation();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {

                addDefect.setVisibility(View.GONE);
                defects.setVisibility(View.VISIBLE);

                unitDefectsString = data.getStringArrayListExtra("unitDefects");
                trailerDefectsString = data.getStringArrayListExtra("trailerDefects");

                unitDefects.setText(defects(unitDefectsString));
                trailerDefects.setText(defects(trailerDefectsString));

                if (unitDefectsString.size() == 0) {
                    unitDefectsTitle.setVisibility(View.GONE);
                    unitDefects.setVisibility(View.GONE);
                }

                if (trailerDefectsString.size() == 0) {
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

//        addUnit.setOnClickListener(v -> createDialog(context,"Add Unit","Create unit",1));
        units.setOnClickListener(v -> {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_list_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            TextView unitText = dialog.findViewById(R.id.idHeaderTv);
            RecyclerView recyclerView = dialog.findViewById(R.id.idRecyclerViewUnit);
            TextView cancel = dialog.findViewById(R.id.idDialogCancel);
            TextView idGeneralTrailerText = dialog.findViewById(R.id.idGeneralTrailerText);
            idGeneralTrailerText.setVisibility(View.GONE);

            userViewModel.getGetAllVehicles().observe(this,vehiclesEntities -> {
                UnitListAdapter unitListAdapter = new UnitListAdapter(this,vehiclesEntities);
                recyclerView.setAdapter(unitListAdapter);
                unitListAdapter.setListener(s -> {
                    idSelectedUnitLayout.setVisibility(View.VISIBLE);
                    idSelectedUnitText.setText(s);
                    dialog.dismiss();
                });
            });

            unitText.setText("Add Vehicle");
            cancel.setOnClickListener(view -> {
                dialog.dismiss();
            });

            dialog.show();

        });

        idSelectedUnitDelete.setOnClickListener(v->{
            idSelectedUnitLayout.setVisibility(View.GONE);
            idSelectedUnitText.setText("");
        });

        addTrailer.setOnClickListener(v -> createDialog(context, "Add Trailer", "Add Trailer", 2));

        addTime.setOnClickListener(v -> {
            DialogFragment timeFragment = new TimePickerFragment();
            timeFragment.show(getSupportFragmentManager(), "time picker");
        });

        trailers.setOnClickListener(v -> {

            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_list_dialog);

            TextView unitText = dialog.findViewById(R.id.idHeaderTv);
            RecyclerView recyclerView = dialog.findViewById(R.id.idRecyclerViewUnit);
            TextView cancel = dialog.findViewById(R.id.idDialogCancel);
            TextView idGeneralTrailerText = dialog.findViewById(R.id.idGeneralTrailerText);
            idGeneralTrailerText.setVisibility(View.GONE);

//            userViewModel.getGetAllTrailers().observe(this,trailersEntities -> {
//                TrailerListAdapter trailerListAdapter = new TrailerListAdapter(this,trailersEntities);
//                recyclerView.setAdapter(trailerListAdapter);
//                trailerListAdapter.setUpdateListener(position ->{
//                    dialog.dismiss();
//                    int n = 0;
//                    for (int i = 0; i < selectedTrailers.size(); i++) {
//                        if (trailersEntities.get(position).equals(selectedTrailers.get(i))){
//                            n++;
//                        }
//                    }
//                    if (n == 0){
//                        selectedTrailers.add(trailersEntities.get(position));
//                    }
//                });
//            });

            unitText.setText("Add Trailer");
            cancel.setOnClickListener(view -> {
                dialog.dismiss();
            });

            dialog.show();
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
            }else if (getCurrentLocation().equals("")){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Location missed")
                        .setMessage("Location not created!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else{
                time = addTime.getText().toString() + " CDT";
                location = getCurrentLocation();
                mNotes = notes.getText().toString();

                arrayList = new ArrayList<>();
                if (!idSelectedUnitText.getText().toString().equals("")){
                    arrayList.add(idSelectedUnitText.getText().toString());
                }else arrayList.add(null);
                arrayList.add(time);
                arrayList.add(location);
                arrayList.add(mNotes);
                arrayList.add(day);

                ArrayList<String> selectedTrailersNumber = new ArrayList<>();
                for (int i = 0; i < selectedTrailers.size(); i++) {
                    selectedTrailersNumber.add(selectedTrailers.get(i).getNumber());
                }

                Intent intent = new Intent(this, SignatureActivity.class);
                intent.putExtra("isTruckSelected", unitDefectsString.size() != 0 || trailerDefectsString.size() != 0);
                intent.putExtra("arrayList",arrayList);
                intent.putExtra("unitDefects",unitDefectsString);
                intent.putExtra("trailerDefects",trailerDefectsString);
                intent.putExtra("selectedTrailers",selectedTrailersNumber);
                intent.putExtra("day",day);
                intent.putExtra("finisher",new ResultReceiver(null){
                    @Override
                    protected void onReceiveResult(int resultCode, Bundle resultData) {
                        super.onReceiveResult(resultCode, resultData);
                        AddDvirActivity.this.finish();
                    }
                });
                startActivityForResult(intent,2);
            }

        });

        addDefect.setOnClickListener(v1 -> {
            if (idSelectedUnitText.getText().equals("") && selectedTrailers.isEmpty()){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Unit not created")
                        .setMessage("Select unit or create manually!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else{
                Intent intent = new Intent(this, AddDefectActivity.class);
                intent.putExtra("isUnitSelected", !idSelectedUnitText.getText().equals(""));
                intent.putExtra("isTruckSelected", !selectedTrailers.isEmpty());
                startActivityForResult(intent, 100);
            }
        });

        defects.setOnClickListener(v1 -> {
            if (idSelectedUnitText.getText().equals("") && selectedTrailers.isEmpty()){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Vehicle not created")
                        .setMessage("Select vehicle!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else{
                Intent intent = new Intent(this, AddDefectActivity.class);
                intent.putExtra("isUnitSelected", !idSelectedUnitText.getText().equals(""));
                intent.putExtra("isTruckSelected", !selectedTrailers.isEmpty());
                startActivityForResult(intent, 100);
            }

        });
    }

    private void createDialog(ViewGroup group, String title, String hint, int type) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        TextView header = dialog.findViewById(R.id.idHeaderTv);
        EditText editText =  dialog.findViewById(R.id.idDialogEdit);
        TextView save = dialog.findViewById(R.id.idDialogSave);
        TextView cancel = dialog.findViewById(R.id.idDialogCancel);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        save.setOnClickListener(view -> {
            if (!editText.getText().toString().equalsIgnoreCase("")){
                dialog.dismiss();
                apiInterface.sendTrailer(new TrailNubmer(editText.getText().toString())).enqueue(new Callback<TrailersEntity>() {
                    @Override
                    public void onResponse(Call<TrailersEntity> call, Response<TrailersEntity> response) {
                        if (response.isSuccessful()){
                            try {
                                userViewModel.insertTrailer(new TrailersEntity(response.body().getTrailer_id(),response.body().getNumber()));
                            } catch (ExecutionException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailersEntity> call, Throwable t) {
                    }
                });
            }else {
                Toast.makeText(this, "Insert Trailer Number!!!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();

    }

    private String getCurrentLocation(){
        if (true){
            locationEditText.setVisibility(View.GONE);
            idDefectLocationText.setVisibility(View.VISIBLE);
            idDefectLocationText.setOnClickListener(view -> {
                GPSTracker gpsTracker = new GPSTracker(this);
                double longtitude = gpsTracker.getLongitude();
                double latitude = gpsTracker.getLatitude();
                Geocoder geocoder = new Geocoder(AddDvirActivity.this, Locale.getDefault());
                try {
                    if (latitude != 0 && longtitude != 0){
                        List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                        Address obj = addresses.get(0);
                        String add = obj.getAddressLine(0);

                        idDefectLocationText.setText(add);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddDvirActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            return idDefectLocationText.getText().toString();
        }else {
            locationEditText.setVisibility(View.VISIBLE);
            idDefectLocationText.setVisibility(View.GONE);
            return locationEditText.getText().toString();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        addTime = findViewById(R.id.idDefectTimeText);
        if (hourOfDay > 12){
            addTime.setText(String.format("%s:%s %s", timeString(hourOfDay-12), timeString(minute),"PM"));
        }else {
            addTime.setText(String.format("%s:%s %s", timeString(hourOfDay), timeString(minute),"AM"));
        }
    }

    private String timeString(int digit) {
        String s = "" + digit;
        if (s.length() == 1) s = "0" + s;
        return s;
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.getViewModelStore().clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getViewModelStore().clear();
    }
}