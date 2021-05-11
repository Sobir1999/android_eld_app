package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.customViews.CustomRulerChart;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewLastAdapter extends RecyclerView.Adapter<RecyclerViewLastAdapter.ViewHolder> {
    private List<DayEntity> dayEntities;

    public RecyclerViewLastAdapter(Context context, DayDaoViewModel daoViewModel) {
        dayEntities = new ArrayList<>();

        daoViewModel.getMgetAllDays().observe((LifecycleOwner) context, new Observer<List<DayEntity>>() {
            @Override
            public void onChanged(List<DayEntity> dayEntities) {
                RecyclerViewLastAdapter.this.dayEntities = dayEntities;
            }
        });
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
        return dayEntities.size() - 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, day_week;
        View clickable;
        CustomRulerChart customRulerChart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.idTableDay);
            day_week = itemView.findViewById(R.id.idTableMonth);
            clickable = itemView.findViewById(R.id.idCLickableView);
            customRulerChart = itemView.findViewById(R.id.idLastDaysTable);
            itemsClicked();
        }

        void onBind(DayEntity dayEntity) {
            day.setText(dayEntity.getDay_name());
            day_week.setText(dayEntity.getDay());
        }

        private void itemsClicked() {
            clickable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customRulerChart.setVisibility(customRulerChart.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}
