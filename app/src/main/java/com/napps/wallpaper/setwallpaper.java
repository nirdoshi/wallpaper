package com.napps.wallpaper;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.UUID;

public class setwallpaper extends AppCompatActivity{


    private static final int PERMISSION_REQUEST_CODE =1000 ;
   // DrawerLayout drawer;
    //Button btn_set,btn_download;
    ImageView imageView_set;
    int index;
    ProgressBar progressBar;
    AlertDialog.Builder builder;
    public ProgressDialog progressDialog;
    //WeakReference<ContentResolver> contentResolverWeakReference ;


    //ProgressDialog mProgressDialog;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



      //  contentResolverWeakReference = new WeakReference<ContentResolver>(getApplicationContext().getContentResolver());

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
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
        setContentView(R.layout.activity_setwallpaper);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{

                    Manifest.permission.WRITE_EXTERNAL_STORAGE


            },PERMISSION_REQUEST_CODE);
        }


       // Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //final NavigationView navigationView=findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        imageView_set=findViewById(R.id.imageview_set);

        Intent intent=new Intent();
        //String url=intent.getStringExtra("url");
            int postion=getIntent().getIntExtra("url",0);
            index=postion;
        Picasso.get().load(array_class.arrayurl.get(postion).getImage()).into(imageView_set);



        imageView_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // WallpaperManager wallpaperManager = WallpaperManager.getInstance(setwallpaper.this);



                builder = new AlertDialog.Builder(setwallpaper.this);
                builder.setTitle("Choose Options");
                builder.setItems(new CharSequence[]
                                {"set homescreen", "set lockscreen", "download"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        new settask().execute(array_class.arrayurl.get(index).getImage());
                                        //setimage();
                                       // Toast.makeText(setwallpaper.this, "hello", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                       // setlockimage();
                                        new settask2().execute(array_class.arrayurl.get(index).getImage());
                                        //Toast.makeText(setwallpaper.this, "hello", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 2:

                                        if (ActivityCompat.checkSelfPermission(setwallpaper.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){

                                            Toast.makeText(setwallpaper.this, "you should grant permission", Toast.LENGTH_SHORT).show();
                                            requestPermissions(new String[]{

                                                    Manifest.permission.WRITE_EXTERNAL_STORAGE


                                            },PERMISSION_REQUEST_CODE);
                                            return;
                                        }else {

                                            final String filename= UUID.randomUUID().toString()+".jpg";

                                            Picasso.get().load(array_class.arrayurl.get(index).getImage()).into(new settingimagehelper(setwallpaper.this,
                                                    getApplicationContext().getContentResolver()
                                                    ,filename,"image desc"));
                                        }
                                        Toast.makeText(setwallpaper.this, "downloaded", Toast.LENGTH_SHORT).show();
                                        break;

                                }
                            }
                        });
                builder.create().show();


            }
        });



        setResult(RESULT_OK,intent);

    }

/*
    private void setimage() {

        Picasso.get()
                .load(array_class.arrayurl.get(index).getImage())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
                        // Save the bitmap or do something with it here
                       // progressBar.setVisibility(View.VISIBLE);
                        //Set it in the ImageView
                        WallpaperManager wallpaperManager = WallpaperManager.getInstance(setwallpaper.this);
                        try {
                            wallpaperManager.setBitmap(bitmap);
                         //   progressBar.setVisibility(View.GONE);
                           // Toast.makeText(setwallpaper.this, "wallpaper set as background", Toast.LENGTH_SHORT).show();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                      //  progressBar.setVisibility(View.VISIBLE);
                    }
                });

    }

 */

   /*
    private void setlockimage() {

        Picasso.get()
        .load(array_class.arrayurl.get(index).getImage())
        .into(new Target() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onBitmapLoaded (final Bitmap bitmap, Picasso.LoadedFrom from){
       // Save the bitmap or do something with it here
        //progressBar.setVisibility(View.VISIBLE);
        //Set it in the ImageView
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(setwallpaper.this);
        try {
        wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
        //progressBar.setVisibility(View.GONE);
        //Toast.makeText(setwallpaper.this, "wallpaper set as background", Toast.LENGTH_SHORT).show();
        } catch (IOException ex) {
        ex.printStackTrace();
        }

        }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
       // progressBar.setVisibility(View.VISIBLE);
        }
        });
    }
*/




  public class settask extends AsyncTask<String,Void, Bitmap> {



      @Override
        protected Bitmap doInBackground(String... strings)
        {

            Bitmap bitmap1= null;
            try {
                bitmap1 = Picasso.get().load(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(setwallpaper.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
           // WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
            WallpaperManager wallpaperManager=(WallpaperManager) getSystemService(Context.WALLPAPER_SERVICE);
            //wallpaperManager.setWallpaperOffsetSteps(1,1);
            //Toast.makeText(setwallpaper.this, "hagaggaga", Toast.LENGTH_SHORT).show();

            try {
                wallpaperManager.setWallpaperOffsetSteps(0,0);
                wallpaperManager.setBitmap(bitmap);

                progressDialog.dismiss();
                Toast.makeText(setwallpaper.this, "wallpaper has been set", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class settask2 extends AsyncTask<String,Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings)
        {

            Bitmap bitmap1= null;
            try {
                bitmap1 = Picasso.get().load(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap1;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(setwallpaper.this);
            progressDialog.setMessage("please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(getBaseContext());
           // Toast.makeText(setwallpaper.this, "hagaggaga", Toast.LENGTH_SHORT).show();

            try {
                wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK);
                progressDialog.dismiss();
                Toast.makeText(setwallpaper.this, "wallpaper set as lockscreen", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}

