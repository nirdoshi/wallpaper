package com.napps.wallpaper;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class settingimagehelper implements Target {

   // private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private String desc;
    private Context context;

    public settingimagehelper(Context context,ContentResolver contentResolver, String name, String desc) {
      //  this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
        this.context=context;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

        ContentResolver r=contentResolverWeakReference.get();
        //AlertDialog dialog=alertDialogWeakReference.get();

        if(r!=null){
            MediaStore.Images.Media.insertImage(r,bitmap,name,desc);
        }
        //dialog.dismiss();

       /* Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
            context.startActivity(Intent.createChooser(intent,"VIEW PICTURE"));


        */
        //Toast.makeText(context, "Image downloaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
