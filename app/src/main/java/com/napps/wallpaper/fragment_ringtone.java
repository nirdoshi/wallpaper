package com.napps.wallpaper;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

public class fragment_ringtone extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    View view;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_ringtone,container,false);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        recyclerView=view.findViewById(R.id.Rlist);
        recyclerView.setHasFixedSize(true);


        //layoutManager=new LinearLayoutManager(this.getActivity());
        layoutManager=new GridLayoutManager(getActivity(),2);

        recyclerView.setLayoutManager(layoutManager);

        adapter=new Rintone_adapter(getActivity(),array_class.arrayurl2);
        //adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }



}
