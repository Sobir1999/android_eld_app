package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.AddDvirActivity;
import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.adapters.DvirlistAdapter;
import com.iosix.eldblesample.models.ExampleSMSModel;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DVIRFragment extends Fragment {
    private boolean hasDefects = false;
    private TextView idDvirExText;
    private ImageView imageView;
    private Button createButton;
    private FloatingActionButton idFABAddDvir;
    private LinearLayout container1;
    private ConstraintLayout idDVIRContainer;
    private DvirViewModel dvirViewModel;
    private String currDay;
    private DvirlistAdapter adapter;
    private RecyclerView dvir_recyclerview;
    private List<DvirEntity> dvirEntities;


    public static DVIRFragment newInstance(String currDay) {
        DVIRFragment fragment = new DVIRFragment();
        Bundle args = new Bundle();
        args.putString("ARG_PARAM1", currDay);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currDay = getArguments().getString("ARG_PARAM1");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_d_v_i_r, container, false);
        container1 = v.findViewById(R.id.idDVIRContainer1);
        idDVIRContainer = v.findViewById(R.id.idDVIRContainer);
        idDvirExText = v.findViewById(R.id.idDvirExText);
        createButton = v.findViewById(R.id.create_new_dvir_button);
        dvir_recyclerview = v.findViewById(R.id.idDvirRecyclerView);
        idFABAddDvir = v.findViewById(R.id.idFABAddDvir);


        dvirViewModel = new DvirViewModel(requireActivity().getApplication());
        dvirViewModel = ViewModelProviders.of((FragmentActivity) requireContext()).get(DvirViewModel.class);

        dvirEntities = new ArrayList<>();

        dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities1 -> {

            dvirEntities.addAll(dvirEntities1);
            if (dvirEntities.size() > 0){
                adapter = new DvirlistAdapter(getContext(),dvirEntities1,currDay);
                dvir_recyclerview.setVisibility(View.VISIBLE);
                container1.setVisibility(View.GONE);
                idDVIRContainer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dvir_recyclerview.setAdapter(adapter);
                adapter.setListener(dvirEntity1 -> {
                    Dialog dialog = new Dialog(requireContext());
                    dialog.setContentView(R.layout.custom_delete_dvir_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                    dialog.findViewById(R.id.idDvirYes).setOnClickListener(view ->{
                        dvirViewModel.deleteDvir(dvirEntity1);
                        dvir_recyclerview.setVisibility(View.GONE);
                        idDVIRContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                        container1.setVisibility(View.VISIBLE);
                        dialog.dismiss();
                    });

                    dialog.findViewById(R.id.idDvirNo).setOnClickListener(view -> {
                        dialog.dismiss();
                    });

                    dialog.show();

                });
            }else {
                dvir_recyclerview.setVisibility(View.GONE);
                container1.setVisibility(View.VISIBLE);
            }
        });


        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), s -> {
            Log.d("Adverse Diving",s);

                dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities1 -> {
                    adapter = new DvirlistAdapter(requireContext(),dvirEntities1,s);
                    dvir_recyclerview.setAdapter(adapter);
                    if (adapter.getItemCount() == 0){
                        container1.setVisibility(View.VISIBLE);
                        dvir_recyclerview.setVisibility(View.GONE);
                        idDVIRContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                    }else {
                        container1.setVisibility(View.GONE);
                        idDVIRContainer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        dvir_recyclerview.setVisibility(View.VISIBLE);
                    }
                    adapter.setListener(dvirEntity1 -> {

                        Dialog dialog = new Dialog(requireContext());
                        dialog.setContentView(R.layout.custom_delete_dvir_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        dialog.findViewById(R.id.idDvirYes).setOnClickListener(view ->{
                            dvirViewModel.deleteDvir(dvirEntity1);
                            dvir_recyclerview.setVisibility(View.GONE);
                            container1.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        });

                        dialog.findViewById(R.id.idDvirNo).setOnClickListener(view -> {
                            dialog.dismiss();
                        });

                        dialog.show();

                    });
                });

            idFABAddDvir.setOnClickListener(view ->{
                Intent intent = new Intent(getContext(),AddDvirActivity.class);
                intent.putExtra("currDay",s);
                startActivity(intent);
            });
        });


        return v;
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
//        textView.setText(sendModels.getS());
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}