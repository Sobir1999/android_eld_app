package com.iosix.eldblesample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.models.Dvir;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DvirlistAdapter extends BaseAdapter {

    private ArrayList<Dvir> dvirEntities = new ArrayList<>();
    private Context ctx;
    private DvirlistAdapterItemClickListener listener;


    public DvirlistAdapter(Context context, List<Dvir> dvirEntities1) {
        ctx = context;
        dvirEntities.addAll(dvirEntities1);
    }

    public void setListener(DvirlistAdapter.DvirlistAdapterItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return dvirEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(ctx).inflate(R.layout.dvir_list,viewGroup,false);
        TextView vehicleName = view.findViewById(R.id.idTVVehicleName);
        TextView trailerName = view.findViewById(R.id.idTVTrailerName);
        TextView tvTrailer = view.findViewById(R.id.TvTrailer);
        TextView time = view.findViewById(R.id.idTvVehicleTime);
        TextView location = view.findViewById(R.id.idTvVehicleLocation);
        ImageView delete = view.findViewById(R.id.idDVIRDelete);
        TextView satsfaction = view.findViewById(R.id.idTvVehicleSatisfaction);
        ImageView imgSatisfaction = view.findViewById(R.id.ImageView);
        TextView notes = view.findViewById(R.id.idDVIRNotes);
        TextView unitDefects = view.findViewById(R.id.idUnitDefectName);
        View myView = view.findViewById(R.id.idDVIRView);
        LinearLayout unitsContainer = view.findViewById(R.id.idUnitsContainer);
        LinearLayout addDefect = view.findViewById(R.id.addDefect);
        LinearLayout idParentContainer = view.findViewById(R.id.idParentContainer);

        if (Objects.equals(dvirEntities.get(i).getUnit(), "")){
            vehicleName.setText("No Unit Selected");
        }else {
            vehicleName.setText(dvirEntities.get(i).getUnit());
        }
        time.setText(dvirEntities.get(i).getTime());
        location.setText(dvirEntities.get(i).getPoint());

        if (dvirEntities.get(i).getDefects().size()>0){
            unitsContainer.setVisibility(View.VISIBLE);
            addDefect.setVisibility(View.GONE);
            for (String defect: dvirEntities.get(i).getDefects()) {
                unitDefects.append(defect + "\n");
            }
        }else {
            unitsContainer.setVisibility(View.GONE);
            addDefect.setVisibility(View.VISIBLE);
        }

        notes.setText(dvirEntities.get(i).getNote());

        if (dvirEntities.get(i).isSatisfy()) {
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
                listener.onclickDelete(dvirEntities.get(i));
            }

        });

        return view;
    }

    public interface DvirlistAdapterItemClickListener {
        void onclickDelete(Dvir dvirEntity);

//        void onclickDvir(String s,List<DvirEntity> dvirEntity);
    }
}
