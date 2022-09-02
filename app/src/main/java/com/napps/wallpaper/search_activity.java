package com.napps.wallpaper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class search_activity extends AppCompatActivity implements imageAdapter.itemclicked {
    recyclercontent recyclercontent;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);
        String query = getIntent().getStringExtra("query");

        MainActivity.key=107;
        array_class.test.clear();
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.pexels.com/v1/search?query="+query+"&orientation=portrait&per_page=80";
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading");
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
                                array_class.test.add(recyclercontent);
                                //Log.d("test",array_class.trend.get(i).getImage());
                            }

                            progressDialog.dismiss();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new fragment_wallpaper()).commit();
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
    }

    @Override
    public void onitemclicked(int index) {
        Intent intent=new Intent(search_activity.this,setwallpaper.class);
        // String url= array_class.arrayurl.get(index).getImage();
        intent.putExtra("url",index);
        startActivity(intent);
        Toast.makeText(this, "click anywhere", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, @Nullable Intent data) {
        setwallpaper.handle=false;
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
}