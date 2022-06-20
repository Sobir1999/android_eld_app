package com.iosix.eldblesample.fragments;

import static com.iosix.eldblesample.activity.SignatureActivity.verifyStoragePermissions;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.shared_prefs.SessionManager;
import com.iosix.eldblesample.shared_prefs.SignaturePrefs;
import com.iosix.eldblesample.utils.UploadRequestBody;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;
import com.iosix.eldblesample.viewModel.apiViewModel.EldJsonViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
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

    public static DocsFragment newInstance(String param1) {
        DocsFragment fragment = new DocsFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            day = getArguments().getString("ARG_PARAM1");
        }
        eldJsonViewModel = ViewModelProviders.of(requireActivity()).get(EldJsonViewModel.class);

        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);

        signatureViewModel = ViewModelProviders.of(requireActivity()).get(SignatureViewModel.class);

        sessionManager = new SessionManager(requireContext());
        signaturePrefs = new SignaturePrefs(requireContext());
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
            signatureViewModel.getMgetAllSignatures().observe(getViewLifecycleOwner(),signatureEntities -> {
                ArrayList<SignatureEntity> arrayList= new ArrayList<>();
                for (int i = 0; i < signatureEntities.size(); i++) {
                    if (signatureEntities.get(i).getDay().equals(currDay)){
                        arrayList.add(signatureEntities.get(i));
                    }
                }if (arrayList.size() != 0){
                    idSignature.setSignatureBitmap(arrayList.get(arrayList.size()-1).getSignatureBitmap());
                }else {
                    idSignature.clear();
                }
            });
        });

        idClearSignature.setOnClickListener(v1 -> {
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
                    Uri uri = getImageUri(getActivity().getApplicationContext(),idSignature.getSignatureBitmap());
                    File file = new File(getPathFromURI(uri));
                    RequestBody body = new UploadRequestBody(file,"image",this);
                    eldJsonViewModel.sendSignature(MultipartBody.Part.createFormData("sign",file.getName(),body));
                    Objects.requireNonNull(outputStream);

                    signatureViewModel.insertSignature(new SignatureEntity(idSignature.getSignatureBitmap(),day));
                }catch (Exception e){
                }

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
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSMSHandler(ExampleSMSModel sendModels){
    }

    @Override
    public void onProgressUpdate(int percentage) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
    }
}