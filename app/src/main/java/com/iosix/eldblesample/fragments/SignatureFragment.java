package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.interfaces.Communicator;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.viewModel.SignatureViewModel;


public class SignatureFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    private boolean mParam1;

    private SignaturePad signature;
    private TextView previousSignature, clearSignature,drawSignature;
    private RadioButton no_defect, correct_defect, corrected_defect;
    private SignatureViewModel signatureViewModel;
    private SignaturePrefs signaturePrefs;
    Communicator comm;

    public SignatureFragment() {
    }

    public static SignatureFragment newInstance(boolean param1,String day) {
        SignatureFragment fragment = new SignatureFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString("PARAM",day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
        }
        signatureViewModel = ViewModelProviders.of(requireActivity()).get(SignatureViewModel.class);
        signaturePrefs = SignaturePrefs.getInstance(requireContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature, container, false);
        signature = view.findViewById(R.id.idSignature);
        previousSignature = view.findViewById(R.id.idTVPreviousSignature);
        clearSignature = view.findViewById(R.id.idTVClearSignature);
        drawSignature = view.findViewById(R.id.idTvDrawSignature);
        no_defect = view.findViewById(R.id.idNoDefects);
        correct_defect = view.findViewById(R.id.idNeedCorrected);
        corrected_defect = view.findViewById(R.id.idCorrected);

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
                drawSignature.setVisibility(View.GONE);
                comm.sendBitmap(signature.getSignatureBitmap());
            }

            @Override
            public void onClear() {
                clearSignature.setClickable(false);
                drawSignature.setVisibility(View.VISIBLE);
                comm.sendBitmap(null);
                signature.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.add_defect_sign_background));
            }
        });

        clearSignature.setOnClickListener(v ->{
            signature.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.add_defect_sign_background));
            signature.clear();
        });

        signatureViewModel.getLastSign(getContext(),previousSignature,signature);
    }

    private void selectRadio() {

        if (mParam1) {
            correct_defect.setChecked(true);
            no_defect.setClickable(false);
            corrected_defect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                signaturePrefs.saveDefect(isChecked);
                comm.sendHasDefect(isChecked);
            });
        } else {
            no_defect.setChecked(true);
            correct_defect.setClickable(false);
            corrected_defect.setClickable(false);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        comm = (Communicator) context;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
    }
}