package com.companyproject.fujitsu.editor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Verified extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ProgressDialog pDialog;
    private List<Movie> movieList = new ArrayList<Movie>();
    private ListView listView;
    private CustomAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verified);

        listView = (ListView) findViewById(R.id.listvery);
        adapter = new CustomAdapter(Verified.this, movieList);
        listView.setAdapter(adapter);


        pDialog = new ProgressDialog(Verified.this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        populatedata();
        listView.setOnItemClickListener(this);


    }

    public void populatedata(){

        final String url = "http://api.minews.in/slimapp/public/api/posts/approved";

        JsonArrayRequest movieReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("rr000", response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);

                                Movie movie = new Movie();

                                String imagestr = obj.getString("image");
                                String imagrurl = "http://minews.in/storage/app/public/uploads/";
                                String imageurlfull = imagrurl+imagestr;


                                movie.setTitle(obj.getString("headline"));
                                movie.setThumbnailUrl(imageurlfull);
                                movie.setRating(obj.getString("content"));

                                movie.setYear(obj.getString("category"));
                                movie.setGenre(obj.getString("created_at"));
                                movie.setId(obj.getString("id"));

                                // Genre is json array  (Number) obj.get("type")
//                                JSONArray genreArry = obj.getJSONArray("created");
//                                ArrayList<String> genre = new ArrayList<String>();
//                                for (int j = 0; j < genreArry.length(); j++) {
//                                    genre.add((String) genreArry.get(j));
//                                }
//                                movie.setGenre(genre);

                                // adding movie to movies array
                                movieList.add(movie);
                                Log.d("valu123",movieList.toString());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  Log.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });{

            RequestQueue requestQueue = Volley.newRequestQueue(Verified.this);
            requestQueue.add(movieReq);
        }

        // Adding request to request queue




    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Movie mo123 = (Movie) parent.getItemAtPosition(position);

        Intent newsdetailintnt = new Intent(this,NewsDetail.class);
        newsdetailintnt.putExtra("type",mo123.getYear());
        newsdetailintnt.putExtra("headline",mo123.getTitle());
        newsdetailintnt.putExtra("content",mo123.getRating());
        newsdetailintnt.putExtra("image",mo123.getThumbnailUrl());
        newsdetailintnt.putExtra("id",mo123.getId());

        //  newsdetailintnt.putExtra("caption",movie.);
        startActivity(newsdetailintnt);


    }


}
