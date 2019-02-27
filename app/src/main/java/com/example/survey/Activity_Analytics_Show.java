package com.example.survey;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Activity_Analytics_Show extends AppCompatActivity {
    String json;
    String answer;
    private String DIR_PATH;
    private String name, desc, js, author, JsonAnswer, JsonForm;
    private LinearLayout parentLinearLayout;
    private ArrayList<JSONObject> list;
    private ArrayList<String> keys;
    private ArrayList<String> questionList;
    private ArrayList<String> typeList;
    private ArrayList<String[]> answerList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__analytics__show);
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp/"; //PATH OF Internal STORAGE FOR FORM and JSON

        keys = new ArrayList<>();
        questionList = new ArrayList<>();
        typeList = new ArrayList<>();
        answerList = new ArrayList<>();
        list = new ArrayList<>();



        Intent intent = getIntent();
        JsonForm = intent.getStringExtra("json");
        name = intent.getStringExtra("name");
        JsonAnswer = intent.getStringExtra("Answer");

        Log.v("TEST123", JsonAnswer+"\n"+JsonForm);

        try {
            JSONObject jsonObject = new JSONObject(JsonForm);
            name = jsonObject.getString("title");
            desc = jsonObject.getString("description");
            JSONArray question =jsonObject.getJSONArray("type");

            Log.v("TEST1234", question.toString());

            for(int i = 0; i < question.length(); i++){
                JSONObject obj = question.getJSONObject(i);
                String ques = obj.getString("question");
                String tp = obj.getString("type");

                typeList.add(tp);
                questionList.add(ques);
            }
            Log.v("TEST12345", questionList+"\n"+typeList.toString());

            JSONArray jsonArray = new JSONArray(JsonAnswer);
            for(int i =0; i< jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                int t = i+1;
                JSONArray arr = obj.getJSONArray(""+t);
                String[] value = new String[arr.length()];
                for(int x=0; x<arr.length();x++){
                    value[x] = arr.getJSONObject(x).getString(typeList.get(x));
                }
                answerList.add(value);
                ToCSV();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void ToCSV() throws IOException {
       StringBuilder str = new StringBuilder();
       for(int i = 0;i < questionList.size();i++){
           str.append(questionList.get(i));
           if(i != questionList.size()-1){
               str.append(",");
           }else{
               str.append("\n");
           }
       }

       for(int i = 0;i < answerList.size(); i++){
           String[] temp = answerList.get(i);
           for(int j = 0;j < temp.length;j++){
               str.append(temp[j]);
               if(j != temp.length-1){
                   str.append(",");
               }
           }
           str.append("\n");
       }

        Log.v("CSV", str.toString());
        File file = new File(DIR_PATH, name + ".csv");

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(str.toString().getBytes());
        fos.close();

    }
}
