package com.napps.wallpaper;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

public class fragment_wallpaper extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    View view;
    Button btncars,btn_nature,btn_travel,btn_bikes,btn_wildlife;


    public fragment_wallpaper(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view= inflater.inflate(R.layout.fragment_wallpaper,container,false);
         btncars=view.findViewById(R.id.btncars);
         btn_nature=view.findViewById(R.id.btn_nature);
         btn_travel=view.findViewById(R.id.btn_travel);
         btn_bikes=view.findViewById(R.id.btn_bikes);
         btn_wildlife=view.findViewById(R.id.btn_wildlife);

         btncars.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent =new Intent(getActivity(),cars.class);
                 startActivityForResult(intent,1);
             }
         });
         btn_nature.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent =new Intent(getActivity(),nature.class);
                 startActivityForResult(intent,1);

             }
         });
         btn_travel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent =new Intent(getActivity(),travel.class);
                 startActivityForResult(intent,1);

             }
         });
         btn_bikes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent =new Intent(getActivity(),bikes.class);
                 startActivityForResult(intent,1);

             }
         });
         btn_wildlife.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent =new Intent(getActivity(),wildlife.class);
                 startActivityForResult(intent,1);
             }
         });


        return  view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        recyclerView=view.findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //layoutManager=new LinearLayoutManager(this.getActivity());
        layoutManager=new GridLayoutManager(this.getActivity(),2);

        recyclerView.setLayoutManager(layoutManager);

        adapter=new imageAdapter(this.getActivity(),array_class.arrayurl);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

}
