package com.iosix.eldblesample.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.roomDatabase.entities.DvirEntity;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;
import com.iosix.eldblesample.viewModel.DayDaoViewModel;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RecyclerViewLastAdapter extends RecyclerView.Adapter<RecyclerViewLastAdapter.ViewHolder> {
    private List<DayEntity> dayEntities;
    private List<DvirEntity> dvirEntities;
    private List<DayEntity> selectedDays = new ArrayList<>();
    private List<LogEntity> truckStatusEntities;
    private LastDaysRecyclerViewItemClickListener listener;
    private Context ctx;
    private boolean isSelected = false;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setListener(LastDaysRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public RecyclerViewLastAdapter(Context context, DayDaoViewModel daoViewModel, StatusDaoViewModel statusDaoViewModel, DvirViewModel dvirViewModel) {
        dayEntities = new ArrayList<>();
        dvirEntities = new ArrayList<>();
        truckStatusEntities = new ArrayList<>();
        ctx = context;

        daoViewModel.getMgetAllDays().observe((LifecycleOwner) ctx, dayEntities -> RecyclerViewLastAdapter.this.dayEntities = dayEntities);
        dvirViewModel.getMgetDvirs().observe((LifecycleOwner) ctx, dvirEntities -> RecyclerViewLastAdapter.this.dvirEntities = dvirEntities);

        statusDaoViewModel.getmAllStatus().observe((LifecycleOwner) ctx, truckStatusEntities -> RecyclerViewLastAdapter.this.truckStatusEntities = truckStatusEntities);
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
        return dayEntities.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView day, day_week, no_dvir;
        View clickable;
        CustomStableRulerChart customRulerChart;
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.idTableDay);
            day_week = itemView.findViewById(R.id.idTableMonth);
            clickable = itemView.findViewById(R.id.idCLickableView);
            customRulerChart = itemView.findViewById(R.id.idLastDaysTable);
            imageView = itemView.findViewById(R.id.idTableRadioBtn);
            no_dvir = itemView.findViewById(R.id.idTableDvir);
            itemsClicked();
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        void onBind(DayEntity dayEntity) {

            ArrayList<DvirEntity> dvirEntity = new ArrayList<>();
            day.setText(dayEntity.getDay_name());
            day_week.setText(dayEntity.getDay());
            customRulerChart.setArrayList(getDayTruckEntity(dayEntity.getDay()));

            if (dvirEntities.size() != 0){
                for (int i = 0; i < dvirEntities.size(); i++) {
                    if (dvirEntities.get(i).getDay().equals(dayEntity.getDay()) ){
                        no_dvir.setText("DVIR");
                        dvirEntity.add(dvirEntities.get(i));
                    }
                }
            }

            imageView.setOnClickListener(v -> {
                imageView.setImageResource(imageView.getDrawable().getConstantState().equals(imageView.getContext().getDrawable(R.drawable.state_checked).getConstantState()) ? R.drawable.state_unchacked : R.drawable.state_checked);
                if (listener != null) {
                    listener.onclickItem(dayEntity.getDay());
                }
            });

            no_dvir.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onclickDvir(dayEntity.getDay(),dvirEntity);
                }
            });

            if (isSelected) {
                imageView.setImageResource(R.drawable.state_checked);
            } else {
                imageView.setImageResource(R.drawable.state_unchacked);
            }
        }

        private void itemsClicked() {
            clickable.setOnClickListener(v -> customRulerChart.setVisibility(customRulerChart.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));
        }
    }

    private ArrayList<LogEntity> getDayTruckEntity(String day) {
        ArrayList<LogEntity> entities = new ArrayList<>();
        for (int i = 0; i < truckStatusEntities.size(); i++) {
            if (truckStatusEntities.get(i).getTime().equalsIgnoreCase(day)) {
                entities.add(truckStatusEntities.get(i));
            }
        }
        return entities;
    }

    public interface LastDaysRecyclerViewItemClickListener {
        void onclickItem(String s);

        void onclickDvir(String s,ArrayList<DvirEntity> dvirEntity);
    }

}
