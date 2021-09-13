package com.iosix.eldblesample.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.iosix.eldblesample.R;

import java.util.Properties;

import static com.iosix.eldblesample.MyApplication.context;

public class InspectionModuleFragment extends Fragment {

    Button beginInspection, sendLogs, sendELDFile;
    ImageView img;
    View content;

    public InspectionModuleFragment() {
        // Required empty public constructor
    }

    public static InspectionModuleFragment newInstance() {
        InspectionModuleFragment fragment = new InspectionModuleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inspection_module, container, false);

        content = inflater.inflate(R.layout.fragment_begin_inspection, container, false);

        loadViews(view);
        return view;
    }

    private void loadViews(View view) {

        img = view.findViewById(R.id.idImageBack);
        beginInspection = view.findViewById(R.id.idBeginInspection);
        sendLogs = view.findViewById(R.id.idSendLogs);
        sendELDFile = view.findViewById(R.id.idSendELDFile);

        img.setOnClickListener(v -> getFragmentManager().popBackStack());

        sendLogs.setOnClickListener(v -> {
//            loadFragment(SendLogFragment.newInstance());

            PdfGenerator.getBuilder()
                    .setContext(requireContext())
                    .fromViewSource()
                    .fromView(content)
                    .setPageSize(PdfGenerator.PageSize.A4)
                    .setFileName("Log Reports")
                    .setFolderName("FastLogz/")
                    .openPDFafterGeneration(true)
                    .build(new PdfGeneratorListener() {
                        @Override
                        public void showLog(String log) {
                            super.showLog(log);
                        }

                        @Override
                        public void onStartPDFGeneration() {

                        }

                        @Override
                        public void onFinishPDFGeneration() {

                        }

                        @SuppressLint("IntentReset")
                        @Override
                        public void onSuccess(SuccessResponse response) {
                            super.onSuccess(response);
//                            Log.d("PDF", "onSuccess: PDF generated" + response.getPdfDocument());
//
//                            String username = "axrorbekbebitovich97@gmail.com";
//                            String password = "04kun10oy1997yil";
//
//                            String message = "It is message";
//
//                            Properties prop = new Properties();
//                            prop.put("mail.smtp.auth", "true");
//                            prop.put("mail.smtp.starttls.enable", "true");
//                            prop.put("mail.smtp.host", "smtp.gmail.com");
//                            prop.put("mail.smtp.port", "587");

                        }

                        @Override
                        public void onFailure(FailureResponse failureResponse) {
                            super.onFailure(failureResponse);
                        }
                    });
        });

        beginInspection.setOnClickListener(v -> {
            loadFragment(BeginInspectionFragment.newInstance());
        });

    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}