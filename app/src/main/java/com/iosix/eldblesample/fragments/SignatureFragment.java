package com.iosix.eldblesample.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.appbar.AppBarLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static android.os.Build.VERSION.SDK_INT;


public class SignatureFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean mParam1;
    private ArrayList<String> mParams;
    private DayDaoViewModel daoViewModel;
    private DvirViewModel dvirViewModel;

    private SignaturePad signature,mechanicSignature;
    private LinearLayout mechanicLayout;
    private TextView previousSignature, clearSignature,drawSignature,drawMecanicSignature;
    private boolean hasSignature = false,hasMechanicSignature = false;
    private boolean isDefectsCorrected = false;
    private Context context;
    private Bitmap bitmap;
    private SignatureViewModel signatureViewModel;
    List<SignatureEntity> signatureEntities = new ArrayList<>();
    private RadioButton no_defect, correct_defect, corrected_defect;
    private ConstraintLayout mechanicCons;
    private TextView mechamnicTextView, mechanicText;
    private String day;
    private SignaturePrefs signaturePrefs;

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
            mParams = getArguments().getStringArrayList(ARG_PARAM2);
            day = getArguments().getString("PARAM");
        }
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

        signaturePrefs = new SignaturePrefs(context);

        signaturePrefs.clearDefect();
        signaturePrefs.clearSignature();
        signaturePrefs.clearMechanicSignature();

        signatureViewModel = new SignatureViewModel(requireActivity().getApplication());
        signatureViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(SignatureViewModel.class);
        signatureViewModel.getMgetAllSignatures().observe(getViewLifecycleOwner(), signatureEntities -> {
//            this.signatureEntities = signatureEntities;
            if (signatureEntities.size() > 0){
                bitmap = signatureEntities.get(signatureEntities.size() - 1).getSignatureBitmap();
                previousSignature.setClickable(true);
                previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                previousSignature.setOnClickListener(v -> {
                    signature.setSignatureBitmap(bitmap);
                });
                Log.d("Signature missed","" + signatureEntities.size());
                Log.d("Signature missed","" + signatureEntities.get(signatureEntities.size() - 1).getSignatureBitmap());
            }else {
                previousSignature.setClickable(false);
                previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));

            }
        });

//        if (signatureEntities.size() > 0) {
//            bitmap = signatureEntities.get(signatureEntities.size() - 1).getSignatureBitmap();
//            Log.d("Signature missed", String.valueOf(signatureEntities.size()));
//
//        } else {
//            bitmap = null;
//        }

        selectRadio();

        actionFunctions();

        return view;
    }

    private void actionFunctions() {


        signature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                clearSignature.setClickable(true);
                drawSignature.setVisibility(View.GONE);

            }

            @Override
            public void onSigned() {
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                clearSignature.setClickable(true);
                signaturePrefs.saveSignature(signature.getSignatureBitmap());
                drawSignature.setVisibility(View.GONE);
            }

            @Override
            public void onClear() {
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                clearSignature.setClickable(false);
                signaturePrefs.clearSignature();
                drawSignature.setVisibility(View.VISIBLE);
            }
        });

        clearSignature.setOnClickListener(v -> signature.clear()
        );

//        if (bitmap != null){
//            previousSignature.setClickable(true);
//            previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
//            previousSignature.setOnClickListener(v -> {
//                    signature.setSignatureBitmap(bitmap);
//            });
//        }else {
//            previousSignature.setClickable(false);
//            previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
//
//        }

        mechanicSignature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                mechamnicTextView.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                mechamnicTextView.setClickable(true);
                drawMecanicSignature.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {
                mechamnicTextView.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                mechamnicTextView.setClickable(true);
                drawMecanicSignature.setVisibility(View.GONE);
                hasMechanicSignature = true;
                signaturePrefs.saveMechanicSignature(mechanicSignature.getSignatureBitmap());
            }

            @Override
            public void onClear() {
                mechamnicTextView.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
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
                    isDefectsCorrected = true;

                } else {
                    mechanicLayout.setVisibility(View.INVISIBLE);
                    isDefectsCorrected = false;
                }
            });
        } else {
            no_defect.setChecked(true);
            correct_defect.setClickable(false);
            corrected_defect.setClickable(false);
        }
    }

}