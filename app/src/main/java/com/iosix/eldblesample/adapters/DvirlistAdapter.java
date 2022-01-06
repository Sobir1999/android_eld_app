package com.iosix.eldblesample.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import com.iosix.eldblesample.fragments.DVIRFragment;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.viewModel.DvirViewModel;

import java.util.ArrayList;
import java.util.List;

public class DvirlistAdapter extends RecyclerView.Adapter<DvirlistAdapter.DvirlistViewHolder> {

    private List<DvirEntity> dvirEntities;
    private Context ctx;
    private DvirViewModel dvirViewModel;
    private DvirlistAdapterItemClickListener listener;


    public DvirlistAdapter(Context context, DvirViewModel dvirViewModel,String currday) {
        this.dvirViewModel = dvirViewModel;
        dvirEntities = new ArrayList<>();
        ctx = context;
        dvirViewModel.getMgetDvirs().observe((LifecycleOwner) ctx, dvirEntities1 -> {
            dvirEntities.clear();
            for (int i = 0; i < dvirEntities1.size() ; i++) {
                if (dvirEntities1.get(i).getDay().equals(currday)){
                    dvirEntities.add(dvirEntities1.get(i));
                }
            }
        });
    }

    @NonNull
    @Override
    public DvirlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dvir_list,parent,false);
        return new DvirlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DvirlistViewHolder holder, int position) {
        Log.d("Veihcle Satisfactory"," " + dvirEntities.size());
        if (dvirEntities.size() > 0){
            holder.onBind(dvirEntities.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (dvirEntities != null){
            return dvirEntities.size();
        }else return 0;
    }

    public void setListener(DvirlistAdapter.DvirlistAdapterItemClickListener listener) {
        this.listener = listener;
    }

    public class DvirlistViewHolder extends RecyclerView.ViewHolder{

        private TextView vehicleName,time,location,satsfaction,notes,unitDefects,trailerDefects;
        private ImageView bottomArrow,delete,imgSatisfaction,arrowUp;
        private LinearLayout unitsContainer,addDefect;
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
            trailerDefects = itemView.findViewById(R.id.idTrailerDefect);
            addDefect = itemView.findViewById(R.id.addDefect);
        }

        void onBind(DvirEntity dvirEntity){

            if (dvirEntity != null){

                time.setText(dvirEntity.getTime());
                location.setText(dvirEntity.getLocation());
                if (dvirEntity.getUnitDefect().equals("No unit selected") && dvirEntity.getTrailerDefect().equals("No trailer selected")){
                    unitsContainer.setVisibility(View.GONE);
                    addDefect.setVisibility(View.GONE);
                    bottomArrow.setOnClickListener(v -> {
                        view.setVisibility(View.VISIBLE);
                        addDefect.setVisibility(View.VISIBLE);
                        arrowUp.setVisibility(View.VISIBLE);
                        bottomArrow.setVisibility(View.GONE);
                    });

                    arrowUp.setOnClickListener(v -> {
                        view.setVisibility(View.GONE);
                        addDefect.setVisibility(View.GONE);
                        arrowUp.setVisibility(View.GONE);
                        bottomArrow.setVisibility(View.VISIBLE);
                    });
                }else {
                    addDefect.setVisibility(View.GONE);
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
                    if(dvirEntity.getUnitDefect().equals("No unit selected")){
                        trailerDefects.setText(dvirEntity.getTrailerDefect());
                        unitDefects.setText("No unit defects");
                        notes.setText(dvirEntity.getNote());
                    }else if(dvirEntity.getTrailerDefect().equals("No trailer selected")){
                        unitDefects.setText(dvirEntity.getUnitDefect());
                        notes.setText(dvirEntity.getNote());
                        trailerDefects.setText("No trailer defects");
                    }else{
                        trailerDefects.setText(dvirEntity.getTrailerDefect());
                        unitDefects.setText(dvirEntity.getUnitDefect());
                        notes.setText(dvirEntity.getNote());
                    }
                }
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


                delete.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onclickDelete(dvirEntity);
                    }

                });
            }
        }
    }

    public interface DvirlistAdapterItemClickListener {
        void onclickDelete(DvirEntity dvirEntity);

//        void onclickDvir(String s,List<DvirEntity> dvirEntity);
    }
}
