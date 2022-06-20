package com.iosix.eldblesample.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.models.User;

import java.util.ArrayList;
import java.util.List;

public class DriversListAdapter extends RecyclerView.Adapter<DriversListAdapter.DriversViewHolder> {

    Context context;
    ArrayList<User> arrayList;
    TextView textView;
    DriverRecyclerViewItemClickListener listener;

    public DriversListAdapter(Context context, List<User> arrayList){
        this.arrayList = new ArrayList<>();
        this.context = context;
        this.arrayList.addAll(arrayList);
        this.arrayList.add(new User("None","","","","","","",new ArrayList<>(),"","","","",""));
    }

    public void setListener(DriverRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public DriversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.unit_item,parent,false);
        return new DriversViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class DriversViewHolder extends RecyclerView.ViewHolder{

        public DriversViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.idUnitItem);
        }

        void onBind(int i){
            textView.setText(String.format("%s %s",arrayList.get(i).getName(),arrayList.get(i).getLastName()));
            textView.setOnClickListener(view -> {
                listener.onclickItem(String.format("%s %s",arrayList.get(i).getName(),arrayList.get(i).getLastName()));
            });
        }
    }

    public interface DriverRecyclerViewItemClickListener {
        void onclickItem(String s);
    }
}
