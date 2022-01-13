package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;

public class RecapListAdapter extends RecyclerView.Adapter<RecapListAdapter.ViewHolder> {

    Context ctx;
    public RecapListAdapter(Context context){
        ctx = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recap_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 7;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView week,day,time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            week = itemView.findViewById(R.id.idRecapWeek);
            day = itemView.findViewById(R.id.idRecapDay);
            time = itemView.findViewById(R.id.idRecapTime);

        }
    }
}
