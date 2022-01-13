package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.util.List;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.LogViewHolder> {

    private Context context;
    private List<LogEntity> logEntities;
    private TextView tv_log_id,tv_time_log,tv_timer_log,tv_location_log,tv_driving_log,dr_text;
    private ImageView im_more_icon,im_less_icon;
    private CardView dr_button;
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
        holder.onBind(logEntities.get(position));
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
            im_less_icon = itemView.findViewById(R.id.im_less_icon);
            dr_button = itemView.findViewById(R.id.dr_button);
            dr_text = itemView.findViewById(R.id.dr_text);
            idStatusContainer = itemView.findViewById(R.id.idStatusContainer);
            idLocationContainer = itemView.findViewById(R.id.idLocationContainer);
        }

        void onBind(LogEntity logEntity){

            im_more_icon.setOnClickListener(v -> {
                idStatusContainer.setVisibility(View.VISIBLE);
                idLocationContainer.setVisibility(View.VISIBLE);
                im_more_icon.setVisibility(View.GONE);
                im_less_icon.setVisibility(View.VISIBLE);
            });

            im_less_icon.setOnClickListener(v -> {
                idStatusContainer.setVisibility(View.GONE);
                idLocationContainer.setVisibility(View.GONE);
                im_more_icon.setVisibility(View.VISIBLE);
                im_less_icon.setVisibility(View.GONE);
            });

            int t = logEntity.getSeconds();
            int hour = t/3600;
            int minut = (t - hour*3600)/60;
            tv_log_id.setText(String.valueOf(logEntity.getId()));
//            tv_timer_log.setText(String.format("%s:%s", hour, minut));
            switch (logEntity.getTo_status()){
                case EnumsConstants.STATUS_OFF :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                    dr_text.setText("OFF");
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                    break;

                case EnumsConstants.STATUS_SB :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusSB));
                    dr_text.setText("SB");
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusSBBold));
                    break;

                case EnumsConstants.STATUS_DR :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusDR));
                    dr_text.setText("DR");
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusDRBold));
                    break;

                case EnumsConstants.STATUS_ON :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusON));
                    dr_text.setText("ON");
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusONBold));
                    break;
            }
        }
    }
}
