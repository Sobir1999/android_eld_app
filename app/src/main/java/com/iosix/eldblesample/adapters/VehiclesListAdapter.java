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

public class VehiclesListAdapter extends RecyclerView.Adapter<VehiclesListAdapter.VehiclesViewHolder> {

    Context context;
    ArrayList<VehiclesEntity> arrayList;
    TextView textView;
    VehiclesRecyclerViewItemClickListener listener;

    public VehiclesListAdapter(Context context, List<VehiclesEntity> arrayList){
        this.arrayList = new ArrayList<>();
        this.context = context;
        this.arrayList.addAll(arrayList);
    }

    public void setListener(VehiclesRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public VehiclesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.unit_item,parent,false);
        return new VehiclesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class VehiclesViewHolder extends RecyclerView.ViewHolder{

        public VehiclesViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.idUnitItem);
        }

        void onBind(int i){
            textView.setText(arrayList.get(i).getName());
            textView.setOnClickListener(view -> {
                listener.onclickItem(arrayList.get(i).getName());
            });
        }
    }

    public interface VehiclesRecyclerViewItemClickListener {
        void onclickItem(String s);
    }
}
