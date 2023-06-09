package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.getDayTime1;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.HOSConstants.mCycle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.customViews.CustomLiveRulerChart;
import com.iosix.eldblesample.customViews.CustomStableRulerChart;
import com.iosix.eldblesample.roomDatabase.entities.DayEntity;
import com.iosix.eldblesample.viewModel.DvirViewModel;
import com.iosix.eldblesample.viewModel.StatusDaoViewModel;

import org.threeten.bp.ZonedDateTime;

import java.util.List;

public class RecyclerViewLastAdapter extends RecyclerView.Adapter<RecyclerViewLastAdapter.MyViewHolder> {
    private final List<DayEntity> dayEntities;
    private final DvirViewModel dvirViewModel;
    private final StatusDaoViewModel statusDaoViewModel;
    private LastDaysRecyclerViewItemClickListener listener;
    private final Context ctx;

    public void setListener(LastDaysRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    public RecyclerViewLastAdapter(Context context, List<DayEntity> dayEntities, DvirViewModel dvirViewModel, StatusDaoViewModel statusDaoViewModel) {
        this.dayEntities = dayEntities;
        this.dvirViewModel = dvirViewModel;
        this.statusDaoViewModel = statusDaoViewModel;
        ctx = context;
        mCycle = 0;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.last_days_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.onBind(dayEntities.get(position+1),statusDaoViewModel,dvirViewModel,ctx,listener);
    }
    @Override
    public int getItemCount() {
        return dayEntities.size()-1;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private final TextView workedTime;
        TextView day, day_week;
        View clickable;
        CustomStableRulerChart customRulerChart;
        CustomLiveRulerChart customLiveRulerChart;
        ImageButton imageView,idTableSignature;
        Button no_dvir;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.idTableDay);
            day_week = itemView.findViewById(R.id.idTableMonth);
            clickable = itemView.findViewById(R.id.idCLickableView);
            customRulerChart = itemView.findViewById(R.id.idLastDaysTable);
            no_dvir = itemView.findViewById(R.id.idTableDvir);
            idTableSignature = itemView.findViewById(R.id.idTableSignature);
            workedTime = itemView.findViewById(R.id.idTableTime);
        }

        void onBind(DayEntity dayEntity,StatusDaoViewModel statusDaoViewModel,DvirViewModel dvirViewModel,Context ctx,LastDaysRecyclerViewItemClickListener listener){
            day.setText(getDayTime1(dayEntity.getDay()));

            statusDaoViewModel.getAllDrivingStatusTime(dayEntity.getDay(), n -> workedTime.setText(intToTime(n)));

            statusDaoViewModel.getCurDateStatus(customLiveRulerChart, customRulerChart, dayEntity.getDay());

            clickable.setOnClickListener(v -> customRulerChart.setVisibility(customRulerChart.getVisibility() == View.GONE ? View.VISIBLE : View.GONE));

            idTableSignature.setOnClickListener(view -> {
                if (listener != null){
                    listener.onclickSignature(dayEntity.getDay());
                }
            });

            dvirViewModel.getDvirCurr(ctx,dayEntity.getDay(),no_dvir);

            no_dvir.setOnClickListener(view -> dvirViewModel.getCurDateDvirs(ctx,dayEntity.getDay(),no_dvir,listener));
        }
    }

    public interface LastDaysRecyclerViewItemClickListener {
        void onclickItem(String s);

        void onclickDvir(String s,boolean a);

        void onclickSignature(String s);
    }

}
