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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class cars extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        imageAdapter.itemclicked,Rintone_adapter.itemclicked2 {
    DrawerLayout drawer;
    //DrawerLayout drawer;
    FirebaseDatabase database;
    DatabaseReference reff,reff2;
    recyclercontent recyclercontent;
    Ringtonecontent ringtonecontent;
   // ArrayList<recyclercontent> car=new ArrayList<>();
    DownloadManager downloadManager;
    MediaPlayer mediaPlayer;
    Toolbar toolbar;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cars);
        MainActivity.key=101;

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Cars wallpaper");
        setSupportActionBar(toolbar);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        database=FirebaseDatabase.getInstance();
        reff2=database.getReference("ringtones").child("trending");
        reff=database.getReference("wallpapers").child("cars");
       array_class.car.clear();
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    array_class.car.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    recyclercontent=data.getValue(recyclercontent.class);


                    array_class.car.add(recyclercontent);
                    //String url=data.getValue().toString();
                   // array_class.arrayurl.add(recyclercontent);
                   // car.add(recyclercontent);
                   // Collections.reverse(array_class.arrayurl);
                   // Collections.reverse(array_class.car);
                }
                Collections.reverse(array_class.car);
                if (savedInstanceState==null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                            .commit();
                    navigationView.setCheckedItem(R.id.nav_wallpaper);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(cars.this, "not working", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(cars.this, "not working", Toast.LENGTH_SHORT).show();
            }
        });




        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent=new Intent(cars.this,MainActivity.class);
        setResult(RESULT_OK,intent);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);


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


    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_wallpaper:
                toolbar.setTitle("Trending Wallpaper");
               //startActivity(new Intent(cars.this,MainActivity.class));
                Intent intent=new Intent(cars.this,MainActivity.class);
              //  setResult(RESULT_OK,intent);
                startActivity(intent);
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_wallpaper())
                 //       .commit();
                break;
            case R.id.nav_ringtone:
                toolbar.setTitle("Trending Ringtones");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new fragment_ringtone())
                        .commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onitemclicked(int index) {
        Intent intent=new Intent(cars.this,setwallpaper.class);
       // String url= array_class.arrayurl.get(index).getImage();
        intent.putExtra("url",index);
        startActivity(intent);
        Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {

        MainActivity.key=101;

        /*
        super.onActivityResult(requestCode, resultCode, data);
        final NavigationView navigationView=findViewById(R.id.nav_view);
        final SharedPreferences sharedPreferences= getSharedPreferences("my_key",MODE_PRIVATE);
        //active=sharedPreferences.getInt("df",0);
        array_class.arrayurl.clear();
        array_class.arrayurl= (ArrayList<com.napps.wallpaper.recyclercontent>) car.clone();

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



    @Override
    public void onitemclicked2(int index) {
        Intent intent=new Intent(cars.this,music_info.class);
        intent.putExtra("audiourl",index);
        startActivityForResult(intent,2);
    }

    @Override
    public void iv(int index) {
        Intent intent=new Intent(cars.this,music_info.class);
        intent.putExtra("audiourl",index);
        startActivityForResult(intent,3);
    }


}
