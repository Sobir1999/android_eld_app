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

    private ArrayList<DvirEntity> dvirEntities = new ArrayList<>();
    private Context ctx;
    private DvirlistAdapterItemClickListener listener;


    public DvirlistAdapter(Context context, List<DvirEntity> dvirEntities1,String currday) {
        ctx = context;
        for (int i = 0; i < dvirEntities1.size() ; i++) {
            if (dvirEntities1.get(i).getDay().equals(currday)){
                dvirEntities.add(dvirEntities1.get(i));
            }
        }

    }

    @NonNull
    @Override
    public DvirlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dvir_list,parent,false);
        return new DvirlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DvirlistViewHolder holder, int position) {
        if (dvirEntities.size() > 0){
            holder.onBind(dvirEntities.get(position));
            holder.bottomArrow.setTag(position);
            holder.arrowUp.setTag(position);
            holder.idParentContainer.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
            return dvirEntities.size();
    }

    public void setListener(DvirlistAdapter.DvirlistAdapterItemClickListener listener) {
        this.listener = listener;
    }

    public class DvirlistViewHolder extends RecyclerView.ViewHolder{

        private TextView vehicleName,time,location,satsfaction,notes,unitDefects,trailerDefects,trailerName,TvTrailer;
        private ImageView bottomArrow,delete,imgSatisfaction,arrowUp;
        private LinearLayout unitsContainer,addDefect,idParentContainer;
        private View view;

        public DvirlistViewHolder(@NonNull View itemView) {
            super(itemView);
            vehicleName = itemView.findViewById(R.id.idTVVehicleName);
            trailerName = itemView.findViewById(R.id.idTVTrailerName);
            TvTrailer = itemView.findViewById(R.id.TvTrailer);
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
            idParentContainer = itemView.findViewById(R.id.idParentContainer);
        }

        void onBind(DvirEntity dvirEntity){

            if (dvirEntity != null){

                bottomArrow.setOnClickListener(v ->{
                    if (bottomArrow.getTag() == idParentContainer.getTag()){
                        idParentContainer.setVisibility(View.VISIBLE);
                        arrowUp.setVisibility(View.VISIBLE);
                        bottomArrow.setVisibility(View.GONE);
                    }
                });

                arrowUp.setOnClickListener(v ->{
                    if (arrowUp.getTag() == idParentContainer.getTag()){
                        idParentContainer.setVisibility(View.GONE);
                        bottomArrow.setVisibility(View.VISIBLE);
                        arrowUp.setVisibility(View.GONE);
                    }
                });

                vehicleName.setText(dvirEntity.getUnit());
                if (dvirEntity.getTrailer().equals("")){
                    trailerName.setVisibility(View.GONE);
                    TvTrailer.setVisibility(View.GONE);
                }else {
                    trailerName.setVisibility(View.VISIBLE);
                    TvTrailer.setVisibility(View.VISIBLE);
                    trailerName.setText(dvirEntity.getTrailer().substring(0,dvirEntity.getTrailer().length()-1));
                }
                time.setText(dvirEntity.getTime());
                location.setText(dvirEntity.getLocation());
                if (dvirEntity.getUnitDefect().equals("") && dvirEntity.getTrailerDefect().equals("")){
                    unitsContainer.setVisibility(View.GONE);
                    addDefect.setVisibility(View.VISIBLE);
                }else {
                    addDefect.setVisibility(View.GONE);
                    unitsContainer.setVisibility(View.VISIBLE);
                    if (!dvirEntity.getTrailerDefect().equals("")){
                        trailerDefects.setText(dvirEntity.getTrailerDefect());
                    }else {
                        trailerDefects.setText("No trailer defects");
                    }

                    if (!dvirEntity.getUnitDefect().equals("")){
                        unitDefects.setText(dvirEntity.getUnitDefect());
                    }else {
                        unitDefects.setText("No unit defects");
                    }
                    notes.setText(dvirEntity.getNote());
                }
                if (dvirEntity.getHasMechanicSignature() || (dvirEntity.getUnitDefect().equals("") && dvirEntity.getTrailerDefect().equals(""))) {
                    satsfaction.setText("Veihcle Satisfactory");
                    satsfaction.setTextColor(Color.parseColor("#2D9B05"));
                    imgSatisfaction.setBackgroundResource(R.drawable.ic_baseline_verified_24);
                } else {
                    satsfaction.setText("Veihcle Unsatisfactory");
                    satsfaction.setTextColor(Color.parseColor("#C10303"));
                    imgSatisfaction.setBackgroundResource(R.drawable.ic_baseline_info_24);
                }

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
