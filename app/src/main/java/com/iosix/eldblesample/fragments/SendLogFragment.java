package com.iosix.eldblesample.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.print.PrintAttributes;
import android.print.pdf.PrintedPdfDocument;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class SendLogFragment extends Fragment {

    ImageView img;
    TextInputEditText textInputEditText;
    Button button;
    Context context;
    View content;
    Bitmap bitmap;

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
        content = inflater.inflate(R.layout.fragment_begin_inspection,null      );
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

                bitmap = loadBitmapFromView(content, 900, 1280);
                createPdf();
                sendPdfToGmail();

            }
        });

    }

    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }

    private void createPdf(){
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels ;

        int convertHighet = (int) hight, convertWidth = (int) width;

//        Resources mResources = getResources();
//        Bitmap bitmap = BitmapFactory.decodeResource(mResources, R.drawable.screenshot);

        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);

        // write the document content
        String targetPdf = "/sdcard/FastLogz/Log Reports.pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(context, "PDF is created!!!", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();

    }

    private void openGeneratedPDF(){
        File file = new File("/sdcard/FastLogz/Log Reports.pdf");
        if (file.exists())
        {
            Uri uri;
            Intent intent=new Intent(Intent.ACTION_VIEW);
            if (Build.VERSION.SDK_INT > 24){
                uri = Uri.parse(file.getPath());
            }else {
                uri = Uri.fromFile(file);
            }
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            try
            {
                startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(context, "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void sendPdfToGmail(){
        String gmail = textInputEditText.getText().toString();
        Uri uri;
        File file = new File("/sdcard/FastLogz/Log Reports.pdf");
        
        Intent sendPdfIntent = new Intent(Intent.ACTION_SEND);
        sendPdfIntent.putExtra(Intent.EXTRA_EMAIL,gmail);
        if (Build.VERSION.SDK_INT > 24){
            uri = Uri.parse(file.getPath());
        }else {
            uri = Uri.fromFile(file);
        }
        sendPdfIntent.setDataAndType(uri, "application/pdf");
        sendPdfIntent.putExtra(Intent.EXTRA_STREAM,uri);
        startActivity(Intent.createChooser(sendPdfIntent,"Choose an email client.."));
    }
}