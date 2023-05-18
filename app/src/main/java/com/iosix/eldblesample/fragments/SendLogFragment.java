package com.iosix.eldblesample.fragments;


import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.getDayTime1;
import static com.iosix.eldblesample.enums.Day.stringToDate;
import static com.iosix.eldblesample.enums.Day.stringToDay;

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
import com.iosix.eldblesample.customViews.MyListView;
import com.iosix.eldblesample.interfaces.AllDaysAdd;
import com.iosix.eldblesample.interfaces.FetchStatusCallback;
import com.iosix.eldblesample.interfaces.GeneralsCallback;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.GeneralEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.utils.UploadRequestBody;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.GeneralViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.threeten.bp.ZonedDateTime;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
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
    private List<View> viewList = new ArrayList<>();

    private String time = "" + Calendar.getInstance().getTime();
    public String today = time.split(" ")[1] + " " + time.split(" ")[2];

    private List<DayEntity> dayEntities = new ArrayList<>();
    private List<GeneralEntity> generalEntities = new ArrayList<>();
//    private List<LogEntity> logEntities = new ArrayList<>();
//    ArrayList<LogEntity> truckStatusEntitiesCurr = new ArrayList<>();
//    ArrayList<LogEntity> otherStatusEntities = new ArrayList<>();

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
    MyListView inspectionRv;

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
        loadViews(view);
        return view;
    }

    private void loadViews(View view) {

        img = view.findViewById(R.id.idImageBack);
        textInputEditText = view.findViewById(R.id.idSendLogEdittext);
        button = view.findViewById(R.id.idSendLogs);

        inflateXmltoView();

        img.setOnClickListener(v -> getFragmentManager().popBackStack());

        button.setOnClickListener(v -> {
            if (!Objects.requireNonNull(textInputEditText.getText()).toString().equals("")){
                for (int i = 0; i < viewList.size(); i++) {
                    fetchDimensToView(viewList.get(i));
                    createBitmap(viewList.get(i));
                    attachBitmapToPdf(i+1);
                }
                try {
                    savePdfToFile(textInputEditText.getText().toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void inflateXmltoView() {

        pdfDocument = new PdfDocument();

        dayDaoViewModel.getAllDays(dayEntities -> {
            for (int i = 0; i <= dayEntities.size()-1; i++) {
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
                DayEntity day = dayEntities.get(i);
                idDriverName.setText(String.format("%s %s", driverSharedPrefs.getFirstname(), driverSharedPrefs.getLastname()));
                idDriverID.setText(driverSharedPrefs.getDriverId());
                idCarrier.setText(driverSharedPrefs.getCompany());
                idMainOffice.setText(driverSharedPrefs.getMainOffice());
                idHomeTerminal.setText(driverSharedPrefs.getHomeTerAdd());
                idTimeZone.setText(TimeZone.getDefault().getID());
                idDateReport.setText(dayEntities.get(i).getDay());

                generalViewModel.getCurrDayGenerals(stringToDay(day.getDay()), (vehicles, trailersEntities, co_drivers, shippingDocs, from, to, notes) -> {
                    if (trailersEntities != null) {
                        idTrailerID.setText(getString(trailersEntities));
                    } else {
                        idTrailerID.setText("");
                    }
                    if (shippingDocs != null) {
                        idShippingID.setText(getString(shippingDocs));
                    } else {
                        idShippingID.setText("");
                    }
                    if (co_drivers != null) {
                        idCoDriver.setText(getString(co_drivers));
                    } else {
                        idCoDriver.setText("");
                    }
                    if (vehicles != null) {
                        idTruckTracktorId.setText(getString(vehicles));
                    } else {
                        idTruckTracktorId.setText("");
                    }
                    if (!from.equals("")) {
                        idFrom.setText(from);
                    } else {
                        idFrom.setText("");
                    }
                    if (!to.equals("")) {
                        idTo.setText(to);
                    } else {
                        idTo.setText("");
                    }
                    if (!notes.equals("")) {
                        idNotes.setText(notes);
                    } else {
                        idNotes.setText("");
                    };
                });

                statusDaoViewModel.inspectionLogList(context,day.getDay(),inspectionRv);

                if(day.getDay().equals(getDayFormat(ZonedDateTime.now()))){
                    idCustomChartLive.setVisibility(View.VISIBLE);
                    idCustomChart.setVisibility(View.GONE);
                    statusDaoViewModel.getCurDateStatus(idCustomChartLive,idCustomChart,day.getDay());
                }else {
                    idCustomChartLive.setVisibility(View.GONE);
                    idCustomChart.setVisibility(View.VISIBLE);
                    statusDaoViewModel.getCurDateStatus(idCustomChartLive,idCustomChart,day.getDay());
                }
                viewList.add(view1);
            }
        });
    }

    private void fetchDimensToView(View view1){
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

    private void createBitmap(View view1){
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
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

        File filepath = new File(path, "logReports" + ZonedDateTime.now().toLocalTime().toSecondOfDay() + ".pdf");
        pdfDocument.writeTo(new FileOutputStream(filepath));
        pdfDocument.close();

        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), filepath);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf_file", filepath.getName(), requestFile);

        RequestBody mail = RequestBody.create(MediaType.parse("text/plain"), email);
        eldJsonViewModel.sendPdf(mail, body);

    }

    private String getString(List<String> arrayList){
        if (arrayList.size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < arrayList.size(); i++) {
                if (i == arrayList.size() -1){
                    stringBuilder.append(arrayList.get(i));
                }else {
                    stringBuilder.append(arrayList.get(i)).append(",");
                }
            }
            return stringBuilder.toString();
        }else return "";
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }
}
