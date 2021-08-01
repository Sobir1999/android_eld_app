package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.appbar.AppBarLayout;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.AddDefectActivity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import static android.app.Activity.RESULT_OK;

public class AddDvirFragment extends Fragment{
    private TextView addUnit;
    private TextView units;
    private TextView addTrailer;
    private TextView trailers;
    private TextView unitDefects, trailerDefects, unitDefectsTitle, trailerDefectsTitle;
    private TextView addTime;
    private TextView notes;
    private ImageView backView;
    private LinearLayout defects, addDefect;
    private AppBarLayout appBarLayout;
    private DayDaoViewModel daoViewModel;

    public static AddDvirFragment newInstance(DayDaoViewModel dayDaoViewModel) {
        AddDvirFragment fragment = new AddDvirFragment();
        Bundle args = new Bundle();
        fragment.daoViewModel = dayDaoViewModel;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_add_dvir, container, false);

        addUnit = v.findViewById(R.id.idAddDvirUnitText);
        units = v.findViewById(R.id.idAddDvirUnitNumberText);
        addTrailer = v.findViewById(R.id.idAddDvirTrailerText);
        trailers = v.findViewById(R.id.idAddDvirTrailerNumberText);
        trailers = v.findViewById(R.id.idAddDvirTrailerNumberText);
        unitDefects = v.findViewById(R.id.unitDefects);
        trailerDefects = v.findViewById(R.id.trailerDefects);
        unitDefectsTitle = v.findViewById(R.id.unitDefectsTitle);
        trailerDefectsTitle = v.findViewById(R.id.trailerDefectsTitle);
        addTime = v.findViewById(R.id.idDefectTimeText);
        notes = v.findViewById(R.id.notes);
        addDefect = v.findViewById(R.id.addDefect);
        defects = v.findViewById(R.id.defects);
        backView = v.findViewById(R.id.idImageBack);
        TextView nextText = v.findViewById(R.id.idAddDvirNext);
        appBarLayout = v.findViewById(R.id.idAppbar);


        daoViewModel.getGetAllVehicles().observe(requireActivity(), dayEntities -> {
            for (int i = 0; i < dayEntities.size(); i++) {
                Log.d("ADDDVIR", "onChanged: " + dayEntities.get(i).getName());
            }
        });

        buttonClicks(container);

        nextText.setOnClickListener(v1 -> {
            loadFragment(new SignatureFragment());
            appBarLayout.setVisibility(View.GONE);
        });

        addDefect.setOnClickListener(v1 -> startActivityForResult(new Intent(requireContext(), AddDefectActivity.class), 100));
        defects.setOnClickListener(v1 -> startActivityForResult(new Intent(requireContext(), AddDefectActivity.class), 100));
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {
                String unitDefectsString, trailerDefectsString;

                addDefect.setVisibility(View.GONE);
                defects.setVisibility(View.VISIBLE);

                unitDefectsString = data.getStringExtra("unitDefects");
                trailerDefectsString = data.getStringExtra("trailerDefects");

                unitDefects.setText(unitDefectsString);
                trailerDefects.setText(trailerDefectsString);

                if(unitDefectsString.equals("")){
                    unitDefectsTitle.setVisibility(View.GONE);
                    unitDefects.setVisibility(View.GONE);
                }

                if(trailerDefectsString.equals("")){
                    trailerDefectsTitle.setVisibility(View.GONE);
                    trailerDefects.setVisibility(View.GONE);
                }

                notes.setText(data.getStringExtra("notes"));

            } else {
                Toast.makeText(requireContext(), "Not selected item", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(requireContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("deprecation")
    private void buttonClicks(ViewGroup context) {
        addUnit.setOnClickListener(v -> createDialog(context, "Add Unit", "Add Unit", 1));

        units.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select One Name:-");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice);
            arrayAdapter.add("Hardik");
            arrayAdapter.add("Archit");
            arrayAdapter.add("Jignesh");
            arrayAdapter.add("Umang");
            arrayAdapter.add("Gatti");

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

//            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
//                String strName = arrayAdapter.getItem(which);
//                AlertDialog.Builder builderInner = new AlertDialog.Builder(getContext());
//                builderInner.setMessage(strName);
//                builderInner.setTitle("Your Selected Item is");
//                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog,int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builderInner.show();
//            });
            builderSingle.show();
        });

        addTrailer.setOnClickListener(v -> createDialog(context, "Add Trailer", "Add Trailer", 2));

        addTime.setOnClickListener(v->{
            DialogFragment timeFragment = new TimePickerFragment();
            timeFragment.show(getFragmentManager() != null ? getFragmentManager() : null, "time picker");
        });

        trailers.setOnClickListener(v -> {

        });

        backView.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });
    }

    private void createDialog(ViewGroup group, String title, String hint, int type) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(title);

        View v = getLayoutInflater().inflate(R.layout.add_unit_trailer_dialog_view, group, false);
        EditText editText = v.findViewById(R.id.idAddUnitTrailerEdit);
        editText.setHint(hint);

        dialog.setPositiveButton("Ok", (dialog12, which) -> {
            if (type == 1 && !editText.getText().toString().equalsIgnoreCase("")) {
                daoViewModel.insertVehicle(new VehiclesEntity(editText.getText().toString()));
            }
        });

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());

        dialog.setView(v);
        dialog.show();

//        editText.getText().toString().trim();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.replace(R.id.fragment_container_add_dvir, fragment);
        fragmentTransaction.commit();

    }
}