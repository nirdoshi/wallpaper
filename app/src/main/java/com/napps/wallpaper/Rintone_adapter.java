package com.napps.wallpaper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Rintone_adapter extends RecyclerView.Adapter<Rintone_adapter.viewholder> {

    private ArrayList<Ringtonecontent> arrayurl;
    Rintone_adapter.itemclicked2 activity;
    HashMap<Integer, Rintone_adapter.viewholder> holderlist;

    public interface itemclicked2 {

        public void onitemclicked2(int index, View itemView);
        public void iv(int index, View view) throws IOException;
        public void download(int index, View view) throws IOException;
    }


    public Rintone_adapter(Context context, ArrayList<Ringtonecontent> list) {

        arrayurl = list;
        activity = (itemclicked2) context;
        holderlist = new HashMap<>();
    }


    public class viewholder extends RecyclerView.ViewHolder {

        ImageView iv_play, iv_download;
        TextView tv_name;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            iv_play=itemView.findViewById(R.id.iv_play);
           // iv_download=itemView.findViewById(R.id.iv_download);
            tv_name=itemView.findViewById(R.id.tv_name);
            iv_download = itemView.findViewById(R.id.iv_download);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onitemclicked2(arrayurl.indexOf(v.getTag()),itemView);
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
    public void onBindViewHolder(@NonNull Rintone_adapter.viewholder holder, @SuppressLint("RecyclerView") final int position) {
        holder.itemView.setTag(arrayurl.get(position));
        holder.tv_name.setText(arrayurl.get(position).getName());

        if(!holderlist.containsKey(position)){
            holderlist.put(position,holder);
        }
//        if(!click.get(position)){
//            holder.iv_play.setImageResource(R.drawable.play);
//        }


        holder.iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Toast.makeText(v.getContext(), "click working", Toast.LENGTH_SHORT).show();


                try {
                    activity.iv(position, holder.iv_play);
                } catch (IOException e) {
                    e.printStackTrace();
                }


//                if (!Ringtone_Refresh.nir) {
//                    holder.iv_play.setImageResource(R.drawable.pause);
//                    click.set(position,true);
//                } else {
//                    holder.iv_play.setImageResource(R.drawable.play);
//                    click.set(position,false);
//                }

//
//                if(click_switch){
//                    click_switch = false;
//                    click.set(position,true);
//                }else{
//                    click_switch = true;
//                    click.set(position,true);
//                }

              //  notifyItemChanged(holder.getAdapterPosition());

            }
        });

        holder.iv_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    activity.download(position,holder.iv_download);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayurl.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public Rintone_adapter.viewholder getViewByPosition (int position){
        return holderlist.get(position);
    }

}
