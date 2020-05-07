package com.napps.wallpaper;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.Environment;
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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        imageAdapter.itemclicked,Rintone_adapter.itemclicked2 {

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;
    FirebaseDatabase database;
    DatabaseReference reff;
    DatabaseReference reff2;
    recyclercontent recyclercontent;
    Ringtonecontent ringtonecontent;
    DownloadManager downloadManager;
    MediaPlayer mediaPlayer;

    FragmentManager mFragmentManager;
    private fragment_ringtone fragment_ringtone;

    //  RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        array_class.arrayurl.clear();




        database=FirebaseDatabase.getInstance();
        reff=database.getReference("wallpapers").child("trending");
        reff2=database.getReference("ringtones").child("trending");


        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){
                       recyclercontent=data.getValue(recyclercontent.class);

                        //String url=data.getValue().toString();
                        array_class.arrayurl.add(recyclercontent);
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

            array_class.arrayurl2.clear();
        reff2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot data:dataSnapshot.getChildren()){
                    ringtonecontent=data.getValue(Ringtonecontent.class);

                    //String url=data.getValue().toString();
                    array_class.arrayurl2.add(ringtonecontent);
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
                finish();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_wallpaper:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                        .commit();
                break;
            case R.id.nav_ringtone:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_ringtone())
                        .commit();
                break;
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
        startActivityForResult(intent,2);

        Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode==1 && requestCode==RESULT_OK) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_wallpaper()).addToBackStack(null)
                    .commit();
        }*/
        final NavigationView navigationView=findViewById(R.id.nav_view);


        array_class.arrayurl.clear();


            reff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot data:dataSnapshot.getChildren()){
                        recyclercontent=data.getValue(recyclercontent.class);

                        //String url=data.getValue().toString();
                        array_class.arrayurl.add(recyclercontent);
                    }


                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper()).addToBackStack(null)
                                .commit();
                        navigationView.setCheckedItem(R.id.nav_wallpaper);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "not working", Toast.LENGTH_SHORT).show();
                }
            });

        }


    @Override
    public void onitemclicked2(int index) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Player().execute(array_class.arrayurl2.get(index).getUrl());
        ImageView imageView=findViewById(R.id.iv_play);
        imageView.setVisibility(View.GONE);
        ImageView imageView1=findViewById(R.id.iv_pause);
        imageView1.setVisibility(View.VISIBLE);
        Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
    }

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








}