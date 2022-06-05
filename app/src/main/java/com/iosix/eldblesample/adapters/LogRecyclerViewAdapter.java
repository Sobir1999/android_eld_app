package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

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
    private int last_status;
    private String day;
    String time = "" + Calendar.getInstance().getTime();
    String today = time.split(" ")[1] + " " + time.split(" ")[2];

    public LogRecyclerViewAdapter(Context context, List<LogEntity> logEntities,int last_status,String day){
        this.context = context;
        this.logEntities = logEntities;
        this.last_status = last_status;
        this.day = day;
        driverSharedPrefs = DriverSharedPrefs.getInstance(context);
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_items,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogRecyclerViewAdapter.LogViewHolder holder, int position) {
        tv_log_id = holder.itemView.findViewById(R.id.tv_log_id);
        TextView textView = holder.itemView.findViewById(R.id.tv_timer_log);
        tv_log_id.setText(String.valueOf(position+1));

        int time = 0;
        if (day.equals(today)){
            if (position < logEntities.size() - 1){
                time = logEntities.get(position + 1).getSeconds()-logEntities.get(position).getSeconds();

                holder.onBind(logEntities.get(position + 1));
            }else {
                switch (last_status){
                    case EnumsConstants.STATUS_OFF :
                        dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                        dr_text.setText(R.string.off);
                        dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                        break;

                    case EnumsConstants.STATUS_SB :
                        dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusSB));
                        dr_text.setText(R.string.sb);
                        dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusSBBold));
                        break;

                    case EnumsConstants.STATUS_DR :
                        dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusDR));
                        dr_text.setText(R.string.dr);
                        dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusDRBold));
                        break;

                    case EnumsConstants.STATUS_ON :
                        dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusON));
                        dr_text.setText(R.string.on);
                        dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusONBold));
                        break;
                }
                if (logEntities.size() == 0){
                    time = getCurrentSeconds();
                }else {
                    time = getCurrentSeconds() - logEntities.get(position).getSeconds();
                }
            }
        }else {
            if (logEntities.size() == 0){
                last_status = EnumsConstants.STATUS_OFF;
                dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                dr_text.setText(R.string.off);
                dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                time = 86400;

            }else {
                if (position < logEntities.size() - 1){
                    time = logEntities.get(position + 1).getSeconds()-logEntities.get(position).getSeconds();

                    holder.onBind(logEntities.get(position + 1));
                }else {
                    switch (logEntities.get(position).getTo_status()){
                        case EnumsConstants.STATUS_OFF :
                            dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                            dr_text.setText(R.string.off);
                            dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                            break;

                        case EnumsConstants.STATUS_SB :
                            dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusSB));
                            dr_text.setText(R.string.sb);
                            dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusSBBold));
                            break;

                        case EnumsConstants.STATUS_DR :
                            dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusDR));
                            dr_text.setText(R.string.dr);
                            dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusDRBold));
                            break;

                        case EnumsConstants.STATUS_ON :
                            dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusON));
                            dr_text.setText(R.string.on);
                            dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusONBold));
                            break;
                    }
                    if (logEntities.size() == 1){
                        time = 86400;
                    }else {
                        time = 86400 - logEntities.get(position).getSeconds();
                    }
                }
            }
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
        if (logEntities.size() == 0){
            return logEntities.size() + 1;
        }else
        return logEntities.size();
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        public LogViewHolder(@NonNull View itemView){
            super(itemView);
            tv_time_log = itemView.findViewById(R.id.tv_time_log);
            tv_timer_log = itemView.findViewById(R.id.tv_timer_log);
            tv_location_log = itemView.findViewById(R.id.tv_location_log);
            dr_button = itemView.findViewById(R.id.dr_button);
            dr_text = itemView.findViewById(R.id.dr_text);
            idLocationContainer = itemView.findViewById(R.id.idLocationContainer);
            im_more_icon = itemView.findViewById(R.id.idLogBottomArrow);
        }

        void onBind(LogEntity logEntity){
            if (logEntity.getLocation() != null){
                tv_location_log.setText(logEntity.getLocation());
            }else {
                tv_location_log.setText(driverSharedPrefs.getMainOffice());
            }
//            im_more_icon.setOnClickListener(view -> {
//                idLocationContainer.setVisibility(View.VISIBLE);
//            });
//            tv_timer_log.setText(String.format("%s:%s", hour, minut));
            switch (logEntity.getFrom_status()){
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
