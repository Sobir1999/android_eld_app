package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.Day.getCurrentSeconds;
import static com.iosix.eldblesample.enums.Day.getCurrentTime;
import static com.iosix.eldblesample.utils.Utils.pointToString;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.roomDatabase.entities.LogEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InspectionLogAdapter extends RecyclerView.Adapter<InspectionLogAdapter.InspectionLogViewHolder> {

    private final ArrayList<LogEntity> logEntitiesCurr = new ArrayList<>();
    private final ArrayList<LogEntity> logEntities = new ArrayList<>();
    private final Context context;
    private final String day;
    String time = "" + Calendar.getInstance().getTime();
    String today = time.split(" ")[1] + " " + time.split(" ")[2];

    TextView logNumber,dr_text,logStartTime,logDuration,logLocation,logNotes;
    CardView dr_button;

    public InspectionLogAdapter(Context context, List<LogEntity> arrayList, String day){
        this.context = context;
        this.logEntities.addAll(arrayList);
        logEntitiesCurr.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getTime().equals(day)){
                logEntitiesCurr.add(arrayList.get(i));
            }
        }
        this.day = day;
    }

    @NonNull
    @Override
    public InspectionLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.inspection_log_item,parent,false);
        return new InspectionLogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionLogViewHolder holder, int position) {
        logNumber.setText(String.valueOf(position+1));
        int time = 0;
        if (day.equals(today)){
            if (logEntitiesCurr.size() > 0){
                if (position == logEntitiesCurr.size()-1){
                    if (logEntitiesCurr.get(position).getTo_status() <= 5) {
                        time = getCurrentSeconds() - logEntitiesCurr.get(position).getSeconds();
                    }
                }else {
                    if (logEntitiesCurr.get(position).getTo_status() <= 5) {
                        for (int i = position + 1; i < logEntitiesCurr.size(); i++) {
                            if (logEntitiesCurr.get(i).getTo_status() < 6){
                                time = logEntitiesCurr.get(i).getSeconds() - logEntitiesCurr.get(position).getSeconds();
                                break;
                            }if (i == logEntitiesCurr.size() - 1){
                                time = getCurrentSeconds() - logEntitiesCurr.get(position).getSeconds();
                                break;
                            }
                        }
                    }
                }
            }else {
                time = 86400;
            }
        }else {
            if (logEntitiesCurr.size() > 0){
                if (position == logEntitiesCurr.size()-1){
                    if (logEntitiesCurr.get(position).getTo_status() <= 5) {
                        time = getCurrentSeconds() - logEntitiesCurr.get(position).getSeconds();
                    }
                }else {
                    if (logEntitiesCurr.get(position).getTo_status() <= 5) {
                        for (int i = position + 1; i < logEntitiesCurr.size(); i++) {
                            if (logEntitiesCurr.get(i).getTo_status() < 6){
                                time = logEntitiesCurr.get(i).getSeconds() - logEntitiesCurr.get(position).getSeconds();
                                break;
                            }if (i == logEntitiesCurr.size() - 1){
                                time = 86400 - logEntitiesCurr.get(position).getSeconds();
                                break;
                            }
                        }
                    }
                }
            }else {
                time = 86400;
            }
        }


        int hour = time/3600;
        int minut = (time - hour*3600)/60;
        if (hour<10){
            if (minut < 10){
                logDuration.setText(String.format("0%sh 0%sm",hour,minut));
            }else {
                logDuration.setText(String.format("0%sh %sm",hour,minut));
            }
        }else {
            if (minut < 10){
                logDuration.setText(String.format("%sh 0%sm",hour,minut));
            }else {
                logDuration.setText(String.format("%sh %sm",hour,minut));
            }
        }
        try {
            if (logEntitiesCurr.size() > 0){
                holder.onBind(logEntitiesCurr.get(position));
            }else {
                holder.onBind(new LogEntity(logEntities.get(0).getDriverId(),EnumsConstants.STATUS_OFF,null,null,null,day,0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if (logEntitiesCurr.size() == 0){
            return 1;
        }else
            return logEntitiesCurr.size();
    }

    public class InspectionLogViewHolder extends RecyclerView.ViewHolder{

        public InspectionLogViewHolder(@NonNull View itemView) {
            super(itemView);
            logNumber = itemView.findViewById(R.id.idLogNumber);
            dr_button = itemView.findViewById(R.id.dr_button);
            dr_text = itemView.findViewById(R.id.dr_text);
            logStartTime = itemView.findViewById(R.id.idLogStartTime);
            logDuration = itemView.findViewById(R.id.idLogDuration);
            logLocation = itemView.findViewById(R.id.idLogLocation);
            logNotes = itemView.findViewById(R.id.idLogNotes);
        }

        public void onBind(LogEntity logEntity) throws IOException {
            logStartTime.setText(getCurrentTime(logEntity.getSeconds()));
            if (logEntity.getLocation() != null){
                logLocation.setText(pointToString(context,logEntity.getLocation()));
            }else {
                logLocation.setText("");
            }
            logNotes.setText(logEntity.getNote());
            switch (logEntity.getTo_status()){
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

                case EnumsConstants.STATUS_OF_PC :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                    dr_text.setText(R.string.off_pc);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                    break;

                case EnumsConstants.STATUS_ON_YM :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusON));
                    dr_text.setText(R.string.on_ym);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorStatusONBold));
                    break;

                case EnumsConstants.POWER_UP :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPowerUp));
                    dr_text.setText(R.string.power_up);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorPowerUpBold));
                    break;

                case EnumsConstants.POWER_DOWN :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPowerDown));
                    dr_text.setText(R.string.power_down);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorPowerDownBold));
                    break;

                case EnumsConstants.LOGIN :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorLogin));
                    dr_text.setText(R.string.login);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorLoginBold));
                    break;

                case EnumsConstants.LOGOUT :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorLogout));
                    dr_text.setText(R.string.logout);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorLogoutBold));
                    break;

                case EnumsConstants.CERTIFIED :
                    dr_button.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorCertified));
                    dr_text.setText(R.string.certified);
                    dr_text.setTextColor(ContextCompat.getColor(context,R.color.colorCertifiedBold));
                    break;
            }
        }
    }
}
