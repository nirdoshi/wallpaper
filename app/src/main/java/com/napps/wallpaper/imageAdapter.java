package com.napps.wallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.viewholder> {
    private ArrayList<recyclercontent> arrayurl;

    itemclicked activity;



    public interface itemclicked {

        public void onitemclicked(int index);
    }


    public imageAdapter(Context context, ArrayList<recyclercontent> list) {

        arrayurl = list;
        activity = (itemclicked) context;

    }


    public class viewholder extends RecyclerView.ViewHolder {

        ImageView iv;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onitemclicked(arrayurl.indexOf(v.getTag()));
                }
            });

        }
    }

    @NonNull
    @Override
    public imageAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.images_layout,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageAdapter.viewholder holder, int position) {
        holder.itemView.setTag(arrayurl.get(position));
        Picasso.get().load(arrayurl.get(position).getImage()).fit().into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return arrayurl.size();
    }

}