package com.napps.wallpaper;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.viewholder> implements  Filterable{
    private ArrayList<recyclercontent> arrayurl;
    private ArrayList<recyclercontent> arrayurlALL;
    RequestQueue queue;
    itemclicked activity;
    recyclercontent recyclercontent;

    Filter filter=new Filter() {
        @Override
        //background thread;
        protected FilterResults performFiltering(CharSequence charSequence) {

            List<recyclercontent> filteredList=new ArrayList<>();

            Log.d("char",charSequence.toString());

            if(charSequence.toString().isEmpty()){

                filteredList.addAll(arrayurl);

                Log.d("test",arrayurlALL.size()+"");
                MainActivity.skey=69;

            }else{

                String url = "https://api.pexels.com/v1/search?query="+charSequence+"&orientation=portrait&per_page=80";
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

                                        filteredList.add(recyclercontent);

                                        notifyDataSetChanged();
                                        //  Log.d("test",array_class.trend.get(i).getImage());
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
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


               // filteredList.addAll(array_class.bikes);
            }
//            List<recyclercontent> filteredList=new ArrayList<>();
//            if(charSequence.toString().isEmpty()){
//                filteredList.addAll(arrayurlALL);
//            }else{
//                for(recyclercontent obj:arrayurlALL){
//                    if(obj.getSearch().toLowerCase().contains(charSequence.toString().toLowerCase())){
//                        filteredList.add(obj);
//                    }
//                }
//            }

            FilterResults filterResults=new FilterResults();
            filterResults.values=filteredList;
            filterResults.count= filteredList.size();
            return filterResults;
        }

        @Override
        //ui thread;
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            Log.d("char1",charSequence.toString());
            if(charSequence.toString().isEmpty()){
                arrayurl = arrayurlALL;
                Log.d("test1",arrayurlALL.size()+"");
            }else {
               // arrayurl.clear();
                arrayurl= (ArrayList<com.napps.wallpaper.recyclercontent>) filterResults.values;
                Log.d("test2","some value");
                MainActivity.skey = 106;
                array_class.test=arrayurl;
           }

            notifyDataSetChanged();
            // arrayurl.addAll((Collection<? extends recyclercontent>) filterResults.values);


        }
    };



    @Override
    public Filter getFilter() {
        return filter;
    }


    public interface itemclicked {

        public void onitemclicked(int index);
    }


    public imageAdapter(Context context, ArrayList<recyclercontent> list) {

        arrayurl = list;
        arrayurlALL=list;
        activity = (itemclicked) context;
        queue = Volley.newRequestQueue(context);
    }

    // method for filtering our recyclerview items.


    public class viewholder extends RecyclerView.ViewHolder {

        ImageView iv;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    activity.onitemclicked(arrayurl.indexOf(v.getTag()));

                }
            });

        }
    }

    @NonNull
    @Override
    public imageAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.images_layout,parent,false);

        return new viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull imageAdapter.viewholder holder, int position) {
        holder.itemView.setTag(arrayurl.get(position));

       Picasso.get().load(arrayurl.get(position).getImage()).fit().into(holder.iv);

    }

    @Override
    public int getItemCount() {
        return arrayurl.size();
    }

}