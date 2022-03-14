package com.napps.wallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;

public class Ringtone_Refresh extends AppCompatActivity implements Rintone_adapter.itemclicked2, MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ProgressDialog progressDialog;
    DownloadManager downloadManager;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringtone_refresh);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Trending Ringtones");
        setSupportActionBar(toolbar);



        recyclerView=findViewById(R.id.Rlist);
        recyclerView.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);

       // layoutManager=new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);

        adapter=new Rintone_adapter(this,array_class.arrayurl2);
        //adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }

    MediaPlayer mediaPlayer =new MediaPlayer();
    ImageView iv_play;

    @Override
    public void onitemclicked2(int index, View itemView) {
//        play = itemView.findViewById(R.id.iv_play);
//        x= index;
//        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//        String url=array_class.arrayurl2.get(index).getUrl();
//        if (nir){
//            mediaPlayer.stop();
//            mediaPlayer.reset();
//            play.setImageResource(R.drawable.pause);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            new Player().execute(url);
//            Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
//            nir = false;
//        }else{
//            mediaPlayer.stop();
//            play.setImageResource(R.drawable.play);
//            nir = true;
//        }

    }
    ArrayList<Integer> pos = new ArrayList<>();

    @Override
    public void iv(int index, View v) throws IOException {

        if(pos.size() == 0){
            pos.add(index);
            iv_play = v.findViewById(R.id.iv_play);
            if(!mediaPlayer.isPlaying()){
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("please wait");
                progressDialog.setCancelable(false);
                progressDialog.show();
                iv_play.setImageResource(R.drawable.pause);
                play(index);
            }else{
                iv_play.setImageResource(R.drawable.play);
                pause();
            }
        }
        else{
            pos.add(index);
            if(pos.get(0)== pos.get(1)){
                //legit stop
                pos.clear();
                iv_play = v.findViewById(R.id.iv_play);
                if(!mediaPlayer.isPlaying()){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("please wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    iv_play.setImageResource(R.drawable.pause);
                    play(index);
                }else{
                    iv_play.setImageResource(R.drawable.play);
                    pause();
                }

            }else{

                //View V = layoutManager.findViewByPosition(pos.get(0));
                Rintone_adapter.viewholder V = ((Rintone_adapter)recyclerView.getAdapter()).getViewByPosition(pos.get(0));
                ImageView returnp = V.iv_play;
                returnp.setImageResource(R.drawable.play);
                pause();
                pos.remove(0);
                iv_play = v.findViewById(R.id.iv_play);
                if(!mediaPlayer.isPlaying()){
                    progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("please wait");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    iv_play.setImageResource(R.drawable.pause);
                    play(index);
                }else{
                    iv_play.setImageResource(R.drawable.play);
                    pause();
                }
                //new audio
            }
        }




    }

    @Override
    public void download(int index, View view) throws IOException {
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

    private void pause() {
        mediaPlayer.pause();
    }

    private void play(int index) {
        try {
            //Toast.makeText(this, "play", Toast.LENGTH_SHORT).show();
                mediaPlayer.stop();
                mediaPlayer.reset();

            String url=array_class.arrayurl2.get(index).getUrl();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnBufferingUpdateListener(this);

            mediaPlayer.setOnErrorListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    Toast.makeText(Ringtone_Refresh.this, "completed", Toast.LENGTH_SHORT).show();
                    iv_play.setImageResource(R.drawable.play);
                }
            });

        } catch (Throwable t) {
            Log.d("a", t.toString());
        }
    }
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        progressDialog.dismiss();
        mediaPlayer.start();
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        mediaPlayer.stop();
        Toast.makeText(this, "completed", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
            mediaPlayer.stop();
            super.onBackPressed();
    }

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
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void avoid) {
//            progressDialog.dismiss();
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                @Override
//                public void onCompletion(MediaPlayer mediaPlayer) {
//                    play.setImageResource(R.drawable.play);
//                    nir =true;
//                }
//            });
//        }
//
//        @Override
//        protected void onPreExecute() {
//            progressDialog=new ProgressDialog(Ringtone_Refresh.this);
//            progressDialog.setMessage("please wait");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
//            mediaPlayer.stop();
//        }
//
//    }
}