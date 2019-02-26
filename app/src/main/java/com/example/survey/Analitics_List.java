package com.example.survey;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Analitics_List extends AppCompatActivity {

    JSONArray jsonArray;
    ArrayList<JSONArray> dataset;
    ArrayList<String> answer;
    String name, DIR_PATH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analitics__list);
        ListView lv =  findViewById(R.id.analytics_list);
        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        name = intent.getStringExtra("name");
        Log.v("qwerty", json);
        answer = new ArrayList<>();
        dataset = new ArrayList<>();
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON




        try {
            jsonArray = new JSONArray(json);

            for(int i=0; i<jsonArray.length();i++){
                JSONObject j = jsonArray.getJSONObject(i);
                Log.v("listlist", j.toString());
                int t = i+1;
                JSONArray arr = j.getJSONArray(""+t);
                Log.v("listlist", arr.toString());
                dataset.add(arr);
                JSONObject item = arr.getJSONObject(0);

                Iterator<String> keys = item.keys();
                String str_Name=keys.next();
                Log.v("listlist", " "+ str_Name + " " + item.getString(str_Name));

                String tet = t+ ") " +item.getString(str_Name);
                answer.add(tet);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_list_item_1,
                    answer);
            Log.v("listlist", answer.size()+" "+answer.toString());
            lv.setAdapter(arrayAdapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File file = new File(DIR_PATH+"/"+name+"_Form.json");
                    Intent intent =new Intent(Analitics_List.this, Analytics_Filled.class);

                    StringBuilder json = new StringBuilder();
                    BufferedReader br = null;
                    try {
                        br = new BufferedReader(new FileReader(file));

                        String line;

                        while ((line = br.readLine()) != null) {
                            json.append(line);
                        }
                        br.close();

                        Log.v("READFILE", String.valueOf(json));
                        intent.putExtra("json", String.valueOf(json));
                        intent.putExtra("Answer", dataset.get(position).toString());
                        intent.putExtra("name", name);
                        intent.putExtra("description", new JSONObject(json.toString()).getString("description"));
                        startActivity(intent);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }

                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
