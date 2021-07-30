package com.thcreation.euslkuppiya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private Snackbar snackbar;
    private  View rootView;

    SharedPreferences sharedPref;

    ImageView proImg;

    AutoCompleteTextView actvFaculty,actvDegree,actvSem;
    Map<String,String> Faculty = new HashMap<>();
    Map<String,String> Degree = new HashMap<>();
    Map<String,String> Sem = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        proImg = findViewById(R.id.ivPro);

        actvFaculty = findViewById(R.id.faculty);
        actvDegree = findViewById(R.id.degree);
        actvSem = findViewById(R.id.sem);

        rootView = findViewById(R.id.view_root);

        sharedPref = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
    }


    public void submit(View view) {
         String fac = actvFaculty.getText().toString();
         String deg = actvDegree.getText().toString();
         String sem = actvSem.getText().toString();

         if(fac.equals("")){
             Snackbar.make(rootView,"Faculty Name can not be empty",3000).show();
         }else if(deg.equals("")){
             Snackbar.make(rootView,"Degree Name can not be empty",3000).show();
         }else if(sem.equals("")){
             Snackbar.make(rootView,"Semester can not be empty",3000).show();
         }else {
             Intent in = new Intent(this, MenuActivity.class);
             in.putExtra("fac", fac);
             in.putExtra("deg", deg);
             in.putExtra("sem", sem);
             startActivity(in);
         }
    }

    public void adminPanel(View view) {

        Intent in = new Intent(this,AboutActivity.class);
        startActivity(in) ;
    }

    public void profile(View view) {
        Intent in = new Intent(this,ProfileActivity.class);
        startActivity(in) ;
    }

    @Override
    protected void onResume() {
        super.onResume();

        int avtr = sharedPref.getInt("avtr",R.drawable.ic_person);
        proImg.setImageResource(avtr);

        loadFaculty();
        loadDegree();
        loadSemester();
    }

    public void loadFaculty(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/faculty/";

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                setFaculty(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error = "+error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void setFaculty(JSONArray response){

        List<String> list = new ArrayList<>();
        for(int i=0; i<=response.length();i++)
        {

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("fName"));

                Faculty.put(obj.getString("fName"),obj.getString("id"));


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this,layout,list);
        actvFaculty.setAdapter(adapter);

    }

    public void loadDegree(){
        String temfac = actvFaculty.getText().toString();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/degree/";

        //String url = "http://beezzserver.com/slthadi/projectEUSL/degree/index.php?temfac="+temfac+"";

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                setDegree(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error = "+error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void setDegree(JSONArray response){

        List<String> list = new ArrayList<>();
        for(int i=0; i<response.length();i++)
        {

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("dName"));

                Degree.put(obj.getString("dName"),obj.getString("id"));

                System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!RESPONCE = "+obj.getString("dName"));


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this,layout,list);
        actvDegree.setAdapter(adapter);

    }

    public void loadSemester(){

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/semester/";

        JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                setSemester(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error = "+error.getMessage());
            }
        });

        queue.add(jsonArrayRequest);
    }

    public void setSemester(JSONArray response){

        List<String> list = new ArrayList<>();
        for(int i=0; i<=response.length();i++)
        {

            try {
                JSONObject obj = response.getJSONObject(i);
                list.add(obj.getString("sName"));

                Sem.put(obj.getString("sName"),obj.getString("id"));


            }catch (Exception e){
                e.printStackTrace();
            }
        }

        int layout = android.R.layout.simple_list_item_1;
        ArrayAdapter adapter = new ArrayAdapter(this,layout,list);
        actvSem.setAdapter(adapter);
    }
}