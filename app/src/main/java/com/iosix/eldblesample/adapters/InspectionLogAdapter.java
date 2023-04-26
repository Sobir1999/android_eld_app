package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.Day.getCurrentMillis;
import static com.iosix.eldblesample.enums.Day.getCurrentTime;
import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.Day.stringToDate;
import static com.iosix.eldblesample.utils.Utils.getStatus;
import static com.iosix.eldblesample.utils.Utils.pointToString;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.Status;

import org.threeten.bp.ZonedDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class InspectionLogAdapter extends BaseAdapter {

    private final Context context;
    private final Status lastActionS;
    private final String day;
    private final List<Status> statuses;
    private final List<Status> actionStatuses = new ArrayList<>();
    LayoutInflater inflater;

    TextView logNumber,dr_text,logStartTime,logDuration,logLocation,logNotes;
    CardView dr_button;

    public InspectionLogAdapter(Context context,List<Status> statuses,Status lastActionS, String day){
        this.context = context;
        this.statuses = statuses;
        this.lastActionS = lastActionS;
        this.day = day;
        inflater = LayoutInflater.from(context);
        for (Status status:
                statuses) {
            if (getStatus(status.getStatus()) < 6){
                actionStatuses.add(status);
            }
        }
    }

    @Override
    public int getCount() {
        return statuses.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.inspection_log_item,viewGroup,false);

        logNumber = view.findViewById(R.id.idLogNumber);
        dr_button = view.findViewById(R.id.dr_button);
        dr_text = view.findViewById(R.id.dr_text);
        logStartTime = view.findViewById(R.id.idLogStartTime);
        logDuration = view.findViewById(R.id.idLogDuration);
        logLocation = view.findViewById(R.id.idLogLocation);
        logNotes = view.findViewById(R.id.idLogNotes);

        logNumber.setText(format(Locale.getDefault(),"%d", i + 1));
        if (i == 0){
            dr_text.setText(lastActionS.getStatus());
            getStatusColor(lastActionS.getStatus(),dr_text,dr_button);
            logStartTime.setText(getCurrentTime(0));
            logNotes.setText(lastActionS.getNote());
        }else {
            dr_text.setText(statuses.get(i-1).getStatus());
            logNotes.setText(statuses.get(i-1).getNote());
            getStatusColor(statuses.get(i-1).getStatus(),dr_text,dr_button);
            logStartTime.setText(getCurrentTime(stringToDate(statuses.get(i-1).getTime()).toLocalTime().toSecondOfDay()));
        }
        if (i == 0){
            if (actionStatuses.size() > 0){
                logDuration.setText(intToTime(stringToDate(actionStatuses.get(0).getTime()).toLocalTime().toSecondOfDay()));
            }else {
                if (day.equals(getDayFormat(ZonedDateTime.now()))){
                    logDuration.setText(intToTime(getCurrentMillis()));
                }else {
                    logDuration.setText(intToTime(86400));
                }
            }
        }else {
            if (getStatus(statuses.get(i-1).getStatus()) < 6){
                for (int j = 0; j < actionStatuses.size(); j++) {
                    if (statuses.get(i-1).getTime().equals(actionStatuses.get(j).getTime())){
                        if (j < actionStatuses.size()-1){
                            logDuration.setText(intToTime(stringToDate(actionStatuses.get(j+1).getTime()).toLocalTime().toSecondOfDay() - stringToDate(actionStatuses.get(j).getTime()).toLocalTime().toSecondOfDay()));
                        }else {
                            if (day.equals(getDayFormat(ZonedDateTime.now()))){
                                logDuration.setText(intToTime(getCurrentMillis()-stringToDate(actionStatuses.get(actionStatuses.size()-1).getTime()).toLocalTime().toSecondOfDay()));
                            }else {
                                logDuration.setText(intToTime(86400 - stringToDate(actionStatuses.get(actionStatuses.size()-1).getTime()).toLocalTime().toSecondOfDay()));
                            }
                        }
                    }
                }
            }else {
                logDuration.setText(intToTime(0));
            }
        }
        return view;
    }

    private void getStatusColor(String status1,TextView status,CardView statusBac){
        switch (status1){
            case EnumsConstants.STATUS_OFF:
            case EnumsConstants.STATUS_OF_PC:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorStatusOFFBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusOFF));
                break;
            case EnumsConstants.STATUS_SB:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorStatusSBBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusSB));
                break;
            case EnumsConstants.STATUS_DR:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorStatusDRBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusDR));
                break;
            case EnumsConstants.STATUS_ON:
            case EnumsConstants.STATUS_ON_YM:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorStatusONBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorStatusON));
                break;
            case EnumsConstants.LOGIN:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorLoginBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorLogin));
                break;
            case EnumsConstants.LOGOUT:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorLogoutBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorLogout));
                break;
            case EnumsConstants.POWER_UP:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorPowerUpBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPowerUp));
                break;
            case EnumsConstants.POWER_DOWN:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorPowerDownBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPowerDown));
                break;
            case EnumsConstants.CERTIFIED:
                status.setTextColor(ContextCompat.getColor(context,R.color.colorCertifiedBold));
                statusBac.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorCertified));
                break;
        }
    }
}
