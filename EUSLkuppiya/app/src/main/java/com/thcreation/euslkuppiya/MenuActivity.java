package com.thcreation.euslkuppiya;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    TextView faculty,department;
    String fac,deg,sem,dsCode;

    Dialog dialog;

    private  View rootView;

    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        faculty=findViewById(R.id.facID);
        department = findViewById(R.id.depID);
        rootView = findViewById(R.id.view_root);

        dialog = new Dialog(this);

        fac=getIntent().getStringExtra("fac");
        deg=getIntent().getStringExtra("deg");
        sem=getIntent().getStringExtra("sem");

        sharedPref = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        loadCode();
    }

    public void  loadCode(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/dsCode/index.php?fac="+fac+"&deg="+deg+"&sem="+sem+"";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        setCode(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        setErrDialog();
                    }
                });

        queue.add(request);
    }

    public void setErrDialog(){
        dialog.setContentView(R.layout.dialog_error_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setCode(JSONArray response){

        try {

            JSONObject obj = response.getJSONObject(0);
            dsCode = obj.getString("dsCode");

            //Snackbar.make(rootView,"result =  "+dsCode,3000).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void timeTable(View view){
        Intent intent = new Intent(this,PDFActivity.class);
        startActivity(intent);
    }


    public void pastPaper(View view) {
        Intent intent = new Intent(this,SubjectActivity.class);
        intent.putExtra("type","Past Papers");
        intent.putExtra("dsCode",dsCode);
        startActivity(intent);
    }

    public void assignment(View view) {
        Intent intent = new Intent(this,SubjectActivity.class);
        intent.putExtra("type","Assignments");
        intent.putExtra("dsCode",dsCode);
        startActivity(intent);
    }

    public void lecNote(View view) {
        Intent intent = new Intent(this,SubjectActivity.class);
        intent.putExtra("type","Lecture Notes");
        intent.putExtra("dsCode",dsCode);
        startActivity(intent);
    }


    public void goBack(View view) {
        Intent intent = new Intent(this,HomeActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();

        faculty.setText(fac+" Faculty");
        department.setText(deg+" Department");

    }

    public void startChatroom(View view) {
        Intent in = new Intent(this,ChatroomActivity.class);
        startActivity(in);
    }
}