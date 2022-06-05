package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;

import java.util.ArrayList;
import java.util.List;


public class SignatureFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean mParam1;
    private DayDaoViewModel daoViewModel;
    private DvirViewModel dvirViewModel;

    private SignaturePad signature,mechanicSignature;
    private LinearLayout mechanicLayout;
    private TextView previousSignature, clearSignature,drawSignature,drawMecanicSignature,previousMechanicSignature;
    private boolean hasSignature = false,hasMechanicSignature = false;
    private Context context;
    private Bitmap bitmap;
    List<SignatureEntity> signatureEntities = new ArrayList<>();
    List<MechanicSignatureEntity> mechanicSignatureEntities = new ArrayList<>();
    private RadioButton no_defect, correct_defect, corrected_defect;
    private ConstraintLayout mechanicCons;
    private TextView mechamnicTextView, mechanicText;
    private String day;
    private SignaturePrefs signaturePrefs;
    private SessionManager sessionManager;
    private SignatureViewModel signatureViewModel;

    public SignatureFragment() {
        // Required empty public constructor
    }

    public static SignatureFragment newInstance(boolean param1,ArrayList<String> params,String day) {
        SignatureFragment fragment = new SignatureFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putStringArrayList(ARG_PARAM2, params);
        args.putString("PARAM",day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            ArrayList<String> mParams = getArguments().getStringArrayList(ARG_PARAM2);
            day = getArguments().getString("PARAM");
        }
        signaturePrefs = new SignaturePrefs(context);
        sessionManager = new SessionManager(context);
        signatureViewModel = ViewModelProviders.of(requireActivity()).get(SignatureViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature, container, false);

        context = requireContext();


        signature = view.findViewById(R.id.idSignature);
        previousSignature = view.findViewById(R.id.idTVPreviousSignature);
        clearSignature = view.findViewById(R.id.idTVClearSignature);
        drawSignature = view.findViewById(R.id.idTvDrawSignature);
        no_defect = view.findViewById(R.id.idNoDefects);
        correct_defect = view.findViewById(R.id.idNeedCorrected);
        corrected_defect = view.findViewById(R.id.idCorrected);
        mechamnicTextView = view.findViewById(R.id.idClearSignTextViewMechanic);
        mechanicCons = view.findViewById(R.id.idMechanicCons);
        mechanicText = view.findViewById(R.id.idMechanicSignatureText);
        mechanicSignature = view.findViewById(R.id.idSignatureMechanic);
        drawMecanicSignature = view.findViewById(R.id.idTvDrawSignatureMechanic);
        mechanicLayout = view.findViewById(R.id.idMechanicLayout);
        previousMechanicSignature = view.findViewById(R.id.idTVPreviousMechanicSignature);


        signatureViewModel.getMgetAllSignatures().observe(getViewLifecycleOwner(), signatureEntities -> {
        });

        signatureViewModel.getMgetAllMechanicSignatures().observe(getViewLifecycleOwner(), mechanicSignatureEntities1 -> {
            if (mechanicSignatureEntities1.size() > 0){
                bitmap = mechanicSignatureEntities1.get(mechanicSignatureEntities1.size()-1).getMechanicSignatureBitmap();
                previousMechanicSignature.setClickable(true);
                previousMechanicSignature.setOnClickListener(v -> mechanicSignature.setSignatureBitmap(bitmap));
            }else {
                previousMechanicSignature.setClickable(false);
            }
        });

        selectRadio();

        actionFunctions();

        return view;
    }

    private void actionFunctions() {


        signature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                clearSignature.setClickable(true);
                drawSignature.setVisibility(View.GONE);

            }

            @Override
            public void onSigned() {
                clearSignature.setClickable(true);
                signaturePrefs.saveSignature(signature.getSignatureBitmap());
                drawSignature.setVisibility(View.GONE);
            }

            @Override
            public void onClear() {
                clearSignature.setClickable(false);
                drawSignature.setVisibility(View.VISIBLE);
            }
        });

        clearSignature.setOnClickListener(v -> signature.clear()
        );


        previousSignature.setOnClickListener(view -> {
            if (sessionManager.fetchSignature() != null){
                signature.setSignatureBitmap(sessionManager.fetchSignature());
            }
        });
        mechanicSignature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                mechamnicTextView.setClickable(true);
                drawMecanicSignature.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {
                mechamnicTextView.setClickable(true);
                drawMecanicSignature.setVisibility(View.GONE);
                hasMechanicSignature = true;
                signaturePrefs.saveMechanicSignature(mechanicSignature.getSignatureBitmap());
            }

            @Override
            public void onClear() {
                mechamnicTextView.setClickable(false);
                drawMecanicSignature.setVisibility(View.VISIBLE);
                hasMechanicSignature = false;
                signaturePrefs.clearMechanicSignature();

            }
        });

        mechamnicTextView.setOnClickListener(v -> mechanicSignature.clear());

    }

    private void selectRadio() {

        if (mParam1) {
            correct_defect.setChecked(true);
            no_defect.setClickable(false);
            corrected_defect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                signaturePrefs.saveDefect(isChecked);
                if (isChecked) {
                    mechanicLayout.setVisibility(View.VISIBLE);

                } else {
                    mechanicLayout.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            no_defect.setChecked(true);
            correct_defect.setClickable(false);
            corrected_defect.setClickable(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
    }
}