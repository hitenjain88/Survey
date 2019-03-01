package com.example.survey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
    private Button btn_save_excel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__analytics__show);
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp/"; //PATH OF Internal STORAGE FOR FORM and JSON
        parentLinearLayout = findViewById(R.id.activity_analytics_show_linear_layout);
        keys = new ArrayList<>();
        questionList = new ArrayList<>();
        typeList = new ArrayList<>();
        answerList = new ArrayList<>();
        list = new ArrayList<>();
        btn_save_excel = findViewById(R.id.btn_save_excel);


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
                list.add(obj);
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
                //ToCSV();
            }

            for(int i = 0;i<typeList.size();i++){
                switch (typeList.get(i)){
                    case "EditText" : EditTextAnalytics(i);
                    break;
                    case "MultiText" : MultiTextAnalytics(i);
                    break;
                    case "RadioGroup" : RadioButtonAnalytics(i);
                    break;
                    case "CheckBox" : CheckBoxAnalytics(i);
                    break;

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btn_save_excel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ToCSV();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void CheckBoxAnalytics(int position) throws JSONException {
        TextView tv = new TextView(this);
        tv.setText(questionList.get(position));
        tv.setTextSize(20);
        tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextColor(Color.WHITE);
        final ArrayList<String> items = new ArrayList<>();
        for(int i = 0;i < answerList.size();i++){
            String temp = answerList.get(i)[position].replace("|", ",");
            items.add('"' + temp.substring(0,temp.length()-3)+ '"');
        }
        Log.v("12345678", items.toString());

        JSONObject obj = list.get(position);
        JSONArray arr = obj.getJSONArray("group");
        final ArrayList<String> itemsQuestion = new ArrayList<>();
        for(int i = 0;i< arr.length();i++){
            itemsQuestion.add(arr.getJSONObject(i).getString("title"));
        }
        Log.v("123456", itemsQuestion.toString());
    }

    private void RadioButtonAnalytics(int position) throws JSONException {
        TextView tv = new TextView(this);
        tv.setText(questionList.get(position));
        tv.setTextSize(20);
        tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextColor(Color.WHITE);
        final ArrayList<String> items = new ArrayList<>();
        for(int i = 0;i < answerList.size();i++){
            items.add(answerList.get(i)[position]);
        }
        //Log.v("12345678", items.toString());
        JSONObject obj = list.get(position);
        JSONArray arr = obj.getJSONArray("group");
        final ArrayList<String> itemsQuestion = new ArrayList<>();
        for(int i = 0;i< arr.length();i++){
            itemsQuestion.add(arr.getJSONObject(i).getString("title"));
        }
        Log.v("123456", itemsQuestion.toString());
    }

    private void MultiTextAnalytics(int position) {
        TextView tv = new TextView(this);
        tv.setText(questionList.get(position));
        tv.setTextSize(20);
        tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextColor(Color.WHITE);
        final ArrayList<String> items = new ArrayList<>();
        for(int i = 0;i < answerList.size();i++){
            items.add(answerList.get(i)[position]);

        }
        ListView lv = new ListView(this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                items);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT);
        params.height=items.size()*100;
        lv.setLayoutParams(params);

        lv.setAdapter(arrayAdapter);
        parentLinearLayout.addView(tv);
        parentLinearLayout.addView(lv);

    }

    public void EditTextAnalytics(int position){
        TextView tv = new TextView(this);
        tv.setText(questionList.get(position));
        tv.setTextSize(20);
        tv.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        tv.setTextColor(Color.WHITE);
        final ArrayList<String> items = new ArrayList<>();
        for(int i = 0;i < answerList.size();i++){
            items.add(answerList.get(i)[position]);

        }
        ListView lv = new ListView(this);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                items);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT, AbsListView.LayoutParams.FILL_PARENT);
        params.height=items.size()*100;
        lv.setLayoutParams(params);

        lv.setAdapter(arrayAdapter);
        parentLinearLayout.addView(tv);
        parentLinearLayout.addView(lv);

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
        File file = new File(DIR_PATH+"/Excel", name + ".csv");
        if(!file.exists()){boolean dir_created = file.mkdir(); dir_created = file.createNewFile();}
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(str.toString().getBytes());
        fos.close();
        Toast.makeText(this, "File Created :\n" + file.getAbsolutePath(), Toast.LENGTH_SHORT).show();

    }
}
