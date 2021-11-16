package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.shared_prefs.SessionManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DocsFragment extends Fragment {
    private String mParam1;
    private TextView idDrawSignature;
    private SignaturePad idSignature;
    private Button idClearSignature,idSubmitSignature;

    public static DocsFragment newInstance(String param1) {
        DocsFragment fragment = new DocsFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("ARG_PARAM1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_docs, container, false);
        idDrawSignature = v.findViewById(R.id.idDrawSignature);
        idSignature = v.findViewById(R.id.idSignature);
        idClearSignature = v.findViewById(R.id.idClearSignature);
        idSubmitSignature = v.findViewById(R.id.idSubmitSignature);

        SessionManager sessionManager = new SessionManager(requireContext());

        idSignature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                idDrawSignature.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {
                idDrawSignature.setVisibility(View.GONE);
            }

            @Override
            public void onClear() {
                idDrawSignature.setVisibility(View.VISIBLE);
            }
        });

        idClearSignature.setOnClickListener(v1 -> {
            idSignature.clear();
        } );

        idSubmitSignature.setOnClickListener(v2 -> {
            if (!idSignature.isEmpty()){
                sessionManager.clearSignature();
                sessionManager.saveSignature(idSignature.getSignatureBitmap());
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setTitle("Signature Submitted")
                        .setMessage("Signed signature was submitted!")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }else{
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext());
                alertDialog.setTitle("Signature missed")
                        .setMessage("Sign or take your saved signatures")
                        .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                AlertDialog alert = alertDialog.create();
                alert.show();
            }
        });
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSMSHandler(ExampleSMSModel sendModels){
    }
}