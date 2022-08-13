package com.iosix.eldblesample.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private ArrayList<DvirEntity> dvirEntitiesCurr;

    public static DVIRFragment newInstance() {
        DVIRFragment fragment = new DVIRFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dvirViewModel = ViewModelProviders.of(requireActivity()).get(DvirViewModel.class);
        dvirEntitiesCurr = new ArrayList<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dvirViewModel.getCurrentName().observe(getViewLifecycleOwner(), s -> {
            currDay = s;
            dvirViewModel.getMgetDvirs().observe(getViewLifecycleOwner(),dvirEntities1 -> {
                dvirEntitiesCurr.clear();
                for (int i = 0; i < dvirEntities1.size(); i++) {
                    if (dvirEntities1.get(i).getDay().equals(s)){
                        dvirEntitiesCurr.add(dvirEntities1.get(i));
                    }
                }
                if (dvirEntitiesCurr.size() > 0){
                    adapter = new DvirlistAdapter(requireContext(),dvirEntitiesCurr);
                    dvir_recyclerview.setAdapter(adapter);
                    container1.setVisibility(View.GONE);
                    idDVIRContainer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    dvir_recyclerview.setVisibility(View.VISIBLE);

                    adapter.setListener(dvirEntity1 -> {

                        Dialog dialog = new Dialog(requireContext());
                        dialog.setContentView(R.layout.custom_delete_dvir_dialog);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

                        dialog.findViewById(R.id.idDvirYes).setOnClickListener(view1 ->{
                            dvirViewModel.deleteDvir(dvirEntity1);
                            dvir_recyclerview.setVisibility(View.GONE);
                            container1.setVisibility(View.VISIBLE);
                            dialog.dismiss();
                        });

                        dialog.findViewById(R.id.idDvirNo).setOnClickListener(view2 -> {
                            dialog.dismiss();
                        });

                        dialog.show();

                    });
                }else {
                    container1.setVisibility(View.VISIBLE);
                    dvir_recyclerview.setVisibility(View.GONE);
                    idDVIRContainer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
                }
            });
        });

        idFABAddDvir.setOnClickListener(view3 ->{
            Intent intent = new Intent(getContext(),AddDvirActivity.class);
            intent.putExtra("currDay",currDay);
            startActivity(intent);
            requireActivity().finish();
        });

        createButton.setOnClickListener(view4 ->{
            Intent intent = new Intent(getContext(),AddDvirActivity.class);
            intent.putExtra("currDay",currDay);
            startActivity(intent);
            requireActivity().finish();
        });

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

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requireActivity().getViewModelStore().clear();
        this.getViewModelStore().clear();
    }
}