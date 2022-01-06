package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.List;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.LogViewHolder> {

    private Context context;
    private List<LogEntity> logEntities;
    private TextView tv_log_id,tv_time_log,tv_timer_log,tv_location_log,tv_driving_log;
    private ImageView im_more_icon;
    private Button dr_button;
    private ConstraintLayout idStatusContainer;
    private ConstraintLayout idLocationContainer;

    public LogRecyclerViewAdapter(Context context, List<LogEntity> logEntities){
        this.context = context;
        this.logEntities = logEntities;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_items,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogRecyclerViewAdapter.LogViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return logEntities.size();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        public LogViewHolder(@NonNull View itemView){
            super(itemView);
            tv_log_id = itemView.findViewById(R.id.tv_log_id);
            tv_time_log = itemView.findViewById(R.id.tv_time_log);
            tv_timer_log = itemView.findViewById(R.id.tv_timer_log);
            tv_location_log = itemView.findViewById(R.id.tv_location_log);
            tv_driving_log = itemView.findViewById(R.id.tv_driving_log);
            im_more_icon = itemView.findViewById(R.id.im_more_icon);
            dr_button = itemView.findViewById(R.id.dr_button);
            idStatusContainer = itemView.findViewById(R.id.idStatusContainer);
            idLocationContainer = itemView.findViewById(R.id.idLocationContainer);
        }

        void onBind(int pos){

            if (im_more_icon.getVisibility() == View.VISIBLE){
                idStatusContainer.setVisibility(View.GONE);
                idLocationContainer.setVisibility(View.GONE);
                im_more_icon.setOnClickListener(v -> {
                    idStatusContainer.setVisibility(View.VISIBLE);
                    idLocationContainer.setVisibility(View.VISIBLE);
                    im_more_icon.setVisibility(View.GONE);
                });
            }

            int t = logEntities.get(pos).getSeconds();
            int hour = t/3600;
            int minut = (t - hour*3600)/60;
            tv_log_id.setText(String.valueOf(pos+1));
            tv_timer_log.setText(String.format("%s:%s", String.valueOf(hour), String.valueOf(minut)));
        }
    }
}
