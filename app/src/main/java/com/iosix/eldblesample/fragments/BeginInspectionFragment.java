package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.Day.stringToDay;
import static com.iosix.eldblesample.enums.HOSConstants.SHIFTLIMIT;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.customViews.MyListView;
import com.iosix.eldblesample.interfaces.FetchStatusCallback;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import org.threeten.bp.ZonedDateTime;

import java.util.List;
import java.util.TimeZone;

public class BeginInspectionFragment extends Fragment {

    private GeneralViewModel generalViewModel;
    private DvirViewModel dvirViewModel;
    private DayDaoViewModel dayDaoViewModel;
    private DriverSharedPrefs driverSharedPrefs;
    private StatusDaoViewModel statusDaoViewModel;
    private SignatureViewModel signatureViewModel;
    private CustomStableRulerChart idCustomChart;
    private CustomLiveRulerChart idCustomChartLive;

    ImageView idSignatureReport;
    TextView idDateReport,idDriverName,idCoDriver,idDriverID,idTimeZone,idTimeZoneOffset,idDistance,idCarrier,
            idTruckTracktorId,idTrailerID,idTruckTracktorVIN,idELDProvider,idMainOffice,idHomeTerminal,
            idShippingID,idFrom,idTo,idCurrentLocation,idNotes,idTotalHoursSinceRestart,idHrsAvailableToday,idHrsWorkedToday;
    MyListView inspectionRv;

    public BeginInspectionFragment() {
        // Required empty public constructor
    }

    public static BeginInspectionFragment newInstance() {
        return new BeginInspectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        generalViewModel = ViewModelProviders.of(requireActivity()).get(GeneralViewModel.class);
        dayDaoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);
        driverSharedPrefs = DriverSharedPrefs.getInstance(requireContext());
        statusDaoViewModel = ViewModelProviders.of(requireActivity()).get(StatusDaoViewModel.class);
        signatureViewModel = ViewModelProviders.of(requireActivity()).get(SignatureViewModel.class);
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

        dayDaoViewModel.getDaysforInspection(getDayFormat(ZonedDateTime.now()),dvirViewModel,idDateReport);

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(),day ->{
            signatureViewModel.getLastSignatures(day,idSignatureReport);

            statusDaoViewModel.inspectionLogList(getContext(),day,inspectionRv);

            if(day.equals(getDayFormat(ZonedDateTime.now()))){
                idCustomChart.setVisibility(View.GONE);
                idCustomChartLive.setVisibility(View.VISIBLE);
                statusDaoViewModel.getCurDateStatus(idCustomChartLive,idCustomChart,day);
            }else {
                idCustomChartLive.setVisibility(View.GONE);
                idCustomChart.setVisibility(View.VISIBLE);
                statusDaoViewModel.getCurDateStatus(idCustomChartLive,idCustomChart,day);
            }
            idHrsWorkedToday.setText(intToTime(driverSharedPrefs.getWorkingHours(driverSharedPrefs.getDriverId()+ " " + day)));
            if (SHIFTLIMIT - driverSharedPrefs.getWorkingHours(driverSharedPrefs.getDriverId()+ " " + day) >= 0){
                idHrsAvailableToday.setText(intToTime(SHIFTLIMIT - driverSharedPrefs.getWorkingHours(driverSharedPrefs.getDriverId()+ " " + day)));
            }else {
                idHrsAvailableToday.setText(intToTime(0));
            }

            generalViewModel.getCurrDayGenerals(stringToDay(day), (vehicles, trailersEntities, co_drivers, shippingDocs, from, to, notes) -> {
                if (trailersEntities != null) {
                    idTrailerID.setText(getString(trailersEntities));
                } else {
                    idTrailerID.setText("");
                }
                if (shippingDocs != null) {
                    idShippingID.setText(getString(shippingDocs));
                } else {
                    idShippingID.setText("");
                }
                if (co_drivers != null) {
                    idCoDriver.setText(getString(co_drivers));
                } else {
                    idCoDriver.setText("");
                }
                if (vehicles != null) {
                    idTruckTracktorId.setText(getString(vehicles));
                } else {
                    idTruckTracktorId.setText("");
                }
                if (!from.equals("")) {
                    idFrom.setText(from);
                } else {
                    idFrom.setText("");
                }
                if (!to.equals("")) {
                    idTo.setText(to);
                } else {
                    idTo.setText("");
                }
                if (!notes.equals("")) {
                    idNotes.setText(notes);
                } else {
                    idNotes.setText("");
                };
            });
        });
    }

    private String getString(List<String> arrayList){
        if (arrayList.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == arrayList.size() -1){
                    stringBuilder.append(arrayList.get(i));
                }else {
                    stringBuilder.append(arrayList.get(i)).append(",");
                }
            }
            return stringBuilder.toString();
        }else return "";
    }

    private void clickPrevOrNext(View view){
        TextView prev, next;
        prev = view.findViewById(R.id.idPreviousInsp);
        next = view.findViewById(R.id.idNextInsp);

        prev.setOnClickListener(v12 -> dayDaoViewModel.clickPrevButtonForIns(idDateReport,dvirViewModel));

        next.setOnClickListener(v1 -> dayDaoViewModel.clickNextButtonForIns(idDateReport,dvirViewModel));
    }
}