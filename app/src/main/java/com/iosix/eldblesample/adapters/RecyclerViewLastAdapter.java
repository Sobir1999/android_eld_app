package com.iosix.eldblesample.adapters;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewLastAdapter extends RecyclerView.Adapter<RecyclerViewLastAdapter.ViewHolder> {
    private List<DayEntity> dayEntities = new ArrayList<>();
    private Context context;
    private DayDaoViewModel daoViewModel;

    public RecyclerViewLastAdapter(Context context) {
        this.context = context;
        daoViewModel = new DayDaoViewModel((Application) context.getApplicationContext());

        daoViewModel = ViewModelProviders.of((FragmentActivity) context).get(DayDaoViewModel.class);
        daoViewModel.getMgetAllDays().observe((LifecycleOwner) context, new Observer<List<DayEntity>>() {
            @Override
            public void onChanged(List<DayEntity> dayEntities) {
                RecyclerViewLastAdapter.this.dayEntities = dayEntities;
            }
        });
    }

    public void setDayEntities(List<DayEntity> dayEntities) {
        this.dayEntities = dayEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.last_days_recycler_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(dayEntities.get(position));
    }

    @Override
    public int getItemCount() {
        Log.d("DATA", "getItemCount: " + dayEntities.size());
        return dayEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, day_week;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.idTableDay);
            day_week = itemView.findViewById(R.id.idTableMonth);
        }

        void onBind(DayEntity dayEntity) {
            day.setText(dayEntity.getDay_name());
            day_week.setText(dayEntity.getDay());
        }
    }
}
