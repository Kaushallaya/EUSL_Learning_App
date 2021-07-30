package com.thcreation.euslkuppiya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class YearActivity extends AppCompatActivity {

    String subCode="";

    String type,id,viewURL;

    String paperUrl ="0";
    String assUrl = "0";
    String noteUrl= "0";

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        subCode = bundle.getString("subCode");
        type = bundle.getString("type");

        lv = findViewById(R.id.lvYear);

        //Toast.makeText(this, "sub code = "+subCode+" type = "+type, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadYear();
    }

    public void loadYear(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/year/";
        System.out.println(url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        setYear(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(request);
    }

    public void setYear(JSONArray response){

        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        try {
            for(int i=0; i<response.length(); i++){

                JSONObject obj = response.getJSONObject(i);

                HashMap<String,String> map = new HashMap<>();

                map.put("id",obj.getString("id"));
                map.put("year",obj.getString("year"));
                list.add(map);
            }

            int layout = R.layout.item_year;
            int[] views = {R.id.Yid,R.id.tvYear};
            String[] colums = {"id","year"};

            SimpleAdapter adapter = new SimpleAdapter(this,list,layout,colums,views);
            lv.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goViewer(View v){

        TextView tv = v.findViewById(R.id.Yid);
        id = tv.getText().toString();

        //Toast.makeText(this, "id = "+id, Toast.LENGTH_SHORT).show();
        loadViewer();
    }

    public void loadViewer(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/pastpapers/index.php?subCode="+subCode+"&yId="+id+"";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        setView(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(request);
    }

    public void setView(JSONArray response){

        try {

            JSONObject obj = response.getJSONObject(0);
            paperUrl = obj.getString("paper_url");
            assUrl = obj.getString("ass_url");
            noteUrl = obj.getString("note_url");
            //Toast.makeText(this, "purl = "+paperUrl+" ass = "+assUrl+noteUrl, Toast.LENGTH_LONG).show();

            switch (type) {
                case "Past Papers": viewURL = paperUrl; break;
                case "Assignments": viewURL = assUrl; break;
                case "Lecture Notes": viewURL = noteUrl; break;
            }

            callViewer(viewURL);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void  callViewer(String str){
        Intent intent = new Intent(this,PDFActivity.class);
        intent.putExtra("url",str);
        startActivity(intent);
    }
}