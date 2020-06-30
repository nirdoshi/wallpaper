package com.napps.wallpaper;

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
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;

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
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        imageAdapter.itemclicked,Rintone_adapter.itemclicked2 {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FirebaseDatabase database;
    DatabaseReference reff;
    DatabaseReference reff2;
    recyclercontent recyclercontent;
    Ringtonecontent ringtonecontent;
    FirebaseStorage firebaseStorage;

    int active=0;
    Toolbar toolbar;
   // ArrayList<recyclercontent> trend=new ArrayList<>();
    public static int key=100;
    //  RecyclerView.LayoutManager layoutManager;

   // float x1, x2, y1, y2;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Trending Wallpaper");
        setSupportActionBar(toolbar);

        final NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        array_class.trend.clear();

        Intent intent=new Intent();
        active=getIntent().getIntExtra("df",0);

        firebaseStorage=FirebaseStorage.getInstance();

        database=FirebaseDatabase.getInstance();
        reff=database.getReference("wallpapers").child("trending");
        reff2=database.getReference("ringtones").child("trending");
        ArrayList<recyclercontent>nir=new ArrayList<>();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array_class.trend.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    recyclercontent=data.getValue(recyclercontent.class);

                    //String url=data.getValue().toString();
                        array_class.trend.add(recyclercontent);
                  //  array_class.arrayurl.add(recyclercontent);
                   // trend.add(recyclercontent);
                    //Collections.reverse(array_class.arrayurl);
                    Collections.reverse(array_class.trend);
                }

                if (savedInstanceState==null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_wallpaper);
                }
                // array_class.arrayurl.clear();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "not working", Toast.LENGTH_SHORT).show();
            }
        });

        array_class.arrayurl2.clear();
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                array_class.arrayurl2.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    ringtonecontent=data.getValue(Ringtonecontent.class);

                    //String url=data.getValue().toString();
                    array_class.arrayurl2.add(ringtonecontent);
                    Collections.reverse(array_class.arrayurl2);
                }

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







      /*  if (savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.nav_wallpaper, 0);
            navigationView.setCheckedItem(R.id.nav_wallpaper);
        }
        */





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
                toolbar.setTitle("Trending Wallpaper");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                        .commit();
                break;
            case R.id.nav_ringtone:
                toolbar.setTitle("Trending Ringtones");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_ringtone())
                        .commit();
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
        intent.putExtra("url",index);
       // startActivityForResult(intent,2);
            startActivity(intent);
        Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {

        key=100;


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




//meidaplayer
    @Override
    public void onitemclicked2(int index) {

        Intent intent=new Intent(MainActivity.this,music_info.class);
        intent.putExtra("audiourl",index);
        startActivityForResult(intent,3);
        /*
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Player().execute(array_class.arrayurl2.get(index).getUrl());
        ImageView imageView=findViewById(R.id.iv_play);
        imageView.setVisibility(View.GONE);
        ImageView imageView1=findViewById(R.id.iv_pause);
        imageView1.setVisibility(View.VISIBLE);
        Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
        */
    }

    @Override
    public void iv(int index) {
        Intent intent=new Intent(MainActivity.this,music_info.class);
        intent.putExtra("audiourl",index);
        startActivityForResult(intent,3);
    }


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
*/





}