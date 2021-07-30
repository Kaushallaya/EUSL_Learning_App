package com.thcreation.euslkuppiya;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SubjectActivity extends AppCompatActivity {

    String type,dsCode;

    TextView tvtype;

    ListView lv;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        tvtype = findViewById(R.id.tvType);
        lv = findViewById(R.id.lvSub);

        type=getIntent().getStringExtra("type");
        dsCode=getIntent().getStringExtra("dsCode");

        dialog = new Dialog(this);

        tvtype.setText(type);

        //Toast.makeText(this, "type = "+type+" dsCode = "+dsCode+"", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSubject();
    }

    public void loadSubject(){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://beezzserver.com/slthadi/projectEUSL/subject/index.php?dsCode="+dsCode+"";
        System.out.println(url);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        if(response.length() <= 0){
                            //Toast.makeText(SubjectActivity.this, "respnce nul!!!!", Toast.LENGTH_SHORT).show();
                            setConsDialog();

                        }else{
                            setSubject(response);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        setConsDialog();
                        Toast.makeText(SubjectActivity.this, "respnce nul!!!!", Toast.LENGTH_SHORT).show();
                    }
                });
        queue.add(request);
    }

    public void setConsDialog(){
        dialog.setContentView(R.layout.dialog_cons_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void setSubject(JSONArray response){

        List<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        try {
            for(int i=0; i<response.length(); i++){

                JSONObject obj = response.getJSONObject(i);

                HashMap<String,String> map = new HashMap<>();

                map.put("id",obj.getString("id"));
                map.put("subject",obj.getString("subject"));
                map.put("code",obj.getString("subCode"));
                list.add(map);
            }

            int layout = R.layout.item_subject2;
            int[] views = {R.id.tvCode,R.id.tvSubject};
            String[] colums = {"code","subject"};

            SimpleAdapter adapter = new SimpleAdapter(this,list,layout,colums,views);
            lv.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void goYear(View view){

        ConstraintLayout ll = (ConstraintLayout) view.getParent();
        TextView tv = ll.findViewById(R.id.tvCode);

        String subCode = tv.getText().toString();

        Intent intent = new Intent(this,YearActivity.class);
        intent.putExtra("subCode",subCode);
        intent.putExtra("type",type);
        startActivity(intent);
    }

    public void goBack(View v){
        super.onBackPressed();
    }
}