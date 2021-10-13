package com.iosix.eldblesample.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.iosix.eldblesample.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

//import javax.mail.Authenticator;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.AddressException;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeMessage;

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

        LayoutInflater inflater2 = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_send_log, container, false);
        content = inflater2.inflate(R.layout.fragment_begin_inspection, null);
        context = container.getContext();
        loadViews(view);
        return view;
    }

    private void loadViews(View view) {

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
                        .fromView(content,content,content)
                        .setFileName("Log Reports")
                        .setFolderName("FastLogz")
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
                                Log.d("PDF", "onSuccess: PDF generated" + response.getPdfDocument());

                                String username = "axrorbekbebitovich97@gmail.com";
                                String password = "04kun10oy1997yil";

                                String message = "It is message";

                                Properties prop = new Properties();
                                prop.put("mail.smtp.auth", "true");
                                prop.put("mail.smtp.starttls.enable", "true");
                                prop.put("mail.smtp.host", "smtp.gmail.com");
                                prop.put("mail.smtp.port", "587");

//                                Session session = Session.getInstance(prop, new Authenticator() {
//                                    @Override
//                                    protected PasswordAuthentication getPasswordAuthentication() {
//                                        return new PasswordAuthentication(username, password);
//                                    }
//                                });
//
//                                try {
//                                    Message message1 = new MimeMessage(session);
//                                    message1.setFrom(new InternetAddress(username));
//                                    message1.addRecipients(Message.RecipientType.TO, InternetAddress.parse("axrorbekjorayev97@gmail.com"));
//                                    message1.setSubject("It is a subject.");
//                                    message1.setText("It is a Text.");
//                                    Transport.send(message1);
//
//                                    Toast.makeText(getContext(), "Subject send successfully", Toast.LENGTH_SHORT).show();
//                                } catch (MessagingException e) {
//                                    e.printStackTrace();
//                                }
                            }

                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }
                        });

            }
        });
    }

    @SuppressLint("IntentReset")
    protected void ShareViaEmail(String folder_name, String file_name) {
    }

}
