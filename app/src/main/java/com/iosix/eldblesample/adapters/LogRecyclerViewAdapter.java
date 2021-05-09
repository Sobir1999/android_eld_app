package com.iosix.eldblesample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;

public class LogRecyclerViewAdapter extends RecyclerView.Adapter<LogRecyclerViewAdapter.LogViewHolder> {

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_items,parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogRecyclerViewAdapter.LogViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class LogViewHolder extends RecyclerView.ViewHolder {

        public LogViewHolder(@NonNull View itemView){
            super(itemView);
        }
    }
}
