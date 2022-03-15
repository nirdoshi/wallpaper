package com.napps.wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import static android.content.Context.MODE_PRIVATE;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class fragment_wallpaper extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    View view;
    Button btncars,btn_nature,btn_travel,btn_bikes,btn_wildlife;
    imageAdapter imageAdapter;

    LinearLayout linearLayout;
    public fragment_wallpaper(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



         view= inflater.inflate(R.layout.fragment_wallpaper,container,false);
         linearLayout = view.findViewById(R.id.llay);
         btncars=view.findViewById(R.id.btncars);
         btn_nature=view.findViewById(R.id.btn_nature);
         btn_travel=view.findViewById(R.id.btn_travel);
         btn_bikes=view.findViewById(R.id.btn_bikes);
         btn_wildlife=view.findViewById(R.id.btn_wildlife);

         btncars.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent intent =new Intent(getActivity(),cars.class);
               //  intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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

//        //gesture logic;
//        final GestureDetector gesture = new GestureDetector(getActivity(),
//                new GestureDetector.SimpleOnGestureListener() {
//
//                    @Override
//                    public boolean onDown(MotionEvent e) {
//                        return true;
//                    }
//
//                    @Override
//                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//                                           float velocityY) {
//
//                        final int SWIPE_MIN_DISTANCE = 120;
//                        final int SWIPE_MAX_OFF_PATH = 250;
//                        final int threshold=100;
//                        final int SWIPE_THRESHOLD_VELOCITY = 200;
//
//                        try {
//                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                                return false;
//                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//                                Toast.makeText(getActivity(), "right to left", Toast.LENGTH_SHORT).show();
//                                Intent intent =new Intent(getActivity(),cars.class);
//                                startActivity(intent);
//                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//
//                                Toast.makeText(getActivity(), "left to right", Toast.LENGTH_SHORT).show();
//                            }
//                        } catch (Exception e) {
//                            // nothing
//                        }
//                        return super.onFling(e1, e2, velocityX, velocityY);
//                    }
//                });
//
//        recyclerView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return gesture.onTouchEvent(event);
//            }
//        });
//        //end of gesture logic;

        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        //layoutManager=new LinearLayoutManager(this.getActivity());
        layoutManager=new GridLayoutManager(this.getActivity(),2);
        recyclerView.setLayoutManager(layoutManager);



        if (MainActivity.key==100){
            imageAdapter = new imageAdapter(this.getActivity(), array_class.trend);
            //setHasOptionsMenu(true);//for search to display;
        }else if(MainActivity.key==101){
            imageAdapter = new imageAdapter(this.getActivity(), array_class.car);
            btncars.setVisibility(View.GONE);
        }else if(MainActivity.key==102){
            imageAdapter = new imageAdapter(this.getActivity(), array_class.nature);
            btn_nature.setVisibility(View.GONE);
        }else if(MainActivity.key==103){
            imageAdapter = new imageAdapter(this.getActivity(), array_class.travel);
            btn_travel.setVisibility(View.GONE);
        }else if(MainActivity.key==104){
            imageAdapter = new imageAdapter(this.getActivity(), array_class.bikes);
            btn_bikes.setVisibility(View.GONE);
        }else {
            imageAdapter = new imageAdapter(this.getActivity(), array_class.wildlife);
            btn_wildlife.setVisibility(View.GONE);
        }
        setHasOptionsMenu(true);//for search to display;
        imageAdapter.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();
        recyclerView.setAdapter(imageAdapter);



    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main,menu);
        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
               // imageAdapter.getFilter().filter(query);
//                Intent intent = new Intent(getActivity(),wildlife.class);
//                intent.putExtra("chr","kite");
//                startActivityForResult(intent,1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.toString().isEmpty()){
                    linearLayout.setVisibility(View.VISIBLE);
                }else{
                    linearLayout.setVisibility(View.GONE);
                }
                imageAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

}
