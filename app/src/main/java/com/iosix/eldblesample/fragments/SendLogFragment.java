package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.enums.Day.dateToString;
import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;
import static com.iosix.eldblesample.utils.Utils.getDateFormat;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.adapters.InspectionLogAdapter;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.utils.UploadRequestBody;
import com.iosix.eldblesample.utils.Utils;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class SendLogFragment extends Fragment implements UploadRequestBody.UploadCallback {

    ImageView img;
    TextInputEditText textInputEditText;
    Button button;
    Context context;
    private GeneralViewModel generalViewModel;
    private DvirViewModel dvirViewModel;
    private DayDaoViewModel dayDaoViewModel;
    private DriverSharedPrefs driverSharedPrefs;
    private StatusDaoViewModel statusDaoViewModel;
    private EldJsonViewModel eldJsonViewModel;

    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];

    private List<DayEntity> dayEntities = new ArrayList<>();
    private List<GeneralEntity> generalEntities = new ArrayList<>();
    private List<LogEntity> logEntities = new ArrayList<>();
    ArrayList<LogEntity> truckStatusEntitiesCurr = new ArrayList<>();
    ArrayList<LogEntity> otherStatusEntities = new ArrayList<>();

    View view1;
    DisplayMetrics displayMetrics;
    Bitmap bitmap;
    Canvas canvas;
    PdfDocument pdfDocument;
    PdfDocument.PageInfo pageInfo;
    PdfDocument.Page page;
    File filePath;

    ImageView idSignatureReport;
    TextView idDateReport,idDriverName,idCoDriver,idDriverID,idTimeZone,idTimeZoneOffset,idDistance,idCarrier,
            idTruckTracktorId,idTrailerID,idTruckTracktorVIN,idELDProvider,idMainOffice,idHomeTerminal,
            idShippingID,idFrom,idTo,idCurrentLocation,idNotes,idTotalHoursSinceRestart,idHrsAvailableToday,idHrsWorkedToday;
    CustomStableRulerChart idCustomChart;
    CustomLiveRulerChart idCustomChartLive;
    RecyclerView inspectionRv;

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
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        generalViewModel = ViewModelProviders.of(requireActivity()).get(GeneralViewModel.class);
        dayDaoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);
        driverSharedPrefs = DriverSharedPrefs.getInstance(requireContext());
        statusDaoViewModel = ViewModelProviders.of(requireActivity()).get(StatusDaoViewModel.class);
        eldJsonViewModel = ViewModelProviders.of(requireActivity()).get(EldJsonViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_send_log, container, false);
        context = container.getContext();
        setUpViewModels();
        loadViews(view);
        return view;
    }

    private void loadViews(View view) {

        img = view.findViewById(R.id.idImageBack);
        textInputEditText = view.findViewById(R.id.idSendLogEdittext);
        button = view.findViewById(R.id.idSendLogs);

        img.setOnClickListener(v -> getFragmentManager().popBackStack());

        button.setOnClickListener(v -> {
            if (!textInputEditText.getText().toString().equals("")){
                try {
                    inflateXmltoView(textInputEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void inflateXmltoView(String email) throws IOException {

        LayoutInflater inflater = LayoutInflater.from(context);
        view1 = inflater.inflate(R.layout.view_to_pdf,null);
        idDateReport = view1.findViewById(R.id.idDateReport);
        idDriverName = view1.findViewById(R.id.idDriverName);
        idCoDriver = view1.findViewById(R.id.idCoDriver);
        idDriverID = view1.findViewById(R.id.idDriverID);
        idTimeZone = view1.findViewById(R.id.idTimeZone);
        idTimeZoneOffset = view1.findViewById(R.id.idTimeZoneOffset);
        idDistance = view1.findViewById(R.id.idDistance);
        idCarrier = view1.findViewById(R.id.idCarrier);
        idTruckTracktorId = view1.findViewById(R.id.idVehicleVin);
        idTrailerID = view1.findViewById(R.id.idTrailers);
        idELDProvider = view1.findViewById(R.id.idELDProvider);
        idMainOffice = view1.findViewById(R.id.idMainOffice);
        idHomeTerminal = view1.findViewById(R.id.idHomeTerminal);
        idShippingID = view1.findViewById(R.id.idShippingID);
        idFrom = view1.findViewById(R.id.idFrom);
        idTo = view1.findViewById(R.id.idTo);
        idCurrentLocation = view1.findViewById(R.id.idCurrentLocation);
        idNotes = view1.findViewById(R.id.idNotes);
        idSignatureReport = view1.findViewById(R.id.idSignatureReport);
        idCustomChart = view1.findViewById(R.id.idCustomChart);
        idCustomChartLive = view1.findViewById(R.id.idCustomChartLive);
        inspectionRv = view1.findViewById(R.id.idInspectionRv);
        idTotalHoursSinceRestart = view1.findViewById(R.id.idTotalHoursSinceRestart);
        idHrsAvailableToday = view1.findViewById(R.id.idHrsAvailableToday);
        idHrsWorkedToday = view1.findViewById(R.id.idHrsWorkedToday);

        pdfDocument = new PdfDocument();

        for (int i = dayEntities.size()-1; i >=0; i--) {
            idDriverName.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname()));
            idDriverID.setText(driverSharedPrefs.getDriverId());
            idCarrier.setText(driverSharedPrefs.getCompany());
            idMainOffice.setText(driverSharedPrefs.getMainOffice());
            idHomeTerminal.setText(driverSharedPrefs.getHomeTerAdd());
            idTimeZone.setText(TimeZone.getDefault().getID());
            idDateReport.setText(String.format("%s,%s",dayEntities.get(i).getDay(), Calendar.getInstance().get(Calendar.YEAR)));
            int j = 0;
            if (generalEntities.size()>0){
                for (int n = generalEntities.size()-1; n >=0; n--) {
                    try {
                        if (dateToString(generalEntities.get(n).getDay()).equals(dayEntities.get(i).getDay())){
                            j++;
                            idTrailerID.setText(getString(generalEntities.get(n).getTrailers()));
                            idShippingID.setText(getString(generalEntities.get(n).getShippingDocs()));
                            idCoDriver.setText(getString(generalEntities.get(n).getCo_driver_name()));
                            idTruckTracktorId.setText(getString(generalEntities.get(n).getVehicle()));
                            idFrom.setText(generalEntities.get(n).getFrom_info());
                            idTo.setText(generalEntities.get(n).getTo_info());
                            idNotes.setText(generalEntities.get(n).getNote());
                            break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                if (j == 0){
                    idTrailerID.setText("");
                    idShippingID.setText("");
                    idCoDriver.setText("");
                    idTruckTracktorId.setText("");
                    idFrom.setText("");
                    idTo.setText("");
                    idNotes.setText("");
                }
            }

            InspectionLogAdapter inspectionLogAdapter = new InspectionLogAdapter(requireContext(),logEntities,dayEntities.get(i).getDay());
            inspectionRv.setAdapter(inspectionLogAdapter);

            truckStatusEntitiesCurr.clear();
            otherStatusEntities.clear();
            for (int m = 0; m < logEntities.size(); m++) {
                if (logEntities.get(m).getTime().equals(dayEntities.get(i).getDay())){
                    if (logEntities.get(m).getTo_status() < 6){
                        truckStatusEntitiesCurr.add(logEntities.get(m));
                    }else {
                        otherStatusEntities.add(logEntities.get(m));
                    }
                }
            }
            if(dayEntities.get(i).getDay().equals(today)){
                idCustomChartLive.setVisibility(View.VISIBLE);
                idCustomChart.setVisibility(View.GONE);
                idCustomChartLive.setArrayList(truckStatusEntitiesCurr,otherStatusEntities);
            }else {
                idCustomChartLive.setVisibility(View.GONE);
                idCustomChart.setVisibility(View.VISIBLE);
                idCustomChart.setArrayList(truckStatusEntitiesCurr,otherStatusEntities);
            }
            fetchDimensToView();
            createBitmap();
            attachBitmapToPdf(i+1);
        }
        savePdfToFile(email);
    }

    private void setUpViewModels(){
        dayDaoViewModel.getMgetAllDays().observe(getViewLifecycleOwner(),dayEntities -> {
            this.dayEntities = dayEntities;
        });

        generalViewModel.getMgetGenerals().observe(getViewLifecycleOwner(),generalEntities -> {
            this.generalEntities = generalEntities;
        });

        statusDaoViewModel.getmAllStatus().observe(getViewLifecycleOwner(),logEntities -> {
            for (int i = 0; i < logEntities.size(); i++) {
                if (logEntities.get(i).getDriverId().equals(driverSharedPrefs.getDriverId())){
                    this.logEntities.add(logEntities.get(i));
                }
            }
        });
    }

    private void fetchDimensToView(){
        displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            context.getDisplay().getRealMetrics(displayMetrics);
        }else {
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        }

        view1.measure(
                View.MeasureSpec.makeMeasureSpec(
                        displayMetrics.widthPixels,View.MeasureSpec.EXACTLY
                ),
                View.MeasureSpec.makeMeasureSpec(
                        displayMetrics.heightPixels,View.MeasureSpec.EXACTLY
                )
        );
    }

    private void createBitmap(){
        view1.layout(0,0,displayMetrics.widthPixels,displayMetrics.heightPixels);
        bitmap = Bitmap.createBitmap(view1.getMeasuredWidth(),view1.getMeasuredHeight(),Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        view1.draw(canvas);
        Bitmap.createScaledBitmap(bitmap,displayMetrics.widthPixels,displayMetrics.heightPixels,true);
    }

    private void attachBitmapToPdf(int pageNumber){
        pageInfo = new PdfDocument.PageInfo.Builder(displayMetrics.widthPixels,displayMetrics.heightPixels,pageNumber).create();
        page = pdfDocument.startPage(pageInfo);
        page.getCanvas().drawBitmap(bitmap,0F,0F,null);
        pdfDocument.finishPage(page);
    }

    private void savePdfToFile(String email) throws IOException {
        String fileName = "LogReports" + new Date().getTime() + ".pdf";
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File filepath = new File(path, "logReports.pdf");
        pdfDocument.writeTo(new FileOutputStream(filepath));
        pdfDocument.close();
        Log.d("Adverse Diving",filepath.getAbsolutePath());
//        File file = new File(getPathFromURI(Uri.parse(filepath.getAbsolutePath())));
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), filepath);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf_file", filepath.getName(), requestFile);
        RequestBody mail = RequestBody.create(MediaType.parse("text/plain"), email);
        eldJsonViewModel.sendPdf(mail,body);

    }

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Files.FileColumns.DISPLAY_NAME};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
            res = cursor.getString(column_index);
            cursor.close();
        }

        return res;
    }

    private String getString(ArrayList<String> arrayList){
        if (arrayList.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                stringBuilder.append(arrayList.get(i)).append(",");
            }
            return stringBuilder.toString();
        }else return "";
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }
}
