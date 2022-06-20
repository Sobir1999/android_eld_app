package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.LogViewHolder> {

    private final Context context;
    private final List<LogEntity> logEntities;
    private final ArrayList<LogEntity> logEntitiesCurr = new ArrayList<>();
    private TextView tv_log_id,tv_time_log,tv_timer_log,tv_location_log,tv_driving_log,dr_text;
    private ImageView im_more_icon,im_less_icon;
    private CardView dr_button;
    private ConstraintLayout idStatusContainer;
    private ConstraintLayout idLocationContainer;
    private DriverSharedPrefs driverSharedPrefs;
    private final String day;
    String time = "" + Calendar.getInstance().getTime();
    String today = time.split(" ")[1] + " " + time.split(" ")[2];

    public LogRecyclerViewAdapter(Context context, List<LogEntity> logEntities,String day){
        this.context = context;
        this.logEntities = logEntities;
        logEntitiesCurr.clear();
        for (int i = 0; i < logEntities.size(); i++) {
            if (logEntities.get(i).getTime().equals(day)){
                logEntitiesCurr.add(logEntities.get(i));
            }
            Log.d("Adverse Diving",logEntitiesCurr.size() + "");
            Log.d("Adverse Diving",day + "11");
        }
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
            if (position < logEntitiesCurr.size() - 1){
                time = logEntitiesCurr.get(position + 1).getSeconds()-logEntitiesCurr.get(position).getSeconds();

                holder.onBind(logEntitiesCurr.get(position + 1));
            }else {
                switch (logEntitiesCurr.get(logEntitiesCurr.size()-1).getTo_status()){
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
                if (logEntitiesCurr.size() == 0){
                    time = getCurrentSeconds();
                }else {
                    time = getCurrentSeconds() - logEntities.get(position).getSeconds();
                }
            }
        }else {
            if (logEntitiesCurr.size() == 0){
                dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                dr_text.setText(R.string.off);
                dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                time = 86400;

            }else {
                if (position < logEntitiesCurr.size() - 1){
                    time = logEntitiesCurr.get(position + 1).getSeconds()-logEntitiesCurr.get(position).getSeconds();

                    holder.onBind(logEntitiesCurr.get(position + 1));
                }else {
                    switch (logEntitiesCurr.get(position).getTo_status()){
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
                    if (logEntitiesCurr.size() == 1){
                        time = 86400;
                    }else {
                        time = 86400 - logEntitiesCurr.get(position).getSeconds();
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
        if (logEntitiesCurr.size() == 0){
            return 1;
        }else
        return logEntitiesCurr.size();
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
