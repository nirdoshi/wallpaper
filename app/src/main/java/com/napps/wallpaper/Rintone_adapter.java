package com.napps.wallpaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Rintone_adapter extends RecyclerView.Adapter<Rintone_adapter.viewholder> {

    private ArrayList<Ringtonecontent> arrayurl;


    Rintone_adapter.itemclicked2 activity;

    public interface itemclicked2 {

        public void onitemclicked2(int index);
        public void iv(int index);
    }


    public Rintone_adapter(Context context, ArrayList<Ringtonecontent> list) {

        arrayurl = list;
        activity = (itemclicked2) context;

    }


    public class viewholder extends RecyclerView.ViewHolder {

        ImageView iv_play,iv_download;
        TextView tv_name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            iv_play=itemView.findViewById(R.id.iv_play);
            iv_download=itemView.findViewById(R.id.iv_download);
            tv_name=itemView.findViewById(R.id.tv_name);
            //iv_pause=itemView.findViewById(R.id.iv_pause);
//            iv_pause.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onitemclicked2(arrayurl.indexOf(v.getTag()));
                }
            });

        }
    }

    @NonNull
    @Override
    public Rintone_adapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.ringtone_layout,parent,false);
        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Rintone_adapter.viewholder holder, final int position) {
        holder.itemView.setTag(arrayurl.get(position));
        holder.tv_name.setText(arrayurl.get(position).getName());
        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                activity.iv(position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayurl.size();
    }


}
