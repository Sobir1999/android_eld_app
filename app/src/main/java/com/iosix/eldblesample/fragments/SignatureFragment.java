package com.iosix.eldblesample.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.viewModel.SignatureViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutionException;


public class SignatureFragment extends Fragment {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    SignaturePad signature;
    TextView previousSignature,clearSignature,save,drawSignature;
    private boolean hasSignature = false;
    private Context context;
    Bitmap bitmap;
    SignatureViewModel signatureViewModel;

    public SignatureFragment() {
        // Required empty public constructor
    }

    public static SignatureFragment newInstance() {
        SignatureFragment fragment = new SignatureFragment();
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
        View view = inflater.inflate(R.layout.fragment_signature, container, false);

        context = container.getContext();

        ImageView img = view.findViewById(R.id.idImageBack);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });


        signature = view.findViewById(R.id.idSignature);
        previousSignature = view.findViewById(R.id.idTVPreviousSignature);
        clearSignature = view.findViewById(R.id.idTVClearSignature);
        save = view.findViewById(R.id.idAddDvirSave);
        drawSignature = view.findViewById(R.id.idTvDrawSignature);

        signatureViewModel = new SignatureViewModel(requireActivity().getApplication());
        signatureViewModel = ViewModelProviders.of((FragmentActivity) context).get(SignatureViewModel.class);
        signatureViewModel.getMgetAllSignatures().observe(getViewLifecycleOwner(), signatureEntities -> {
            if (signatureEntities.size() > 0){
            bitmap = signatureEntities.get(signatureEntities.size()-1).getSignatureBitmap();
            }else {
                bitmap = null;
            }
        });

        isFirstTime();

        signature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                clearSignature.setClickable(true);
                if (bitmap != null){
                    previousSignature.setClickable(true);
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                }else {
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                    previousSignature.setClickable(false);
                }
                drawSignature.setVisibility(View.GONE);

            }

            @Override
            public void onSigned() {
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                clearSignature.setClickable(true);
                hasSignature = true;
                if (bitmap != null){
                    previousSignature.setClickable(true);
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                }else {
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                    previousSignature.setClickable(false);
                }
                drawSignature.setVisibility(View.GONE);


            }

            @Override
            public void onClear() {
                hasSignature = false;
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                clearSignature.setClickable(false);
                if (bitmap != null){
                    previousSignature.setClickable(true);
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                }else {
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                    previousSignature.setClickable(false);
                }
                drawSignature.setVisibility(View.VISIBLE);
            }
        });

        clearSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.clear();
            }
        });

        previousSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature.setSignatureBitmap(bitmap);
                }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (hasSignature){
                    Bitmap signatureBitmap = signature.getSignatureBitmap();
                    SignatureEntity signatureEntity = new SignatureEntity(signatureBitmap);
                    try {
                        signatureViewModel.insertSignature(signatureEntity);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (addJpgSignatureToGallery(signatureBitmap)){
                        Toast.makeText(context, "Signature saved into the Gallery", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Signature missed")
                            .setMessage("Sign or take your saved signatures")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    alertDialog.setCancelable(true);
                                }
                            });
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }
        });

        return view;
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.jpg", System.currentTimeMillis()));
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    void isFirstTime(){
        if (bitmap != null){
            previousSignature.setClickable(true);
            previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
        }else {
            previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
            previousSignature.setClickable(false);
        }
    }
}