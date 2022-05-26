package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.roomDatabase.entities.VehiclesEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class UnitListAdapter extends RecyclerView.Adapter<UnitListAdapter.UnitListViewHolder> {

    Context context;
    ArrayList<VehiclesEntity> arrayList;
    UnitListListener listener;

    public UnitListAdapter(Context context, List<VehiclesEntity> vehiclesEntities) {
        this.context = context;
        arrayList = new ArrayList<>();
        arrayList.addAll(vehiclesEntities);
    }

    public void setListener(UnitListListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public UnitListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unit_item,parent,false);
        return new UnitListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UnitListViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class UnitListViewHolder extends RecyclerView.ViewHolder{

        TextView unitItem;

        public UnitListViewHolder(@NonNull View itemView) {
            super(itemView);
            unitItem = itemView.findViewById(R.id.idUnitItem);
        }

        void onBind(int i){
            unitItem.setText(arrayList.get(i).getName());
            unitItem.setOnClickListener(view -> {
                if (listener != null){
                    listener.onItemClick(arrayList.get(i).getName());
                }
            });

        }
    }

    public interface UnitListListener{
        void onItemClick(String s);
    }
}
