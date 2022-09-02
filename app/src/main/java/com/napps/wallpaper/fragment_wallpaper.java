package com.napps.wallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
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
import java.util.logging.Handler;

public class fragment_wallpaper extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

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
                 setwallpaper.handle=false;
                 Intent intent =new Intent(getActivity(),cars.class);
               //  intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                 startActivityForResult(intent,1);
             }
         });

         btn_nature.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 setwallpaper.handle=false;
                 Intent intent =new Intent(getActivity(),nature.class);
                 startActivityForResult(intent,1);

             }
         });
         btn_travel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 setwallpaper.handle=false;
                 Intent intent =new Intent(getActivity(),travel.class);
                 startActivityForResult(intent,1);

             }
         });
         btn_bikes.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 setwallpaper.handle=false;
                 Intent intent =new Intent(getActivity(),bikes.class);
                 startActivityForResult(intent,1);

             }
         });
         btn_wildlife.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 setwallpaper.handle=false;
                 Intent intent =new Intent(getActivity(),wildlife.class);
                 startActivityForResult(intent,1);
             }
         });

        recyclerView=view.findViewById(R.id.list);
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
        }else if (MainActivity.key==105){
            imageAdapter = new imageAdapter(this.getActivity(), array_class.wildlife);
            btn_wildlife.setVisibility(View.GONE);
        }else {
            imageAdapter = new imageAdapter(this.getActivity(), array_class.test);
            linearLayout.setVisibility(View.GONE);
        }
        setHasOptionsMenu(true);//for search to display;
        imageAdapter.notifyDataSetChanged();
        //adapter.notifyDataSetChanged();
        //recyclerView.getLayoutManager().onRestoreInstanceState(listState);

        recyclerView.setAdapter(imageAdapter);

        return  view;

    }

    private final String KEY_RECYCLER_STATE = "recycler_state";
    public static Bundle mBundleRecyclerViewState;
    private Parcelable mListState = null;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        if (!setwallpaper.handle){
            mBundleRecyclerViewState=null;
        }else {
            if(mBundleRecyclerViewState!=null) {
                Log.d("instanceRestored", "instanceRestored");
                mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
                recyclerView.getLayoutManager().onRestoreInstanceState(mListState);
            }
        }


    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mBundleRecyclerViewState = new Bundle();
        mListState = recyclerView.getLayoutManager().onSaveInstanceState();
        Log.d("state_value",mListState+"");
        Log.d("activity1",getActivity()+"");
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
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
//
                Intent intent = new Intent(getActivity(),search_activity.class);
                intent.putExtra("query",query);
                setwallpaper.handle=false;
                startActivityForResult(intent,1);
//                if(query.toString().isEmpty()){
//                    linearLayout.setVisibility(View.VISIBLE);
//                }else{
//                    linearLayout.setVisibility(View.GONE);
//                }
//                imageAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (newText.toString().isEmpty()){
//                    linearLayout.setVisibility(View.VISIBLE);
//                    imageAdapter.getFilter().filter(newText);
//                }

                return false;
            }
        });
    }

}
