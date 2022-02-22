package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecapListAdapter extends RecyclerView.Adapter<RecapListAdapter.ViewHolder> {

    private Context ctx;
    private List<DayEntity> dayEntities;

    public RecapListAdapter(Context context, DayDaoViewModel daoViewModel){
        ctx = context;
        dayEntities = new ArrayList<>();

        daoViewModel.getMgetAllDays().observe((LifecycleOwner) ctx, dayEntities1 -> {
            for (int i = dayEntities1.size()-1; i > 7; i--) {
                dayEntities.add(dayEntities1.get(i));
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recap_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(dayEntities.get(position));
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView week,day,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            week = itemView.findViewById(R.id.idRecapWeek);
            day = itemView.findViewById(R.id.idRecapDay);
            time = itemView.findViewById(R.id.idRecapTime);

        }

        void onBind(DayEntity dayEntity){
            week.setText(dayEntity.getDay_name());
            day.setText(dayEntity.getDay());
        }
    }
}
