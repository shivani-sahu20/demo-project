package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.framed.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TextView name,year1;
    ImageView image;
    AutoCompleteTextView autocomplete;
    List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=findViewById(R.id.name);
        autocomplete=findViewById(R.id.autoCompleteTextView);
        year1=findViewById(R.id.year);
        image=findViewById(R.id.image);

        ArrayAdapter<String> autoAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, data);
        autocomplete.setAdapter(autoAdapter);

        autocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieve data s
            }

            @Override
            public void afterTextChanged(Editable s) {
                retrieveData(s);
                autoAdapter.notifyDataSetChanged();
            }
        });


        new getData("http://www.omdbapi.com/?i=tt3896198&apikey=8a2ab032").execute();

    }

    private void retrieveData(Editable s)
    {
        String text = s.toString();
        if(text.contains(" "))
        {
            text.replace(" ", "%20");
        }
        String url = "http://www.omdbapi.com/?Title="+text+"&i=tt3896198&apikey=8a2ab032";
        new getData(url).execute();
    }


    public class getData extends AsyncTask<String, String, String>
    {
        String url1=null;
        public getData(String url) {
            this.url1=url;
        }
            @Override
            public void onPreExecute() {
                super .onPreExecute();
            }
            @Override
            protected String doInBackground(String... params) {
                String result = null;
                try {
                    URL url = new URL(url1);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();

                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
                        BufferedReader reader = new BufferedReader(inputStreamReader);
                        StringBuilder stringBuilder = new StringBuilder();
                        String temp;

                        while ((temp = reader.readLine()) != null) {
                            stringBuilder.append(temp);
                        }
                        result = stringBuilder.toString();
                    }else  {
                        result = "error";
                    }

                } catch (Exception  e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void onPostExecute(String s) {
                super .onPostExecute(s);
                try {

                    JSONObject jsonObject = new JSONObject(s);
                    String title = jsonObject.getString("Title");
                    String year = jsonObject.getString("Year");
                    String poster = jsonObject.getString("Poster");

                   name.setText(title);
                   year1.setText(year);
                   Picasso.with(MainActivity.this).load(poster).into(image);

                   data.add(title);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
    }
}