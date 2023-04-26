package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.activity.SignatureActivity.verifyStoragePermissions;
import static com.iosix.eldblesample.enums.Day.stringToDateTime;
import static com.iosix.eldblesample.enums.Day.stringToDay;
import static com.iosix.eldblesample.utils.Utils.getDateFormat;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.Status;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.utils.UploadRequestBody;
import com.iosix.eldblesample.utils.Utils;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class DocsFragment extends Fragment implements UploadRequestBody.UploadCallback {
    private String day;
    private TextView idDrawSignature;
    private SignaturePad idSignature;
    private Button idClearSignature,idSubmitSignature;
    private DvirViewModel dvirViewModel;
    private SessionManager sessionManager;
    private SignaturePrefs signaturePrefs;
    private EldJsonViewModel eldJsonViewModel;
    private SignatureViewModel signatureViewModel;
    private StatusDaoViewModel statusDaoViewModel;
    private DriverSharedPrefs driverSharedPrefs;

    public static DocsFragment newInstance() {
        DocsFragment fragment = new DocsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        eldJsonViewModel = ViewModelProviders.of(requireActivity()).get(EldJsonViewModel.class);
        signatureViewModel = ViewModelProviders.of(requireActivity()).get(SignatureViewModel.class);
        statusDaoViewModel = ViewModelProviders.of(requireActivity()).get(StatusDaoViewModel.class);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        signaturePrefs = new SignaturePrefs(context.getApplicationContext());
        sessionManager = SessionManager.getInstance(context.getApplicationContext());
        driverSharedPrefs = DriverSharedPrefs.getInstance(context.getApplicationContext());
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

        verifyStoragePermissions(requireActivity());

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

        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(),currDay ->{
            day = currDay;
            signatureViewModel.getMgetAllSignatures(getContext(),currDay,idSignature);
        });

        idClearSignature.setOnClickListener(v1 -> {
            idSignature.setBackground(AppCompatResources.getDrawable(requireContext(),R.drawable.add_defect_sign_background));
            idSignature.clear();
        });

        idSubmitSignature.setOnClickListener(v2 -> {
            if (!idSignature.isEmpty()){
                String fileName = "DriverSignature" + new Date().getTime() + ".png";
                OutputStream outputStream;

                try {
                    ContentResolver resolver = getContext().getContentResolver();
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);
                    values.put(MediaStore.MediaColumns.MIME_TYPE,"image/png");
                    Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
                    outputStream = resolver.openOutputStream(Objects.requireNonNull(imageUri));
                    idSignature.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    Uri uri = getImageUri(getContext(),idSignature.getSignatureBitmap());
                    File file = new File(getPathFromURI(uri));
                    RequestBody body = new UploadRequestBody(file,"image",this);
                    eldJsonViewModel.sendSignature(MultipartBody.Part.createFormData("sign",file.getName(),body));
                    Objects.requireNonNull(outputStream);
                }catch (Exception e){
                    Log.d("Adverse",e.getMessage());
                }

                signatureViewModel.insertSignature(new SignatureEntity(idSignature.getSignatureBitmap(),stringToDay(day)));
                eldJsonViewModel.postStatus(new Status(EnumsConstants.CERTIFIED,null,null,stringToDateTime(day)));
                statusDaoViewModel.insertStatus(new Status(EnumsConstants.CERTIFIED,null,null,stringToDateTime(day)));

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

    public String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

}