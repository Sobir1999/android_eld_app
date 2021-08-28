package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.iosix.eldblesample.adapters.TrailerRecyclerAdapter;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

import static android.app.Activity.RESULT_OK;

public class AddDvirFragment extends Fragment {
    private TextView addUnit, nextText;
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
    private final String selectedUnit = "No Unit Selected";
    private ArrayList<TrailersEntity> selectedTrailers;
    private TrailerRecyclerAdapter adapter;
    private String unitDefectsString = "", trailerDefectsString = "";

    public AddDvirFragment() {
    }

    public static AddDvirFragment newInstance() {
        AddDvirFragment fragment = new AddDvirFragment();
//        Bundle args = new Bundle();d
//        fragment.daoViewModel = dayDaoViewModel;
//        fragment.setArguments(args);
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
        unitDefects = v.findViewById(R.id.unitDefects);
        trailerDefects = v.findViewById(R.id.trailerDefects);
        unitDefectsTitle = v.findViewById(R.id.unitDefectsTitle);
        trailerDefectsTitle = v.findViewById(R.id.trailerDefectsTitle);
        addTime = v.findViewById(R.id.idDefectTimeText);
        notes = v.findViewById(R.id.notes);
        addDefect = v.findViewById(R.id.addDefect);
        defects = v.findViewById(R.id.defects);
        backView = v.findViewById(R.id.idImageBack);
        nextText = v.findViewById(R.id.idAddDvirNext);
        appBarLayout = v.findViewById(R.id.idAppbar);
        RecyclerView recyclerView = v.findViewById(R.id.idTrailersRecyclerView);

        buttonClicks(container);

        daoViewModel = ViewModelProviders.of(requireActivity()).get(DayDaoViewModel.class);
        daoViewModel.getGetAllVehicles().observe((LifecycleOwner) requireContext(), vehiclesEntities -> units.setText(vehiclesEntities.isEmpty() ? selectedUnit : vehiclesEntities.get(0).getName()));

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TrailerRecyclerAdapter(selectedTrailers);
        recyclerView.setAdapter(adapter);

        adapterListener();

        return v;
    }

    private void adapterListener() {
        adapter.setUpdateListener((position) -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Trailers");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllTrailers().observe((LifecycleOwner) requireContext(), trailersEntities -> {
                for (int i = 0; i < trailersEntities.size(); i++) {
                    arrayAdapter.add(trailersEntities.get(i).getNumber());
                }
            });

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                selectedTrailers.get(position).setNumber(strName);
                adapter.notifyDataSetChanged();
            });
            builderSingle.show();

            adapter.notifyDataSetChanged();
        });

        adapter.setDeleteListener((position) -> {
            selectedTrailers.remove(position);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 100 && resultCode == RESULT_OK) {

                addDefect.setVisibility(View.GONE);
                defects.setVisibility(View.VISIBLE);

                unitDefectsString = data.getStringExtra("unitDefects");
                trailerDefectsString = data.getStringExtra("trailerDefects");

                unitDefects.setText(unitDefectsString);
                trailerDefects.setText(trailerDefectsString);

                if (unitDefectsString.equals("")) {
                    unitDefectsTitle.setVisibility(View.GONE);
                    unitDefects.setVisibility(View.GONE);
                }

                if (trailerDefectsString.equals("")) {
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
        selectedTrailers = new ArrayList<>();

        addUnit.setOnClickListener(v -> createDialog(context, "Add Unit", "Add Unit", 1));

        units.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Unit");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllVehicles().observe((LifecycleOwner) requireContext(), vehiclesEntities -> {
                for (int i = 0; i < vehiclesEntities.size(); i++) {
                    arrayAdapter.add(vehiclesEntities.get(i).getName());
                }
            });

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                units.setText(strName);
            });
            builderSingle.show();
        });

        addTrailer.setOnClickListener(v -> createDialog(context, "Add Trailer", "Add Trailer", 2));

        addTime.setOnClickListener(v -> {
            DialogFragment timeFragment = new TimePickerFragment();
            assert getFragmentManager() != null;
            timeFragment.show(getFragmentManager(), "time picker");
        });

        trailers.setOnClickListener(v -> {
            AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
            builderSingle.setTitle("Select Trailers");

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
            daoViewModel.getGetAllTrailers().observe((LifecycleOwner) requireContext(), trailersEntities -> {
                for (int i = 0; i < trailersEntities.size(); i++) {
                    arrayAdapter.add(trailersEntities.get(i).getNumber());
                }
            });

            builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

            builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
                String strName = arrayAdapter.getItem(which);
                selectedTrailers.add(new TrailersEntity(strName));
                adapter.notifyDataSetChanged();
            });
            builderSingle.show();
        });

        backView.setOnClickListener(v -> {
            assert getFragmentManager() != null;
            getFragmentManager().popBackStack();
        });

        nextText.setOnClickListener(v1 -> {
            loadFragment(SignatureFragment.newInstance(!unitDefectsString.equalsIgnoreCase("") || !trailerDefectsString.equalsIgnoreCase("")));
            appBarLayout.setVisibility(View.GONE);
        });

        addDefect.setOnClickListener(v1 -> {
            Intent intent = new Intent(requireContext(), AddDefectActivity.class);
            intent.putExtra("isTruckSelected", !selectedTrailers.isEmpty());
            startActivityForResult(intent, 100);
        });

        defects.setOnClickListener(v1 -> {
            Intent intent = new Intent(requireContext(), AddDefectActivity.class);
            intent.putExtra("isTruckSelected", !selectedTrailers.isEmpty());
            startActivityForResult(intent, 100);
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
            } else if (type == 2 && !editText.getText().toString().equalsIgnoreCase("")) {
                try {
                    daoViewModel.insertTrailer(new TrailersEntity(editText.getText().toString()));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (type == 1 && editText.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(requireContext(), "Insert Unit Number!!!", Toast.LENGTH_SHORT).show();
            } else if (type == 2 && editText.getText().toString().equalsIgnoreCase("")) {
                Toast.makeText(requireContext(), "Insert Trailer Number!!!", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setNegativeButton("Cancel", (dialog1, which) -> dialog1.cancel());

        dialog.setView(v);
        dialog.show();

//        editText.getText().toString().trim();
    }

    @SuppressWarnings("deprecation")
    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getParentFragmentManager();
        assert fm != null;
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.addToBackStack("fragment");
        fragmentTransaction.replace(R.id.fragment_container_add_dvir, fragment);
        fragmentTransaction.commit();
    }

    private String getDialogString() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        builderSingle.setTitle("Select Trailers");
        final String[] strName = new String[1];

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_selectable_list_item);
        daoViewModel.getGetAllTrailers().observe((LifecycleOwner) requireContext(), trailersEntities -> {
            for (int i = 0; i < trailersEntities.size(); i++) {
                arrayAdapter.add(trailersEntities.get(i).getNumber());
            }
        });

        builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

        builderSingle.setAdapter(arrayAdapter, (dialog, which) -> {
            strName[0] = arrayAdapter.getItem(which);
            adapter.notifyDataSetChanged();
        });
        builderSingle.show();

        return strName[0];
    }
}