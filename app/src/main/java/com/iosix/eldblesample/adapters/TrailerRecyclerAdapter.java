package com.iosix.eldblesample.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.util.ArrayList;

public class TrailerRecyclerAdapter extends RecyclerView.Adapter<TrailerRecyclerAdapter.TrailerHolder> {
    private ArrayList<TrailersEntity> trailers;
    private onTrailerUpdateListener updateListener;
    private onTrailerDeleteListener deleteListener;

    public void setUpdateListener(onTrailerUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setDeleteListener(onTrailerDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public TrailerRecyclerAdapter(ArrayList<TrailersEntity> trailers) {
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public TrailerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_recycler, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder holder, int position) {
        holder.onBind(trailers.get(position));
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;

        public TrailerHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.idTrailerRecyclerItemText);
            imageView = itemView.findViewById(R.id.idTrailerDelete);
        }

        void onBind(TrailersEntity s) {
            textView.setText(s.getNumber());

            textView.setOnClickListener(v -> {
                if (updateListener != null) {
                    updateListener.onClick();
                }
            });

            imageView.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onClick();
                }
            });
        }
    }

    interface onTrailerUpdateListener {
        void onClick();
    }

    interface onTrailerDeleteListener {
        void onClick();
    }
}
