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

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.GeneralAdapter;
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.adapters.VehiclesListAdapter;
import com.iosix.eldblesample.base.BaseActivity;
import com.iosix.eldblesample.enums.GPSTracker;
import com.iosix.eldblesample.enums.GeneralConstants;
import com.iosix.eldblesample.fragments.TimePickerFragment;
import com.iosix.eldblesample.models.TrailNubmer;
import com.iosix.eldblesample.retrofit.APIInterface;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddDvirActivity extends BaseActivity  implements TimePickerDialog.OnTimeSetListener {

    private TextView idSelectedUnitText, nextText;
    private TextView units;
    private TextView addTrailer;
    private TextView trailers;
    private TextView unitDefects, trailerDefects, unitDefectsTitle, trailerDefectsTitle;
    private TextView addTime;
    private TextView notes;
    private TextView idDefectLocationText;
    private ImageView backView,idVehicleImage,idSelectedUnitDelete;
    private EditText locationEditText;
    private ConstraintLayout idSelectedUnitLayout;
    String time,location,mNotes;
    private LinearLayout defects, addDefect;
    private DayDaoViewModel daoViewModel;
    private final String selectedUnit = "";
    private ArrayList<TrailersEntity> selectedTrailers;
    private TrailerRecyclerAdapter adapter;
    private ArrayList<String> unitDefectsString = new ArrayList<>();
    private ArrayList<String> trailerDefectsString = new ArrayList<>();
    private final ArrayList<String> trailerListSelected = new ArrayList<>();
    private String vehicleListSelected = "";
    private ArrayList<String> arrayList;
    private String day;
    private DvirViewModel dvirViewModel;
    private EldJsonViewModel eldJsonViewModel;
    RecyclerView selectedTrailersRecyclerView;
    private UserViewModel userViewModel;
    private String timeString = "";

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

        units = findViewById(R.id.idAddDvirUnitNumberText);
        idSelectedUnitText = findViewById(R.id.idSelectedUnitText);
        addTrailer = findViewById(R.id.idAddDvirTrailerText);
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
        idSelectedUnitText = findViewById(R.id.idSelectedUnitText);
        idVehicleImage = findViewById(R.id.idVehicleImage);
        idSelectedUnitDelete = findViewById(R.id.idSelectedUnitDelete);
        idSelectedUnitLayout = findViewById(R.id.idSelectedUnitLayout);


        day = getIntent().getStringExtra("currDay");
        dvirViewModel = ViewModelProviders.of(this).get(DvirViewModel.class);
        daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        eldJsonViewModel = ViewModelProviders.of(this).get(EldJsonViewModel.class);

        buttonClicks(getWindow().getDecorView().findViewById(R.id.fragment_container_add_dvir));
        getCurrentLocation();

        dvirViewModel.getTrailersMutableLiveData().observe(this,size ->{
            if (size > 0){
                selectedTrailersRecyclerView.setVisibility(View.VISIBLE);
                GeneralAdapter adapter = new GeneralAdapter(this,trailerListSelected,GeneralConstants.TRAILERS);
                selectedTrailersRecyclerView.setAdapter(adapter);
                adapter.setDeleteListener(s1 -> {
                    trailerListSelected.remove(s1);
                    dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                });
            }else {
                selectedTrailersRecyclerView.setVisibility(View.GONE);
            }
        });

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
            dialog.setContentView(R.layout.custom_vehicles_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RecyclerView vehicleListRv = dialog.findViewById(R.id.idVehicleRecyclerview);
            TextView cancel = dialog.findViewById(R.id.idVehicleCancel);
            userViewModel.getGetAllVehicles().observe(this,vehicleLists -> {
                VehiclesListAdapter vehiclesListAdapter = new VehiclesListAdapter(this,vehicleLists);
                vehicleListRv.setAdapter(vehiclesListAdapter);
                vehiclesListAdapter.setListener(s -> {
                    if (!vehicleListSelected.equals(s)){
                        vehicleListSelected = s;
                        dvirViewModel.getVehicleMutableLiveData().postValue(vehicleListSelected);
                    }
                    dialog.dismiss();
                });
                cancel.setOnClickListener(view1->{
                    dialog.dismiss();
                });
            });
            dialog.show();
        });

        dvirViewModel.getVehiclesMutableLiveData().observe(this,size ->{
            if (!size.equals("")){
                idSelectedUnitLayout.setVisibility(View.VISIBLE);
                idSelectedUnitText.setText(vehicleListSelected);
                idSelectedUnitDelete.setOnClickListener(view -> {
                    vehicleListSelected = "";
                    dvirViewModel.getVehicleMutableLiveData().postValue("");
                });
            }else {
                idSelectedUnitLayout.setVisibility(View.GONE);
            }
        });

        addTrailer.setOnClickListener(v -> createDialog(context, "Add Trailer", "Add Trailer", 2));

        addTime.setOnClickListener(v -> {
            DialogFragment timeFragment = new TimePickerFragment();
            timeFragment.show(getSupportFragmentManager(), "time picker");
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
                intent.putExtra("time",timeString);
                intent.putExtra("location",location);
                intent.putExtra("notes",mNotes);
                intent.putExtra("unit",vehicleListSelected);
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
            if (vehicleListSelected.equals("") && trailerListSelected.isEmpty()){
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setTitle("Unit not created")
                        .setMessage("Select unit or create manually!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else{
                Intent intent = new Intent(this, AddDefectActivity.class);
                intent.putExtra("isUnitSelected", !vehicleListSelected.equals(""));
                intent.putExtra("isTruckSelected", !trailerListSelected.isEmpty());
                intent.putExtra("unitDefects",unitDefectsString);
                intent.putExtra("trailerDefects",trailerDefectsString);
                startActivityForResult(intent, 100);
            }
        });

        defects.setOnClickListener(v1 -> {
            Intent intent = new Intent(this, AddDefectActivity.class);
            intent.putExtra("isUnitSelected", !vehicleListSelected.equals(""));
            intent.putExtra("isTruckSelected", !trailerListSelected.isEmpty());
            intent.putExtra("unitDefects",unitDefectsString);
            intent.putExtra("trailerDefects",trailerDefectsString);
            startActivityForResult(intent, 100);
        });
    }

    private void createDialog(ViewGroup group, String title, String hint, int type) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        TextView header = dialog.findViewById(R.id.idHeaderTv);
        EditText editText =  dialog.findViewById(R.id.idDialogEdit);
        TextView save = dialog.findViewById(R.id.idDialogSave);
        TextView cancel = dialog.findViewById(R.id.idDialogCancel);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        save.setOnClickListener(view -> {
            if (!editText.getText().toString().equalsIgnoreCase("")){
                if (trailerListSelected.size() > 0){
                    for (String t:trailerListSelected) {
                        if (!editText.getText().toString().equals(t)){
                            dialog.dismiss();
                            trailerListSelected.add(editText.getText().toString());
                            dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                            eldJsonViewModel.sendTrailer(new TrailNubmer(editText.getText().toString()));
                        }else {
                            Toast.makeText(this, "Already Added!!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    dialog.dismiss();
                    trailerListSelected.add(editText.getText().toString());
                    dvirViewModel.getTrailersMutableLiveData().postValue(trailerListSelected.size());
                    eldJsonViewModel.sendTrailer(new TrailNubmer(editText.getText().toString()));
                }
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        Time time = new Time(calendar.getTimeInMillis());
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSSSSS", Locale.US);
        timeString = format.format(time);
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