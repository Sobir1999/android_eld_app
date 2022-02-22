package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.os.Handler;
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
import com.iosix.eldblesample.shared_prefs.DriverSharedPrefs;

import java.util.Calendar;
import java.util.List;
import java.util.logging.LogRecord;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.LogViewHolder> {

    private Context context;
    private List<LogEntity> logEntities;
    private TextView tv_log_id,tv_time_log,tv_timer_log,tv_location_log,tv_driving_log,dr_text;
    private ImageView im_more_icon,im_less_icon;
    private CardView dr_button;
    private ConstraintLayout idStatusContainer;
    private ConstraintLayout idLocationContainer;
    private DriverSharedPrefs driverSharedPrefs;

    public LogRecyclerViewAdapter(Context context, List<LogEntity> logEntities){
        this.context = context;
        this.logEntities = logEntities;
        driverSharedPrefs = new DriverSharedPrefs(context);
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
        TextView textView = holder.itemView.findViewById(R.id.tv_timer_log);
        int time = 0;
        if (position < logEntities.size()-1){
        time = logEntities.get(position+1).getSeconds()-logEntities.get(position).getSeconds();
        }else {
            time = getCurrentSeconds() - logEntities.get(position).getSeconds();
        }
        int hour = time/3600;
        int minut = (time - hour*3600)/60;
        if (hour<10){
            if (minut < 10){
                textView.setText(String.format("0%sh 0%sm",hour,minut));
            }else {
                textView.setText(String.format("0%sh %sm",hour,minut));
            }
        }else {
            if (minut < 10){
                textView.setText(String.format("%sh 0%sm",hour,minut));
            }else {
                textView.setText(String.format("%sh %sm",hour,minut));
            }
        }
    }

    @Override
    public int getItemCount() {
        return logEntities.size();
    }
    @SuppressWarnings("deprecation")
    private int getCurrentSeconds() {
        int hour = Calendar.getInstance().getTime().getHours();
        int minute = Calendar.getInstance().getTime().getMinutes();
        int second = Calendar.getInstance().getTime().getSeconds();
        return hour * 3600 + minute * 60 + second;
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        public LogViewHolder(@NonNull View itemView){
            super(itemView);
            tv_log_id = itemView.findViewById(R.id.tv_log_id);
            tv_time_log = itemView.findViewById(R.id.tv_time_log);
            tv_timer_log = itemView.findViewById(R.id.tv_timer_log);
            tv_location_log = itemView.findViewById(R.id.tv_location_log);
            dr_button = itemView.findViewById(R.id.dr_button);
            dr_text = itemView.findViewById(R.id.dr_text);
            idLocationContainer = itemView.findViewById(R.id.idLocationContainer);
        }

        void onBind(LogEntity logEntity){

            int t = logEntity.getSeconds();
            int hour = t/3600;
            int minut = (t - hour*3600)/60;
            if (logEntity.getLocation() != null){
                tv_location_log.setText(logEntity.getLocation());
            }else {
                tv_location_log.setText(driverSharedPrefs.getMainOffice());
            }
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
