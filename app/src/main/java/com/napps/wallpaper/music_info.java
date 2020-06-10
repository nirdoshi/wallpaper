package com.napps.wallpaper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;

public class music_info extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ImageView play,download;
    TextView name;
    MediaPlayer mediaPlayer;
    DownloadManager downloadManager;
    Boolean nir=true;
    DrawerLayout drawer;
    public ProgressDialog progressDialog;
    private static final int PERMISSION_REQUEST_CODE =1000 ;
    Toolbar toolbar;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        //  contentResolverWeakReference = new WeakReference<ContentResolver>(getApplicationContext().getContentResolver());

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                }
            }


            break;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_info);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{

                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            },PERMISSION_REQUEST_CODE);
        }

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ringtone info");
        setSupportActionBar(toolbar);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mediaPlayer = new MediaPlayer();
        final int index=getIntent().getIntExtra("audiourl",0);
        play=findViewById(R.id.play);
        download=findViewById(R.id.download);
        name=findViewById(R.id.name);
        String url=array_class.arrayurl2.get(index).getUrl();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        name.setText(array_class.arrayurl2.get(index).getName());




        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nir) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                    play.setImageResource(R.drawable.pause_white);

                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    new Player().execute(array_class.arrayurl2.get(index).getUrl());
                    Toast.makeText(music_info.this, "please wait", Toast.LENGTH_SHORT).show();
                    nir=false;
                }else {

                    mediaPlayer.stop();
                    play.setImageResource(R.drawable.play_white);
                    nir=true;
                }


            }
        });




        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadManager=(DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri= Uri.parse(array_class.arrayurl2.get(index).getUrl());
                DownloadManager.Request request=new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setMimeType("audio/MP3");
                request.allowScanningByMediaScanner();
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,array_class.arrayurl2.get(index).getName()+".mp3");
                request.setTitle(array_class.arrayurl2.get(index).getName());

                Long reference=downloadManager.enqueue(request);
            }
        });




        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setResult(1);

    }

    @Override
    public void onBackPressed() {
            mediaPlayer.stop();
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
            progressDialog.dismiss();
            mediaPlayer.start();
        }



        @Override
        protected void onPreExecute() {
            progressDialog=new ProgressDialog(music_info.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();
            mediaPlayer.stop();
        }

    }



}
