package com.iosix.eldblesample.adapters;

import static com.iosix.eldblesample.enums.GeneralConstants.CODRIVERS;
import static com.iosix.eldblesample.enums.GeneralConstants.SHIPPINGDOCS;
import static com.iosix.eldblesample.enums.GeneralConstants.TRAILERS;
import static com.iosix.eldblesample.enums.GeneralConstants.VEHICLES;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.iosix.eldblesample.R;
import com.iosix.eldblesample.fragments.GeneralFragment;
import com.iosix.eldblesample.roomDatabase.entities.TrailersEntity;

import java.util.ArrayList;
import java.util.List;

public class GeneralAdapter extends RecyclerView.Adapter<GeneralAdapter.GeneralViewHolder> {

    private Context context;
    private ArrayList<String> arrayList;
    private String type;
    private onTrailerUpdateListener updateListener;
    private onTrailerDeleteListener deleteListener;

    public void setUpdateListener(onTrailerUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public void setDeleteListener(onTrailerDeleteListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public GeneralAdapter(Context context, ArrayList<String> arraylist,String s){
        this.context = context;
        this.arrayList = arraylist;
        type = s;
    }

    @NonNull
    @Override
    public GeneralViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item_recycler,parent,false);
        return new GeneralAdapter.GeneralViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeneralViewHolder holder, int position) {
        holder.onBind(arrayList.get(position));

        holder.imageView.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onClick(arrayList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class GeneralViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        ImageView imageView;
        ImageView itemImage;

        public GeneralViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.idTrailerRecyclerItemText);
            imageView = itemView.findViewById(R.id.idTrailerDelete);
            itemImage = itemView.findViewById(R.id.idGeneralImage);
        }

        void onBind(String s){
            textView.setText(s);

            switch (type){
                case VEHICLES:{
                    itemImage.setBackgroundResource(R.drawable.ic_truck);
                    break;
                }
                case CODRIVERS:{
                    itemImage.setBackgroundResource(R.drawable.ic_abstract_user_flat_1);
                    break;
                }
                case TRAILERS:{
                    itemImage.setImageResource(R.drawable.trailer);
                    break;
                }
                case SHIPPINGDOCS:{
                    itemImage.setImageResource(R.drawable.document_upload);
                    break;
                }
            }

            textView.setOnClickListener(v -> {
                if (updateListener != null) {
                    updateListener.onClick(getAdapterPosition());
                }
            });
            imageView.setOnClickListener(v -> {
                deleteListener.onClick(s);
            });
        }
    }

    public interface onTrailerUpdateListener {
        void onClick(int position);
    }

    public interface onTrailerDeleteListener {
        void onClick(String s);
    }
}
