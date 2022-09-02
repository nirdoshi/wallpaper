package com.napps.wallpaper;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

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
import com.google.firebase.storage.FirebaseStorage;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        imageAdapter.itemclicked{


    DrawerLayout drawer;
    FirebaseDatabase database;

    DatabaseReference reff2;
    recyclercontent recyclercontent;
    Ringtonecontent ringtonecontent;
    FirebaseStorage firebaseStorage;

    int active=0;
    Toolbar toolbar;

    public static int key=100;
    //public static int skey=69;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Trending Wallpapers");
        setSupportActionBar(toolbar);

        final NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //fetching data from api;
        array_class.trend.clear();
        progressDialog = new ProgressDialog(MainActivity.this);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.pexels.com/v1/search?query=phone wallpaper &orientation=portrait&per_page=80";
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject >() {

                    @Override
                    public void onResponse(JSONObject response) {
                       // Log.d("response",response.toString());
                        try {

                            JSONArray photos = response.getJSONArray("photos");
                            for (int i=0; i<photos.length();i++){
                                JSONObject obj = photos.getJSONObject(i);
                                String url = obj.getJSONObject("src").getString("portrait");
                               // Log.d("url",url);
                                recyclercontent = new recyclercontent();
                                recyclercontent.setImage(url);
                                array_class.trend.add(recyclercontent);
                              //  Log.d("test",array_class.trend.get(i).getImage());

                            }
                            Log.d("initial length",array_class.trend.size()+"");
                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                       // if (savedInstanceState == null){

                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_wallpaper()).commit();
                            navigationView.setCheckedItem(R.id.nav_wallpaper);
                        //}


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error",error.getLocalizedMessage());
                        // TODO: Handle error
                        Toast.makeText(MainActivity.this, "Servers are down Temporary", Toast.LENGTH_SHORT).show();
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




        active=getIntent().getIntExtra("df",0);
        firebaseStorage=FirebaseStorage.getInstance();
        database=FirebaseDatabase.getInstance();
        //reff=database.getReference("wallpapers").child("test");
        reff2=database.getReference("ringtones").child("trending");
//
//        reff=database.getReference("wallpapers");
//
//        ArrayList<recyclercontent>all=new ArrayList<>();
//
//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for(DataSnapshot data:dataSnapshot.getChildren()){
//                    for(DataSnapshot subdata:data.getChildren()){
//                        recyclercontent=subdata.getValue(recyclercontent.class);
//                        array_class.trend.add(recyclercontent);
//
//                        //System.out.println("NIR DOSHI");
//                    }
//                }
//                //Log.d("trend",array_class.trend.get(1).getImage());
//                Collections.shuffle(array_class.trend);
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
//
//            }
//        });




//        reff.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                array_class.test.clear();
//                for (DataSnapshot data:dataSnapshot.getChildren()){
//                    recyclercontent=data.getValue(recyclercontent.class);
//
//                    //String url=data.getValue().toString();
//                        array_class.test.add(recyclercontent);
//                  //  array_class.arrayurl.add(recyclercontent);
//                   // trend.add(recyclercontent);
//                    //Collections.reverse(array_class.arrayurl);
//
//                }
//                Collections.reverse(array_class.test);
//
//                if (savedInstanceState==null) {
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
//                            .commit();
//                    navigationView.setCheckedItem(R.id.nav_wallpaper);
//                }
//                // array_class.arrayurl.clear();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(MainActivity.this, "not working", Toast.LENGTH_SHORT).show();
//            }
//        });
        //Collections.reverse(array_class.trend);

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
                Toast.makeText(MainActivity.this, "not working", Toast.LENGTH_SHORT).show();
            }
        });


        drawer = findViewById(R.id.big_image);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
           // finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_wallpaper:
                toolbar.setTitle("Trending Wallpapers");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                        .commit();
                break;
            case R.id.nav_ringtone:
                toolbar.setTitle("Trending Ringtones");
                menuItem.setCheckable(false);
                Intent intent = new Intent(this, Ringtone_Refresh.class);
                startActivityForResult(intent,1);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_ringtone())
//                        .commit();
                //
                break;
            case R.id.nav_share:
                Intent shareintent=new Intent();
                shareintent.setAction(Intent.ACTION_SEND);
                shareintent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.napps.wallpaper&hl=en");
                shareintent.setType("text/plain");
                startActivity(Intent.createChooser(shareintent,"Share via"));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onitemclicked(int index) {

        Intent intent=new Intent(MainActivity.this,setwallpaper.class);
        //String url=array_class.arrayurl.get(index).getImage();
        //intent.putExtra("url",ur);
        //System.out.println(index);
        intent.putExtra("url",index);
       // startActivityForResult(intent,2);
        startActivity(intent);
        Toast.makeText(this, "click anywhere", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {

        key=100;
        toolbar.setTitle("Trending Wallpapers");
        setwallpaper.handle=false;

    }

}