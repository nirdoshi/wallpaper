package com.napps.wallpaper;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.GestureDetectorCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
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
   // ArrayList<recyclercontent> trend=new ArrayList<>();
    public static int key=100;
    public static int skey=69;
    //  RecyclerView.LayoutManager layoutManager;

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
        String url = "https://api.pexels.com/v1/search?query=trending wallpapers&orientation=portrait&per_page=80";
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
                            progressDialog.dismiss();

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


        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }

    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        getMenuInflater().inflate(R.menu.main,menu);
//        MenuItem item=menu.findItem(R.id.action_search);
//        androidx.appcompat.widget.SearchView searchView= (androidx.appcompat.widget.SearchView) item.getActionView();
//       searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//           @Override
//           public boolean onQueryTextSubmit(String query) {
//               return false;
//           }
//
//           @Override
//           public boolean onQueryTextChange(String newText) {
//
//
//               return false;
//           }
//       });
//        return super.onCreateOptionsMenu(menu);
//    }

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
               // shareintent.putExtra(Intent.EXTRA_SUBJECT,"Use this app to download trending and interesting wallpapers and ringtones");
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

       /*
        super.onActivityResult(requestCode, resultCode, data);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        final SharedPreferences sharedPreferences= getSharedPreferences("my_key",MODE_PRIVATE);
        //active=sharedPreferences.getInt("df",0);
        array_class.arrayurl.clear();
        array_class.arrayurl= (ArrayList<com.napps.wallpaper.recyclercontent>) trend.clone();


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




////meidaplayer
//MediaPlayer mediaPlayer =new MediaPlayer();
//    static int nir= 0;
//    @Override
//    public void onitemclicked2(int index, View v) {
//
////        Intent intent=new Intent(MainActivity.this,music_info.class);
////        intent.putExtra("audiourl",index);
////        startActivityForResult(intent,3);
//
////        new music_info.Player().execute(array_class.arrayurl2.get(index).getUrl());
////        ImageView imageView=findViewById(R.id.iv_play);
////        imageView.setVisibility(View.GONE);
////        ImageView imageView1=findViewById(R.id.iv_pause);
////        imageView1.setVisibility(View.VISIBLE);
////        Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
//
////        play=v.findViewById(R.id.iv_play);
////        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////        String url=array_class.arrayurl2.get(index).getUrl();
////        if (nir){
////            mediaPlayer.stop();
////            mediaPlayer.reset();
////            play.setImageResource(R.drawable.pause_white);
////
////            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
////            new Player().execute(url);
////            Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
////            nir = false;
////        }else{
////            mediaPlayer.stop();
////            play.setImageResource(R.drawable.play_white);
////            nir = true;
////        }
//
//
//    }
//    Rintone_adapter rintone_adapter;
//    public int x = -1;
//    @Override
//    public void iv(int index) {
////        Intent intent=new Intent(MainActivity.this,music_info.class);
////        intent.putExtra("audiourl",index);
////        startActivityForResult(intent,3);
//        x = index;
//        play=findViewById(R.id.iv_play);
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        String url=array_class.arrayurl2.get(index).getUrl();
//        if (nir == 0){
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            //play.setImageResource(R.drawable.pause_white);
//
//
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            new Player().execute(url);
//            Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
//            nir = 1;
//        }
//        if(nir == 1){
//            mediaPlayer.stop();
//           // play.setImageResource(R.drawable.play_white);
//            nir = 0;
//        }
//    }
//    public ProgressDialog progressDialog;
//    public class Player extends AsyncTask<String, Void, Void> {
//
//        @Override
//        protected Void doInBackground(String... params) {
//
//            String url=params[0];
//            //mediaPlayer.stop();
//            try {
//                mediaPlayer.setDataSource(url);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                mediaPlayer.prepare();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    Log.d("completed","completed");
//                   nir = 2;
////                    rintone_adapter = new Rintone_adapter(MainActivity.this, array_class.arrayurl2);
////                    rintone_adapter.notifyItemChanged(x);
//                }
//            });
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void avoid) {
//            progressDialog.dismiss();
//            mediaPlayer.start();
//        }
//
//
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog=new ProgressDialog(MainActivity.this);
//            progressDialog.setMessage("please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            mediaPlayer.stop();
//        }
//
//    }

       /*
    @Override
    public void onpause(int index) {
        mediaPlayer.stop();
        ImageView imageView=findViewById(R.id.iv_pause);
        ImageView imageView1=findViewById(R.id.iv_play);
        imageView.setVisibility(View.GONE);
        imageView1.setVisibility(View.VISIBLE);
    }
    @Override
    public void downloadfile(int index) {
        downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri=Uri.parse(array_class.arrayurl2.get(index).getUrl());
        DownloadManager.Request request=new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("audio/MP3");
        request.allowScanningByMediaScanner();
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,array_class.arrayurl2.get(index).getName()+".mp3");
        request.setTitle(array_class.arrayurl2.get(index).getName());
        Long reference=downloadManager.enqueue(request);
    }

    public class Player extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... params) {
            String url=params[0];
            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void avoid) {
            mediaPlayer.start();
        }
        @Override
        protected void onPreExecute() {
        }
    }



    public class Player extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {

            String url=params[0];
            //mediaPlayer.stop();
            try {
                mediaPlayer.setDataSource(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
//            progressDialog.dismiss();
            mediaPlayer.start();
        }



        @Override
        protected void onPreExecute() {
//            progressDialog=new ProgressDialog(music_info.this);
//            progressDialog.setMessage("please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
            mediaPlayer.stop();
        }

    }

*/

}