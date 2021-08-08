package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.iosix.eldblesample.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SendLogFragment extends Fragment {

    ImageView img;
    TextInputEditText textInputEditText;
    Button button;
    Context context;
    View content;
    Bitmap bitmap;
    LinearLayout scrollView;
    String email;
    LinearLayout xmlToPdf;

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

        requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        return view;
    }

    private void loadViews(View view){

        img = view.findViewById(R.id.idImageBack);
        textInputEditText = view.findViewById(R.id.idSendLogEdittext);
        button = view.findViewById(R.id.idSendLogs);
        scrollView = view.findViewById(R.id.idContainerSendLod);
        email = view.findViewById(R.id.idSendLogEdittext).toString();
        xmlToPdf = content.findViewById(R.id.idPrintView);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                PdfGenerator.getBuilder()
//                        .setContext(context)
//                        .fromViewSource()
//                        .fromView(content)
//                        .setPageSize(PdfGenerator.PageSize.A4)
//                        .setFileName("Log Reports")
//                        .setFolderName("FastLogz/")
//                        .openPDFafterGeneration(true)
//                        .build(new PdfGeneratorListener() {
//                            @Override
//                            public void showLog(String log) {
//                                super.showLog(log);
//                            }
//
//                            @Override
//                            public void onStartPDFGeneration() {
//
//                            }
//
//                            @Override
//                            public void onFinishPDFGeneration() {
//
//                            }
//
//                            @Override
//                            public void onSuccess(SuccessResponse response) {
//                                super.onSuccess(response);
//                            }
//
//                            @Override
//                            public void onFailure(FailureResponse failureResponse) {
//                                super.onFailure(failureResponse);
//                            }
//                        });

                bitmap = Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                Drawable drawable = view.getBackground();
                if (drawable != null){
                    drawable.draw(canvas);
                }else {
                    canvas.drawColor(Color.WHITE);
                    drawable.draw(canvas);
                }

                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                        .Builder(960,1280,1)
                        .create();

                PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                page.getCanvas().drawBitmap(bitmap,0,0,null);
                pdfDocument.finishPage(page);

                String pdfFile = "Fastlogz/" + "Log Reports.pdf";
                File myPdfFile = new File(pdfFile);

                try {
                    pdfDocument.writeTo(new FileOutputStream(myPdfFile));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                pdfDocument.close();

                sendPdftoEmail(email);

            }
        });

    }



    private void sendPdftoEmail(String eEmail){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,eEmail);

        File file = new File("/sdcard/"+"FastLogz"+"/"+"Log Reports.pdf");

        Uri URI = Uri.fromFile(file);
        emailIntent.setDataAndType(URI,"application/pdf");
        emailIntent.putExtra(Intent.EXTRA_STREAM,URI);
        context.startActivity(Intent.createChooser(emailIntent,"Send email..."));
    }

}