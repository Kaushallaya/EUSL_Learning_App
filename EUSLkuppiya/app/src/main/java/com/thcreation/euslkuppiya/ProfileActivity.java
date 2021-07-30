package com.thcreation.euslkuppiya;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    String uname="";
    String ufname="";
    String udname="";
    String uregnum="";
    String uinnum="";
    String uri="";

    EditText name,faculty,degree,regnum,index;

    SharedPreferences sharedPref;

    private  View rootView;

    ImageView proImg;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.uname);
        faculty = findViewById(R.id.ufname);
        degree = findViewById(R.id.udname);
        regnum = findViewById(R.id.urnumber);
        index = findViewById(R.id.uinnum);

        proImg = findViewById(R.id.iv_profile);

        sharedPref = getApplicationContext().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        rootView = findViewById(R.id.view_root);

        dialog = new Dialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String regNo = sharedPref.getString("regNo","0");
        String uriTem = sharedPref.getString("uri","0");
        int avtr = sharedPref.getInt("avtr",R.drawable.pro1);

        proImg.setImageResource(avtr);

        Toast.makeText(ProfileActivity.this, "uri="+uriTem, Toast.LENGTH_SHORT).show();
        //proImg.setImageURI(Uri.parse(uriTem));
        loadUser(regNo);
    }

    public void loadUser(String regNo){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/user/index.php?regNo="+regNo+"";
        System.out.println("url = "+url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response.length() <= 0){
                            //Toast.makeText(SubjectActivity.this, "respnce nul!!!!", Toast.LENGTH_SHORT).show();
                           // setConsDialog();

                        }else{
                            setUser(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        //setConsDialog();
                        //Toast.makeText(ProfileActivity.this, "respnce nul!!!!", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

//    public void setConsDialog(){
//        dialog.setContentView(R.layout.dialog_cons_layout);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        dialog.show();
//    }

    public void setUser(JSONArray response){

        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        try {
                JSONObject obj = response.getJSONObject(0);

            name.setText(obj.getString("name"));
            faculty.setText(obj.getString("faculty"));
            degree.setText(obj.getString("degree"));
            regnum.setText(obj.getString("regNo"));
            index.setText(obj.getString("indexNo"));
            proImg.setImageURI(Uri.parse(obj.getString("img")));



        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void save(View view) {

        final String uname = name.getText().toString();
        final String ufaculty = faculty.getText().toString();
        final String udegree = degree.getText().toString();
        final String uregNo = regnum.getText().toString();
        final String uindex = index.getText().toString();

        SharedPreferences.Editor editor = sharedPref.edit();                        //save on shared preference
        editor.putString("regNo", uregNo);
        editor.putString("uri",uri);
        editor.putString("name",uname);
        editor.apply();

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/user/insert.php";
        StringRequest request = new StringRequest(Request.Method.POST,url,this,this){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> params = new HashMap<>();
                params.put("name",uname);
                params.put("faculty",ufaculty);
                params.put("degree",udegree);
                params.put("regNo",uregNo);
                params.put("indexNo",uindex);
                params.put("img",uri);

                return params;
            }
        };
        queue.add(request);
    }

    @Override
    public void onResponse(String response) {
        //Toast.makeText(this, "responce = "+response, Toast.LENGTH_SHORT).show();
        Snackbar.make(rootView,"SAVED SUCCESSFULLY...!",Snackbar.LENGTH_LONG)
                .setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        error.printStackTrace();
        //Toast.makeText(this, "error = "+error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    public void changePropic(View view) {
        setAvtDialog();
        //Intent oprnGalary = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //startActivityForResult(oprnGalary,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000){
            if(resultCode == Activity.RESULT_OK){
                assert data != null;
                Uri imgUri = data.getData();
                proImg.setImageURI(imgUri);

                assert imgUri != null;
                uri = imgUri.toString();
                //Toast.makeText(this, "uri- "+imgUri, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void setAvtDialog(){
        dialog.setContentView(R.layout.dialog_avater_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setPro(View view) {

        int avtrno;
        switch (view.getId()){
            case R.id.pro1: proImg.setImageResource(R.drawable.pro1); avtrno = R.drawable.pro1; break;
            case R.id.pro2: proImg.setImageResource(R.drawable.pro2); avtrno = R.drawable.pro2;break;
            case R.id.pro3: proImg.setImageResource(R.drawable.pro3); avtrno = R.drawable.pro3;break;
            case R.id.pro4: proImg.setImageResource(R.drawable.pro4); avtrno = R.drawable.pro4;break;
            case R.id.pro5: proImg.setImageResource(R.drawable.pro5); avtrno = R.drawable.pro5;break;
            case R.id.pro6: proImg.setImageResource(R.drawable.pro6); avtrno = R.drawable.pro6;break;
            case R.id.pro7: proImg.setImageResource(R.drawable.pro7); avtrno = R.drawable.pro7;break;
            case R.id.pro8: proImg.setImageResource(R.drawable.pro8); avtrno = R.drawable.pro8;break;
            default: proImg.setImageResource(R.drawable.ic_person); avtrno = R.drawable.ic_person;break;

        }
        SharedPreferences.Editor editor = sharedPref.edit();                        //save on shared preference
        editor.putInt("avtr", avtrno);
        editor.apply();

        dialog.dismiss();

    }

    public void closeDialog(View view) {
        dialog.dismiss();
    }
}