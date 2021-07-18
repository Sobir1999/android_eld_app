package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

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
import android.widget.TextView;
import android.widget.Toast;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

public class AddDvirFragment extends Fragment {
    private TextView addUnit, units, addTrailer, trailers, nextText, idDefectNoText, idAddDefectText;
    private ImageView backView;
    private DayDaoViewModel daoViewModel;

    public static AddDvirFragment newInstance(DayDaoViewModel dayDaoViewModel) {
        AddDvirFragment fragment = new AddDvirFragment();
        Bundle args = new Bundle();
        fragment.daoViewModel = dayDaoViewModel;
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            daoViewModel = (DayDaoViewModel) getArguments().getSerializable("ARG_PARAM1");
////            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_dvir, container, false);

        addUnit = v.findViewById(R.id.idAddDvirUnitText);
        units = v.findViewById(R.id.idAddDvirUnitNumberText);
        addTrailer = v.findViewById(R.id.idAddDvirTrailerText);
        trailers = v.findViewById(R.id.idAddDvirTrailerNumberText);
        backView = v.findViewById(R.id.idImageBack);
        nextText = v.findViewById(R.id.idAddDvirNext);
        idDefectNoText = v.findViewById(R.id.idDefectNoText);
        idAddDefectText = v.findViewById(R.id.idAddDefectText);

        daoViewModel.getGetAllVehicles().observe(requireActivity(), dayEntities -> {
            for (int i=0; i<dayEntities.size(); i++) {
                Log.d("ADDDVIR", "onChanged: " + dayEntities.get(i).getName());
            }
        });

        buttonClicks(container);

        return v;
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

        trailers.setOnClickListener(v -> {

        });

        backView.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });

        nextText.setOnClickListener(v -> {

        });

        idDefectNoText.setOnClickListener(v-> loadLGDDFragment(AddDefectFragment.newInstance(daoViewModel)));
        idAddDefectText.setOnClickListener(v-> {
            loadLGDDFragment(AddDefectFragment.newInstance(daoViewModel));
            Toast.makeText(requireContext(), "clicked", Toast.LENGTH_SHORT).show();
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

//        editText.getText().toString();
    }

    private void loadLGDDFragment(Fragment fragment) {
        FragmentManager fm = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.replace(R.id.drawer_layout, fragment);
        fragmentTransaction.commit();
    }
}