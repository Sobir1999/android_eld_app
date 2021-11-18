package com.iosix.eldblesample.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.iosix.eldblesample.R;

public class SendToEmailFragment extends Fragment {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private String TAG ="SendToEmailFragment";
    private EditText etEmail;
    private EditText etSubject;
    private EditText etBody;
    private Button send;
    private TextView tvPdf;
    private String login;
    private String password;
    private String path;
    View content;
    boolean isAllFieldsChecked = false;

    public SendToEmailFragment() {
        // Required empty public constructor
    }


    public static SendToEmailFragment newInstance() {
        SendToEmailFragment fragment = new SendToEmailFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_to_email, container, false);
        content = inflater.inflate(R.layout.fragment_begin_inspection, container, false);
        etEmail = view.findViewById(R.id.etTo);
        etSubject = view.findViewById(R.id.etSubject);
        etBody = view.findViewById(R.id.etBody);
        tvPdf = view.findViewById(R.id.tvPdf);
        send = view.findViewById(R.id.btSend);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        login = prefs.getString("login", null);
        password = prefs.getString("password", null);
        Log.d("SendTo", "onCreateView: " + login);
        pdf();
        tvPdf.setTextColor(Color.BLUE);
        SpannableString content = new SpannableString(tvPdf.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvPdf.setText(content);
        tvPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PdfGenerator.getBuilder()
                        .setContext(requireContext())
                        .fromLayoutXMLSource()
                        .fromLayoutXML(R.layout.fragment_begin_inspection)
                        .setFileName("Log Reports")
                        .setFolderName(Environment.DIRECTORY_DOCUMENTS)
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

                            }


                            @Override
                            public void onFailure(FailureResponse failureResponse) {

                            }

                        });
            }
        });
        send.setOnClickListener(v -> {

            isAllFieldsChecked = CheckAllFields();
            if (isAllFieldsChecked) {
                AsyncTackRunner runner = new AsyncTackRunner();
                runner.execute();
            }


//            sender.execute();

        });


        return view;


    }

    public void pdf() {

        PdfGenerator.getBuilder()
                .setContext(requireContext())
                .fromLayoutXMLSource()
                .fromLayoutXML(R.layout.fragment_begin_inspection)
                .setFileName("Log Reports")
                .setFolderName(Environment.DIRECTORY_DOCUMENTS)
                .openPDFafterGeneration(false)
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
                        path = response.getPath();
                    }


                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

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
    private boolean CheckAllFields() {
        if (etEmail.length() == 0) {
            etEmail.setError("Email is required");
            return false;
        }
        return true;
    }
    private class AsyncTackRunner extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;
        @Override
        protected String doInBackground(String... strings) {
            try {
                GMailSender sender = new GMailSender("bmbmuhiddinova@gmail.com",
                        "Bmb197174",
                        etSubject.getText().toString(),
                        etBody.getText().toString(),
                        login,
                        etEmail.getText().toString(),
                        path);


                Log.d("SendTo", "onSuccess: " + etSubject.getText().toString());
                Log.d("SendTO", "onSuccess: " + path);
            }
            catch (Exception e){
                Log.e(TAG, "doInBackground: "+e.getMessage() );
            }
            return null;
        }
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Toast.makeText(requireActivity(), " Sent",Toast.LENGTH_SHORT).show();
            loadFragment(InspectionModuleFragment.newInstance());
        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(requireActivity(),
                   "",
                    "message sent");
        }


        @Override
        protected void onProgressUpdate(String... text) {


        }
    }


}