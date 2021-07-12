package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.iosix.eldblesample.R;

public class SendLogFragment extends Fragment {

    ImageView img;
    TextInputEditText textInputEditText;
    Button button;
    Context context;
    View content;

    public SendLogFragment() {
        // Required empty public constructor
    }

    public static SendLogFragment newInstance() {
        SendLogFragment fragment = new SendLogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_send_log, container, false);
        content = inflater.inflate(R.layout.fragment_begin_inspection,null);
        context = container.getContext();
        loadViews(view);
        return view;
    }

    private void loadViews(View view){

        img = view.findViewById(R.id.idImageBack);
        textInputEditText = view.findViewById(R.id.idSendLogEdittext);
        button = view.findViewById(R.id.idSendLogs);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PdfGenerator.getBuilder()
                        .setContext(context)
                        .fromViewSource()
                        .fromView(content)
                        .setCustomPageSize(900,1600)
                        .setFileName("Log Reports")
                        .setFolderName("FastLogz/")
                        .openPDFafterGeneration(true)
                        .build(new PdfGeneratorListener() {
                            @Override
                            public void showLog(String log) {
                                super.showLog(log);
                            }

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }

                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }
                        });

            }
        });

    }
}