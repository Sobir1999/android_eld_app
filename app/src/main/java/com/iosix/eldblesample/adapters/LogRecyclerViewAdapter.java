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
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.color.MaterialColors;
import com.iosix.eldblesample.R;
import com.iosix.eldblesample.activity.MainActivity;
import com.iosix.eldblesample.enums.EnumsConstants;
import com.iosix.eldblesample.enums.GPSTracker;
import com.iosix.eldblesample.models.Status;

import org.threeten.bp.LocalTime;
import org.threeten.bp.ZonedDateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.ViewHolder> {

    private final Context context;
    private final Status lastActionS;
    private final String day;
    private final List<Status> statuses;
    private final List<Status> actionStatuses = new ArrayList<>();
    LayoutInflater inflater;

    private LogListItemClickListener listener;

    public void setListener(LogListItemClickListener listener){
        this.listener = listener;
    }

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.log_items,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return statuses.size() + 1;
    }
    public interface LogListItemClickListener{
        void onItemClick();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id,status,time,dateTime,location,notes;
        CardView statusBac;
        ImageView idLogBottomArrow;
        ConstraintLayout idLocationContainer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.tv_log_id);
            status = itemView.findViewById(R.id.dr_text);
            time = itemView.findViewById(R.id.tv_timer_log);
            dateTime = itemView.findViewById(R.id.tv_time_log);
            location = itemView.findViewById(R.id.tv_location_log);
            notes = itemView.findViewById(R.id.tv_notes_log);
            statusBac = itemView.findViewById(R.id.dr_button);
            idLogBottomArrow = itemView.findViewById(R.id.idLogBottomArrow);
            idLocationContainer = itemView.findViewById(R.id.idLocationContainer);
        }

        void onBind(int i){
            idLogBottomArrow.setTag(i);
            idLocationContainer.setTag(i);

            itemView.setOnClickListener(view1 -> {
                if (listener != null){
                    listener.onItemClick();
                }
                if (idLocationContainer.getVisibility() == View.GONE){
                    idLocationContainer.setVisibility(View.VISIBLE);
                    idLogBottomArrow.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.ic_baseline_keyboard_arrow_up_24));
                }else {
                    idLocationContainer.setVisibility(View.GONE);
                    idLogBottomArrow.setImageDrawable(AppCompatResources.getDrawable(context,R.drawable.keyboard_bottom_arrow));
                }
            });

            id.setText(format(Locale.getDefault(),"%d", i + 1));
            if (i == 0){
                status.setText(lastActionS.getStatus());
                getStatusColor(lastActionS.getStatus());
                dateTime.setText(getCurrentTime(0));
                if (!lastActionS.getNote().equals("")){
                    notes.setText(lastActionS.getNote());
                }
                location.setText(getLocation());
            }else {
                status.setText(statuses.get(i-1).getStatus());
                getStatusColor(statuses.get(i-1).getStatus());
                dateTime.setText(getCurrentTime(stringToDate(statuses.get(i-1).getTime()).toLocalTime().toSecondOfDay()));
                if (!statuses.get(i-1).getNote().equals("")){
                    notes.setText(statuses.get(i-1).getNote());
                }
                location.setText(getLocation());
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
        }

        private void getStatusColor(String status1){
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
                    status.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.custom_table_time_size_land));
                    status.setTextColor(ContextCompat.getColor(context,R.color.colorLoginBold));
                    statusBac.setCardBackgroundColor(MaterialColors.getColor(context,R.attr.customBgColor, Color.BLACK));
                    break;
                case EnumsConstants.LOGOUT:
                    status.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.custom_table_time_size_land));
                    status.setTextColor(ContextCompat.getColor(context,R.color.colorLogoutBold));
                    statusBac.setCardBackgroundColor(MaterialColors.getColor(context,R.attr.customBgColor, Color.BLACK));
                    break;
                case EnumsConstants.POWER_UP:
                    status.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.custom_table_time_size_land));
                    status.setTextColor(ContextCompat.getColor(context,R.color.colorPowerUpBold));
                    statusBac.setCardBackgroundColor(MaterialColors.getColor(context,R.attr.customBgColor, Color.BLACK));
                    break;
                case EnumsConstants.POWER_DOWN:
                    status.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.custom_table_time_size_land));
                    status.setTextColor(ContextCompat.getColor(context,R.color.colorPowerDownBold));
                    statusBac.setCardBackgroundColor(MaterialColors.getColor(context,R.attr.customBgColor, Color.BLACK));
                    break;
                case EnumsConstants.CERTIFIED:
                    status.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimension(R.dimen.custom_table_time_size_land));
                    status.setTextColor(ContextCompat.getColor(context,R.color.colorCertifiedBold));
                    statusBac.setCardBackgroundColor(MaterialColors.getColor(context,R.attr.customBgColor, Color.BLACK));
                    break;
            }
        }
    }

    private String getLocation(){
        String add = "";
        GPSTracker gpsTracker = new GPSTracker(context);
        double longtitude = gpsTracker.getLongitude();
        double latitude = gpsTracker.getLatitude();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            if (latitude != 0 && longtitude != 0) {
                List<Address> addresses = geocoder.getFromLocation(latitude, longtitude, 1);
                Address obj = addresses.get(0);
                add = obj.getAddressLine(0);

                return add;

            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return e.getMessage();
        }

        return add;
    }
}
