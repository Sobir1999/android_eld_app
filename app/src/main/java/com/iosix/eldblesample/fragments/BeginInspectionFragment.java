package com.iosix.eldblesample.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.UserViewModel;

public class BeginInspectionFragment extends Fragment {

    private UserViewModel userViewModel;
    private GeneralViewModel generalViewModel;

    ImageView idSignatureReport;
    TextView idDateReport,idDriverName,idCoDriver,idDriverID,idTimeZone,idDistance,idCarrier,
            idTruckTracktorId,idTrailerID,idTruckTracktorVIN,idELDProvider,idMainOffice,idHomeTerminal,
            idShippingID,idFrom,idTo,idCurrentLocation,idNotes;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        generalViewModel = new GeneralViewModel(this.requireActivity().getApplication());
        generalViewModel = ViewModelProviders.of(this).get(GeneralViewModel.class);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_begin_inspection, container, false);
        idDateReport = view.findViewById(R.id.idDateReport);
        idDriverName = view.findViewById(R.id.idDriverName);
        idCoDriver = view.findViewById(R.id.idCoDriver);
        idDriverID = view.findViewById(R.id.idDriverID);
        idTimeZone = view.findViewById(R.id.idTimeZone);
        idDistance = view.findViewById(R.id.idDistance);
        idCarrier = view.findViewById(R.id.idCarrier);
//        idTruckTracktorId = view.findViewById(R.id.idTruckTracktorId);
//        idTrailerID = view.findViewById(R.id.idTrailerID);
//        idTruckTracktorVIN = view.findViewById(R.id.idTruckTracktorVIN);
        idELDProvider = view.findViewById(R.id.idELDProvider);
        idMainOffice = view.findViewById(R.id.idMainOffice);
        idHomeTerminal = view.findViewById(R.id.idHomeTerminal);
        idShippingID = view.findViewById(R.id.idShippingID);
        idFrom = view.findViewById(R.id.idFrom);
        idTo = view.findViewById(R.id.idTo);
        idCurrentLocation = view.findViewById(R.id.idCurrentLocation);
        idNotes = view.findViewById(R.id.idNotes);
        idSignatureReport = view.findViewById(R.id.idSignatureReport);
        return  view;
    }
}