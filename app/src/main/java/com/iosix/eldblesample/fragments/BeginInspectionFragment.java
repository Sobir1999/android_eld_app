package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.enums.Day.dateToString;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.HOSConstants.SHIFTLIMIT;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.InspectionLogAdapter;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class BeginInspectionFragment extends Fragment {

    private GeneralViewModel generalViewModel;
    private DvirViewModel dvirViewModel;
    private DayDaoViewModel dayDaoViewModel;
    private DriverSharedPrefs driverSharedPrefs;
    private StatusDaoViewModel statusDaoViewModel;
    private int index = 1;
    private List<DayEntity> dayEntities = new ArrayList<>();
    private String currDay;
    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];
    private CustomStableRulerChart idCustomChart;
    private CustomLiveRulerChart idCustomChartLive;
    List<LogEntity> truckStatusEntities = new ArrayList<>();
    ArrayList<LogEntity> truckStatusEntitiesCurr = new ArrayList<>();
    ArrayList<LogEntity> otherStatusEntities = new ArrayList<>();

    ImageView idSignatureReport;
    TextView idDateReport,idDriverName,idCoDriver,idDriverID,idTimeZone,idTimeZoneOffset,idDistance,idCarrier,
            idTruckTracktorId,idTrailerID,idTruckTracktorVIN,idELDProvider,idMainOffice,idHomeTerminal,
            idShippingID,idFrom,idTo,idCurrentLocation,idNotes,idTotalHoursSinceRestart,idHrsAvailableToday,idHrsWorkedToday;
    RecyclerView inspectionRv;

    public BeginInspectionFragment() {
        // Required empty public constructor
    }

    public static BeginInspectionFragment newInstance() {
        BeginInspectionFragment fragment = new BeginInspectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        generalViewModel = ViewModelProviders.of(requireActivity()).get(GeneralViewModel.class);
        dayDaoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);
        driverSharedPrefs = DriverSharedPrefs.getInstance(requireContext());
        statusDaoViewModel = ViewModelProviders.of(requireActivity()).get(StatusDaoViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_begin_inspection, container, false);
        idDateReport = view.findViewById(R.id.idDateReport);
        idDriverName = view.findViewById(R.id.idDriverName);
        idCoDriver = view.findViewById(R.id.idCoDriver);
        idDriverID = view.findViewById(R.id.idDriverID);
        idTimeZone = view.findViewById(R.id.idTimeZone);
        idTimeZoneOffset = view.findViewById(R.id.idTimeZoneOffset);
        idDistance = view.findViewById(R.id.idDistance);
        idCarrier = view.findViewById(R.id.idCarrier);
        idTruckTracktorId = view.findViewById(R.id.idVehicleVin);
        idTrailerID = view.findViewById(R.id.idTrailers);
        idELDProvider = view.findViewById(R.id.idELDProvider);
        idMainOffice = view.findViewById(R.id.idMainOffice);
        idHomeTerminal = view.findViewById(R.id.idHomeTerminal);
        idShippingID = view.findViewById(R.id.idShippingID);
        idFrom = view.findViewById(R.id.idFrom);
        idTo = view.findViewById(R.id.idTo);
        idCurrentLocation = view.findViewById(R.id.idCurrentLocation);
        idNotes = view.findViewById(R.id.idNotes);
        idSignatureReport = view.findViewById(R.id.idSignatureReport);
        idCustomChart = view.findViewById(R.id.idCustomChart);
        idCustomChartLive = view.findViewById(R.id.idCustomChartLive);
        inspectionRv = view.findViewById(R.id.idInspectionRv);
        idTotalHoursSinceRestart = view.findViewById(R.id.idTotalHoursSinceRestart);
        idHrsAvailableToday = view.findViewById(R.id.idHrsAvailableToday);
        idHrsWorkedToday = view.findViewById(R.id.idHrsWorkedToday);

        setUpUI();
        setUpViewModels();

        clickPrevOrNext(view);
        return  view;
    }

    private void setUpUI(){
        idDriverName.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname()));
        idDriverID.setText(driverSharedPrefs.getDriverId());
        idCarrier.setText(driverSharedPrefs.getCompany());
        idMainOffice.setText(driverSharedPrefs.getMainOffice());
        idHomeTerminal.setText(driverSharedPrefs.getHomeTerAdd());
        idTimeZone.setText(TimeZone.getDefault().getID());
    }

    private void setUpViewModels(){

        dayDaoViewModel.getMgetAllDays().observe(getViewLifecycleOwner(), dayEntities -> {
            this.dayEntities = dayEntities;
            dvirViewModel.getCurrentName().postValue(today);
            for (int i = 0; i < dayEntities.size(); i++) {
                if (dayEntities.get(i).getDay().equals(today)){
                    index = i;
                }
            }
            idDateReport.setText(today + "," + Calendar.getInstance().get(Calendar.YEAR));
        });

        statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(),logEntities -> {
            for (int i = 0; i < logEntities.size(); i++) {
                if (logEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId())){
                    truckStatusEntities.add(logEntities.get(i));
                }
            }
        });

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(),day ->{

            idHrsWorkedToday.setText(intToTime(driverSharedPrefs.getWorkingHours(driverSharedPrefs.getDriverId()+ " " + day)));
            if (SHIFTLIMIT - driverSharedPrefs.getWorkingHours(driverSharedPrefs.getDriverId()+ " " + day) >= 0){
                idHrsAvailableToday.setText(intToTime(SHIFTLIMIT - driverSharedPrefs.getWorkingHours(driverSharedPrefs.getDriverId()+ " " + day)));
            }else {
                idHrsAvailableToday.setText(intToTime(0));
            }

            generalViewModel.getMgetGenerals().observe(getViewLifecycleOwner(),generalEntities -> {
                int j = 0;
                for (int i = generalEntities.size()-1; i >=0; i--) {
                    try {
                        if (dateToString(generalEntities.get(i).getDay()).equals(day)){
                            j++;
                            idTrailerID.setText(getString(generalEntities.get(i).getTrailers()));
                            idShippingID.setText(getString(generalEntities.get(i).getShippingDocs()));
                            idCoDriver.setText(getString(generalEntities.get(i).getCo_driver_name()));
                            idTruckTracktorId.setText(getString(generalEntities.get(i).getTrailers()));
                            idFrom.setText(generalEntities.get(i).getFrom_info());
                            idTo.setText(generalEntities.get(i).getTo_info());
                            idNotes.setText(generalEntities.get(i).getNote());
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (j == 0){
                    idTrailerID.setText("");
                    idShippingID.setText("");
                    idCoDriver.setText("");
                    idTruckTracktorId.setText("");
                    idFrom.setText("");
                    idTo.setText("");
                    idNotes.setText("");
                }
            });

            InspectionLogAdapter inspectionLogAdapter = new InspectionLogAdapter(requireContext(),truckStatusEntities,day);
            inspectionRv.setAdapter(inspectionLogAdapter);

            truckStatusEntitiesCurr.clear();
            otherStatusEntities.clear();
            for (int i = 0; i < truckStatusEntities.size(); i++) {
                if (truckStatusEntities.get(i).getTime().equals(day)){
                    if (truckStatusEntities.get(i).getTo_status() < 6){
                        truckStatusEntitiesCurr.add(truckStatusEntities.get(i));
                    }else {
                        otherStatusEntities.add(truckStatusEntities.get(i));
                    }
                }
            }

            if(day.equals(today)){
                idCustomChart.setVisibility(View.GONE);
                idCustomChartLive.setVisibility(View.VISIBLE);
                idCustomChartLive.setArrayList(truckStatusEntitiesCurr,otherStatusEntities);
            }else {
                idCustomChartLive.setVisibility(View.GONE);
                idCustomChart.setVisibility(View.VISIBLE);
                idCustomChart.setArrayList(truckStatusEntitiesCurr,otherStatusEntities);
            }
        });
    }

    private void clickPrevOrNext(View view){
        TextView prev, next;
        prev = view.findViewById(R.id.idPreviousInsp);
        next = view.findViewById(R.id.idNextInsp);

        prev.setOnClickListener(v12 -> {
            if (index > 0) {
                index--;
                currDay = dayEntities.get(index).getDay();
                idDateReport.setText(currDay + "," + Calendar.getInstance().get(Calendar.YEAR));
                dvirViewModel.getCurrentName().postValue(currDay);
            }
        });

        next.setOnClickListener(v1 -> {
            if (index < dayEntities.size()-1) {
                index++;
                currDay = dayEntities.get(index).getDay();
                idDateReport.setText(currDay + "," + Calendar.getInstance().get(Calendar.YEAR));
                dvirViewModel.getCurrentName().postValue(currDay);
            }
        });
    }

    private String getString(ArrayList<String> arrayList){
        if (arrayList.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append(arrayList.get(i)).append(",");
            }
            return stringBuilder.toString();
        }else return "";
    }
}