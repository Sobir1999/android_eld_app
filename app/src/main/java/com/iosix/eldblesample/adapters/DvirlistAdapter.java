package com.iosix.eldblesample.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import java.util.ArrayList;
import java.util.List;

public class DvirlistAdapter extends RecyclerView.Adapter<DvirlistAdapter.DvirlistViewHolder> {

    private List<DvirEntity> dvirEntities;
    private Context ctx;
    private String s;
    private DvirViewModel dvirViewModel;


    public DvirlistAdapter(Context context, DvirViewModel dvirViewModel,String currday) {
        dvirEntities = new ArrayList<>();
        ctx = context;
        s = currday;
        this.dvirViewModel = dvirViewModel;
        dvirViewModel.getMgetDvirs().observe((LifecycleOwner) ctx, dvirEntities -> {
            DvirlistAdapter.this.dvirEntities.clear();
                    for (int i = 0; i < dvirEntities.size() ; i++) {
                        if (dvirEntities.get(i).getDay().equals(currday)){
                            DvirlistAdapter.this.dvirEntities.add(dvirEntities.get(i));
                        }
                    }
                }
        );
    }

    @NonNull
    @Override
    public DvirlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dvir_list,parent,false);
        return new DvirlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DvirlistViewHolder holder, int position) {
        holder.onBind(dvirViewModel,dvirEntities.get(position));
    }

    @Override
    public int getItemCount() {
        if (dvirEntities != null){
            return dvirEntities.size();
        }else return 0;
    }

    public class DvirlistViewHolder extends RecyclerView.ViewHolder{

        private TextView vehicleName,time,location,satsfaction,notes,unitDefects;
        private ImageView bottomArrow,delete,imgSatisfaction,arrowUp;
        private LinearLayout unitsContainer;
        private View view;

        public DvirlistViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.idTVVehicleName);
            time = itemView.findViewById(R.id.idTvVehicleTime);
            location = itemView.findViewById(R.id.idTvVehicleLocation);
            bottomArrow = itemView.findViewById(R.id.idDVIRBottomArrow);
            arrowUp = itemView.findViewById(R.id.idDVIRArrowUp);
            delete = itemView.findViewById(R.id.idDVIRDelete);
            satsfaction = itemView.findViewById(R.id.idTvVehicleSatisfaction);
            imgSatisfaction = itemView.findViewById(R.id.ImageView);
            notes = itemView.findViewById(R.id.idDVIRNotes);
            unitDefects = itemView.findViewById(R.id.idUnitDefectName);
            view = itemView.findViewById(R.id.idDVIRView);
            unitsContainer = itemView.findViewById(R.id.idUnitsContainer);
        }

        void onBind(DvirViewModel dvirViewModel,DvirEntity dvirEntity){

            if (dvirEntities != null){

                time.setText(dvirEntity.getTime());
                location.setText(dvirEntity.getLocation());
                unitDefects.setText(dvirEntity.getUnitDefect());
                notes.setText(dvirEntity.getNote());
                vehicleName.setText(dvirEntity.getTrailer());
                if (dvirEntity.getHasMechanicSignature()) {
                    satsfaction.setText("Veihcle Satisfactory");
                    satsfaction.setTextColor(Color.parseColor("#2D9B05"));
                    imgSatisfaction.setBackgroundResource(R.drawable.ic_baseline_verified_24);
                } else {
                    satsfaction.setText("Veihcle Unsatisfactory");
                    satsfaction.setTextColor(Color.parseColor("#C10303"));
                    imgSatisfaction.setBackgroundResource(R.drawable.ic_baseline_info_24);
                }
                view.setVisibility(View.GONE);
                unitsContainer.setVisibility(View.GONE);

                bottomArrow.setOnClickListener(v -> {
                    view.setVisibility(View.VISIBLE);
                    unitsContainer.setVisibility(View.VISIBLE);
                    arrowUp.setVisibility(View.VISIBLE);
                    bottomArrow.setVisibility(View.GONE);
                });

                arrowUp.setOnClickListener(v -> {
                    view.setVisibility(View.GONE);
                    unitsContainer.setVisibility(View.GONE);
                    arrowUp.setVisibility(View.GONE);
                    bottomArrow.setVisibility(View.VISIBLE);
                });
                delete.setOnClickListener(v -> {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ctx);
                    alertDialog.setTitle("Delete DVIR?")
                            .setNegativeButton("No", (dialog, which) ->
                                    alertDialog.setCancelable(true))
                            .setPositiveButton("Yes",((dialog, which) -> {
                                dvirViewModel.deleteDvir(dvirEntity);
//                                        container1.setVisibility(View.VISIBLE);
//                                        container2.setVisibility(View.GONE);
                            }));
                    AlertDialog alert = alertDialog.create();
                    alert.show();
                });
            }
        }
    }
}
