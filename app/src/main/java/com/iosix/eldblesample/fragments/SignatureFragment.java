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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DiffUtil;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.appbar.AppBarLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.MechanicSignatureEntity;
import com.iosix.eldblesample.roomDatabase.entities.SignatureEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.SignatureViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class SignatureFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean mParam1;
    private ArrayList<String> mParams;
    private DayDaoViewModel daoViewModel;
    private DvirViewModel dvirViewModel;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad signature,mechanicSignature;
    private LinearLayout mechanicLayout;
    private TextView previousSignature, clearSignature, save, drawSignature,drawMecanicSignature;
    private boolean hasSignature = false,hasMechanicSignature = false;
    private boolean isDefectsCorrected = false;
    private Context context;
    private Bitmap bitmap;
    private SignatureViewModel signatureViewModel;
    private RadioButton no_defect, correct_defect, corrected_defect;
    private ConstraintLayout mechanicCons;
    private TextView mechamnicTextView, mechanicText;
    private ImageView img;
    private String day;
    private AppBarLayout appBarLayout;

    public SignatureFragment() {
        // Required empty public constructor
    }

    public static SignatureFragment newInstance(boolean param1,ArrayList<String> params,String day) {
        SignatureFragment fragment = new SignatureFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putStringArrayList(ARG_PARAM2, params);
        args.putString("PARAM",day);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParams = getArguments().getStringArrayList(ARG_PARAM2);
            day = getArguments().getString("PARAM");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature, container, false);

        context = requireContext();

        img = view.findViewById(R.id.idImageBack);
        signature = view.findViewById(R.id.idSignature);
        previousSignature = view.findViewById(R.id.idTVPreviousSignature);
        clearSignature = view.findViewById(R.id.idTVClearSignature);
        save = view.findViewById(R.id.idAddDvirSave);
        drawSignature = view.findViewById(R.id.idTvDrawSignature);
        no_defect = view.findViewById(R.id.idNoDefects);
        correct_defect = view.findViewById(R.id.idNeedCorrected);
        corrected_defect = view.findViewById(R.id.idCorrected);
        mechamnicTextView = view.findViewById(R.id.idClearSignTextViewMechanic);
        mechanicCons = view.findViewById(R.id.idMechanicCons);
        mechanicText = view.findViewById(R.id.idMechanicSignatureText);
        mechanicSignature = view.findViewById(R.id.idSignatureMechanic);
        drawMecanicSignature = view.findViewById(R.id.idTvDrawSignatureMechanic);
        mechanicLayout = view.findViewById(R.id.idMechanicLayout);
        appBarLayout = view.findViewById(R.id.idAppbarSignature);

        signatureViewModel = new SignatureViewModel(requireActivity().getApplication());
        signatureViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(SignatureViewModel.class);
        signatureViewModel.getMgetAllSignatures().observe(getViewLifecycleOwner(), signatureEntities -> {
            if (signatureEntities.size() > 0) {
                bitmap = signatureEntities.get(signatureEntities.size() - 1).getSignatureBitmap();
            } else {
                bitmap = null;
            }
        });


        verifyStoragePermissions(requireActivity());
        isFirstTime();
        selectRadio();

        actionFunctions();

        img.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });

        return view;
    }

    private void actionFunctions() {


        signature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                clearSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                clearSignature.setClickable(true);
                if (bitmap != null) {
                    previousSignature.setClickable(true);
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                } else {
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
                if (bitmap != null) {
                    previousSignature.setClickable(true);
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                } else {
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
                if (bitmap != null) {
                    previousSignature.setClickable(true);
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                } else {
                    previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                    previousSignature.setClickable(false);
                }
                drawSignature.setVisibility(View.VISIBLE);
            }
        });



        clearSignature.setOnClickListener(v -> signature.clear());

        previousSignature.setOnClickListener(v -> signature.setSignatureBitmap(bitmap));

        mechanicSignature.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                mechamnicTextView.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                mechamnicTextView.setClickable(true);
                drawMecanicSignature.setVisibility(View.GONE);
            }

            @Override
            public void onSigned() {
                mechamnicTextView.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
                mechamnicTextView.setClickable(true);
                drawMecanicSignature.setVisibility(View.GONE);
                hasMechanicSignature = true;

            }

            @Override
            public void onClear() {
                mechamnicTextView.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
                mechamnicTextView.setClickable(false);
                drawMecanicSignature.setVisibility(View.VISIBLE);
                hasMechanicSignature = false;

            }
        });

        mechamnicTextView.setOnClickListener(v -> mechanicSignature.clear());

        save.setOnClickListener(v -> {

            daoViewModel = new DayDaoViewModel(this.getActivity().getApplication());

            daoViewModel = ViewModelProviders.of(this).get(DayDaoViewModel.class);
            daoViewModel.getMgetAllDays().observe(getViewLifecycleOwner(), dayEntities -> {
            });

            dvirViewModel = new DvirViewModel(requireActivity().getApplication());
            dvirViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(DvirViewModel.class);

            if (isDefectsCorrected){
                if (hasSignature && hasMechanicSignature) {

                    Bitmap signatureBitmap = signature.getSignatureBitmap();
                    Bitmap mechanicBitmap = mechanicSignature.getSignatureBitmap();

                    SignatureEntity signatureEntity = new SignatureEntity(signatureBitmap);
                    try {
                        signatureViewModel.insertSignature(signatureEntity);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    MechanicSignatureEntity mechanicSignatureEntity = new MechanicSignatureEntity(mechanicBitmap);
                    try {
                        signatureViewModel.insertMechanicSignature(mechanicSignatureEntity);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (addJpgSignatureToGallery(signatureBitmap) && addJpgSignatureToGallery(mechanicBitmap)) {
                        try {
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),mParams.get(1),mParams.get(2),mParams.get(3),true,
                                    mParams.get(4),mParams.get(5),mParams.get(6), day
                            ));
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        loadFragment(LGDDFragment.newInstance(3,daoViewModel,mParams));
                        appBarLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                    }

                } else{
                     if (hasMechanicSignature){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Signature missed")
                                .setMessage("Sign or take your saved signatures")
                                .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setTitle("Signature missed")
                                .setMessage("Mechanic must sign")
                                .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                        AlertDialog alert = alertDialog.create();
                        alert.show();
                    }
                }
            }else {
                if (hasSignature){
                    Bitmap signatureBitmap = signature.getSignatureBitmap();
                    SignatureEntity signatureEntity = new SignatureEntity(signatureBitmap);
                    try {
                        signatureViewModel.insertSignature(signatureEntity);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (addJpgSignatureToGallery(signatureBitmap)) {

                        try {
                            if (!mParams.get(2).equals("No unit selected") || !mParams.get(3).equals("No trailer selected")){
                            dvirViewModel.insertDvir(new DvirEntity(
                                    mParams.get(0),mParams.get(1),mParams.get(2),mParams.get(3),false,
                                    mParams.get(4),mParams.get(5),mParams.get(6), day
                            ));
                            }else {
                                dvirViewModel.insertDvir(new DvirEntity(
                                        mParams.get(0),mParams.get(1),mParams.get(2),mParams.get(3),true,
                                        mParams.get(4),mParams.get(5),mParams.get(6), day
                                ));
                            }
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities -> {
                        });

                        loadFragment(LGDDFragment.newInstance(3,daoViewModel,mParams));
                        appBarLayout.setVisibility(View.GONE);
                    } else {
                        Toast.makeText(context, "Unable to store the signature", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Signature missed")
                            .setMessage("Sign or take your saved signatures")
                            .setPositiveButton("OK", (dialog, which) -> alertDialog.setCancelable(true));
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                }
            }
        });
    }

    private void selectRadio() {

        if (mParam1) {
            correct_defect.setChecked(true);
            no_defect.setClickable(false);
            corrected_defect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    mechanicLayout.setVisibility(View.VISIBLE);
                    isDefectsCorrected = true;

                } else {
                    mechanicLayout.setVisibility(View.INVISIBLE);
                    isDefectsCorrected = false;
                }
            });
        } else {
            no_defect.setChecked(true);
            correct_defect.setClickable(false);
            corrected_defect.setClickable(false);
        }
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

    void isFirstTime() {
        if (bitmap != null) {
            previousSignature.setClickable(true);
            previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorWhenClicked));
        } else {
            previousSignature.setTextColor(getResources().getColor(R.color.SignatureColorDefault));
            previousSignature.setClickable(false);
        }
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.idFragmentSignatureContainer, fragment);
        fragmentTransaction.commit();
    }
}