package com.napps.wallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class nature extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        imageAdapter.itemclicked{

    DrawerLayout drawer;
    //DrawerLayout drawer;
    FirebaseDatabase database;
    DatabaseReference reff,reff2;
    recyclercontent recyclercontent;
    Ringtonecontent ringtonecontent;
   // ArrayList<recyclercontent> nature=new ArrayList<>();

    DownloadManager downloadManager;
    MediaPlayer mediaPlayer;
    Toolbar toolbar;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nature);
        MainActivity.key=102;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Nature Wallpapers");
        setSupportActionBar(toolbar);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fetching data from api;
        array_class.nature.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.pexels.com/v1/search?query=nature&orientation=portrait&per_page=80";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d("response",response.toString());
                        try {
                            JSONArray photos = response.getJSONArray("photos");
                            for (int i=0; i<photos.length();i++){
                                JSONObject obj = photos.getJSONObject(i);
                                String url = obj.getJSONObject("src").getString("portrait");
                                // Log.d("url",url);
                                recyclercontent = new recyclercontent();
                                recyclercontent.setImage(url);
                                array_class.nature.add(recyclercontent);
                                //Log.d("test",array_class.trend.get(i).getImage());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (savedInstanceState == null){
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_wallpaper()).commit();
                            navigationView.setCheckedItem(R.id.nav_wallpaper);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.getLocalizedMessage());
                        // TODO: Handle error

                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization","563492ad6f917000010000018847b2816aa04f2c94801b34f49b76bf");
                return params;
            }
        };
        queue.add(jsonObjectRequest);
        //end of fetching data.



        database=FirebaseDatabase.getInstance();
        reff2=database.getReference("ringtones").child("trending");
//        reff=database.getReference("wallpapers").child("nature");
//        array_class.nature.clear();
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                array_class.nature.clear();
//                for (DataSnapshot data:dataSnapshot.getChildren()){
//                    recyclercontent=data.getValue(recyclercontent.class);
//                    array_class.nature.add(recyclercontent);
//                    //String url=data.getValue().toString();
//                   // array_class.arrayurl.add(recyclercontent);
//                   // nature.add(recyclercontent);
//                    //Collections.reverse(array_class.arrayurl);
//
//                }
//                Collections.reverse(array_class.nature);
//
//                if (savedInstanceState==null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
//                            .commit();
//                    navigationView.setCheckedItem(R.id.nav_wallpaper);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(nature.this, "not working", Toast.LENGTH_SHORT).show();
//            }
//        });

        array_class.arrayurl2.clear();
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array_class.arrayurl2.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    ringtonecontent=data.getValue(Ringtonecontent.class);

                    //String url=data.getValue().toString();
                    array_class.arrayurl2.add(ringtonecontent);


                }
                Collections.reverse(array_class.arrayurl2);

                if (savedInstanceState==null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_wallpaper);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(nature.this, "not working", Toast.LENGTH_SHORT).show();
            }
        });




        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent=new Intent(nature.this,MainActivity.class);
        setResult(RESULT_OK,intent);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            finish();
            super.onBackPressed();
        }
    }


    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_wallpaper:
//                toolbar.setTitle("Trending Wallpaper");
//                MainActivity.key=100;
//                //startActivity(new Intent(cars.this,MainActivity.class));
//                Intent intent=new Intent(nature.this,MainActivity.class);
//                setResult(RESULT_OK,intent);
//                startActivity(intent);
                // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                //       .commit();
                Toast.makeText(this, "You are already in wallpapers section", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_ringtone:
                toolbar.setTitle("Trending Ringtones");
                menuItem.setCheckable(false);
                Intent intent1 = new Intent(this, Ringtone_Refresh.class);
                startActivityForResult(intent1,1);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onitemclicked(int index) {
        Intent intent=new Intent(nature.this,setwallpaper.class);
        // String url= array_class.arrayurl.get(index).getImage();
        intent.putExtra("url",index);
       // startActivityForResult(intent,0);
        startActivity(intent);
        Toast.makeText(this, "click anywhere", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        MainActivity.key=102;
        toolbar.setTitle("Nature Wallpapers");
        /*
        super.onActivityResult(requestCode, resultCode, data);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        final SharedPreferences sharedPreferences= getSharedPreferences("my_key",MODE_PRIVATE);
        //active=sharedPreferences.getInt("df",0);
        array_class.arrayurl.clear();
        array_class.arrayurl= (ArrayList<com.napps.wallpaper.recyclercontent>) nature.clone();

        if (resultCode==RESULT_OK) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_wallpaper);
        }
        else{
            MediaPlayer mediaPlayer=new MediaPlayer();
            mediaPlayer.stop();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_ringtone())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_ringtone);
        }

        */


    }

//    @Override
//    public void onitemclicked2(int index, View v) {
//        Intent intent=new Intent(nature.this,music_info.class);
//        intent.putExtra("audiourl",index);
//        startActivityForResult(intent,2);
//    }
//
//    @Override
//    public void iv(int index, View v) {
//        Intent intent=new Intent(nature.this,music_info.class);
//        intent.putExtra("audiourl",index);
//        startActivityForResult(intent,3);
//    }
//
//    @Override
//    public void download(int index, View view) throws IOException {
//
//    }

}
