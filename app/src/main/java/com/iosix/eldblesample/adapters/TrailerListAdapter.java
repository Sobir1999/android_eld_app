package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TrailerListAdapter extends RecyclerView.Adapter<TrailerListAdapter.TrailerListViewHolder> {

    Context context;
    ArrayList<TrailersEntity> arrayList;
    private TrailerRecyclerAdapter.onTrailerUpdateListener updateListener;
    private TrailerRecyclerAdapter.onTrailerDeleteListener deleteListener;

    public void setUpdateListener(TrailerRecyclerAdapter.onTrailerUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setDeleteListener(TrailerRecyclerAdapter.onTrailerDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public TrailerListAdapter(Context context, List<TrailersEntity> trailersEntities) {
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(trailersEntities);
    }

    @NonNull
    @Override
    public TrailerListAdapter.TrailerListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_item,parent,false);
        return new TrailerListAdapter.TrailerListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerListAdapter.TrailerListViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class TrailerListViewHolder extends RecyclerView.ViewHolder{

        TextView unitItem;

        public TrailerListViewHolder(@NonNull View itemView) {
            super(itemView);
            unitItem = itemView.findViewById(R.id.idUnitItem);
        }

        void onBind(int i){
            unitItem.setText(arrayList.get(i).getNumber());
            unitItem.setOnClickListener(view -> {
                if (updateListener != null) {
                    updateListener.onClick(getAbsoluteAdapterPosition());
                }
            });

        }
    }

    public interface onTrailerUpdateListener {
        void onClick(int position);
    }

    public interface onTrailerDeleteListener {
        void onClick(TrailersEntity trailersEntity);
    }
}
