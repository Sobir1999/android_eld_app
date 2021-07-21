package com.iosix.eldblesample.fragments;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.view.LayoutInflater;
import android.view.PixelCopy;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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

                            @Override
                            public void onSuccess(SuccessResponse response) {
                                super.onSuccess(response);
                            }

                            @Override
                            public void onFailure(FailureResponse failureResponse) {
                                super.onFailure(failureResponse);
                            }
                        });

//                PrintAttributes printAttrs = new PrintAttributes.Builder().
//                        setColorMode(PrintAttributes.COLOR_MODE_MONOCHROME).
//                        setMediaSize(PrintAttributes.MediaSize.ISO_C7).
//                        setResolution(new PrintAttributes.Resolution("zooey", Context.PRINT_SERVICE, 500, 500)).
//                        setMinMargins(PrintAttributes.Margins.NO_MARGINS).
//                        build();
//
//                PdfDocument document = new PrintedPdfDocument(context, printAttrs);
//                PdfDocument.PageInfo pageInfo = new  PdfDocument.PageInfo.Builder(content.getWidth(), content.getHeight(), 1).create();
//                PdfDocument.Page page = document.startPage(pageInfo);
//                content.draw(page.getCanvas());
//                document.finishPage(page);
//
//                try {
//                    File pdfDirPath = new File(context.getFilesDir(), "pdfs");
//                    pdfDirPath.mkdirs();
//                    File file = new File(pdfDirPath, "Log Reports.pdf");
//                    OutputStream os = new FileOutputStream(file);
//                    document.writeTo(os);
//                    document.close();
//                    os.close();
//                } catch (IOException e) {
//                    throw new RuntimeException("Error generating file", e);
//                }

            }
        });

    }
}