package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.Day.getCurrentMillis;
import static com.iosix.eldblesample.enums.Day.getCurrentTime;
import static com.iosix.eldblesample.enums.Day.getDayFormat;
import static com.iosix.eldblesample.enums.Day.intToTime;
import static com.iosix.eldblesample.enums.Day.stringToDate;
import static com.iosix.eldblesample.utils.Utils.getStatus;
import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.models.Status;

import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LogRecyclerViewAdapter extends BaseAdapter {

    private final Context context;
    private final Status lastActionS;
    private final String day;
    private final List<Status> statuses;
    private final List<Status> actionStatuses = new ArrayList<>();
    LayoutInflater inflater;

    public LogRecyclerViewAdapter(Context context,List<Status> statuses,Status lastActionS, String day){
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
        view = inflater.inflate(R.layout.log_items,viewGroup,false);
        TextView id = view.findViewById(R.id.tv_log_id);
        TextView status = view.findViewById(R.id.dr_text);
        TextView time = view.findViewById(R.id.tv_timer_log);
        TextView dateTime = view.findViewById(R.id.tv_time_log);
        CardView statusBac = view.findViewById(R.id.dr_button);

        id.setText(format(Locale.getDefault(),"%d", i + 1));
        if (i == 0){
            status.setText(lastActionS.getStatus());
            getStatusColor(lastActionS.getStatus(),status,statusBac);
            dateTime.setText(getCurrentTime(0));
        }else {
            status.setText(statuses.get(i-1).getStatus());
            getStatusColor(statuses.get(i-1).getStatus(),status,statusBac);
            dateTime.setText(getCurrentTime(stringToDate(statuses.get(i-1).getTime()).toLocalTime().toSecondOfDay()));
        }
        if (i == 0){
            if (actionStatuses.size() > 0){
                time.setText(intToTime(stringToDate(actionStatuses.get(0).getTime()).toLocalTime().toSecondOfDay()));
            }else {
                if (day.equals(getDayFormat(ZonedDateTime.now()))){
                    time.setText(intToTime(getCurrentMillis()));
                }else {
                    time.setText(intToTime(86400));
                }
            }
        }else {
            if (getStatus(statuses.get(i-1).getStatus()) < 6){
                for (int j = 0; j < actionStatuses.size(); j++) {
                    if (statuses.get(i-1).getTime().equals(actionStatuses.get(j).getTime())){
                        if (j < actionStatuses.size()-1){
                            time.setText(intToTime(stringToDate(actionStatuses.get(j+1).getTime()).toLocalTime().toSecondOfDay() - stringToDate(actionStatuses.get(j).getTime()).toLocalTime().toSecondOfDay()));
                        }else {
                            if (day.equals(getDayFormat(ZonedDateTime.now()))){
                                time.setText(intToTime(getCurrentMillis()-stringToDate(actionStatuses.get(actionStatuses.size()-1).getTime()).toLocalTime().toSecondOfDay()));
                            }else {
                                time.setText(intToTime(86400 - stringToDate(actionStatuses.get(actionStatuses.size()-1).getTime()).toLocalTime().toSecondOfDay()));
                            }
                        }
                    }
                }
            }else {
                time.setText(intToTime(0));
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
