package com.example.survey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class FormFill extends AppCompatActivity {

    private String DIR_PATH, DIR_PATH_FILL;
    private String name, desc, js, author;
    private JSONArray jsonParent;
    private LinearLayout parentLinearLayout;
    private ArrayList<View> listView;
    private ArrayList<JSONObject> listJson;
    private ArrayList<JSONObject> listFillJson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill);
        DIR_PATH_FILL = Environment.getExternalStorageDirectory() + "/SurveyApp/Data"; //PATH OF Internal STORAGE FOR FORM and JSON

        listView = new ArrayList<>();
        listJson = new ArrayList<>();
        listFillJson = new ArrayList<>();

        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON
        TextView nm = findViewById(R.id.name_filling);
        TextView ds = findViewById(R.id.description_filling);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        parentLinearLayout = findViewById(R.id.parent_layout_filling);


        try {
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.getString("title");
            desc = jsonObject.getString("description");
            int length = name.length();
            name = name.substring(0,1).toUpperCase() + name.substring(1,length);
            nm.setText(name);
            ds.setText(desc);
            JSONArray type = jsonObject.getJSONArray("type");
            Log.v("testing1", type.toString()+"akjflf");
            name = jsonObject.getString("title");
            desc = jsonObject.getString("description");
            //author = jsonObject.getString("author");
            Log.v("testing1",  type.length()+" Length");

            for(int i = 0; i < type.length(); i++){
                String jj = type.getString(i);
                Log.v("testing1", jj + " JJ");
                JSONObject j = new JSONObject(jj);

                Log.v("testing1", j.toString()+"10011");
                switch (j.getString("type")){
                    case "EditText" : SetEditText(j);
                        break;
                    case "RadioGroup" : SetRadioGroup(j);
                        break;
                    case "CheckBox" : SetCheckBox(j);
                        break;
                    case "MultiEditText" : SetMultiEditText(j);
                        break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        try {
            FetchAllAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        super.onBackPressed();
    }

    private void SetMultiEditText(JSONObject j) throws JSONException {
        String question =   j.getString("question");
        final JSONObject obj = j;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_fill_multiline, null);
        final TextView textView = rowView.findViewById(R.id.field_fill_multi_question);
        textView.setText(question);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        listView.add(rowView);
        listJson.add(j);
    }

    private void SetCheckBox(JSONObject j) {
        try {
            final JSONObject jsonObj = j;
            String question = jsonObj.get("question").toString();
            JSONArray jsonArray = jsonObj.getJSONArray("group");

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field_fill_checkbox, null);

            final TextView textView = rowView.findViewById(R.id.field_fill_check_question);
            textView.setText(question);
            LinearLayout ll = rowView.findViewById(R.id.field_fill_check_linear_layout);

            int len = jsonArray.length();

            for(int i=0; i<len; i++)
            {
                JSONObject o = jsonArray.getJSONObject(i);
                String title = o.getString("title");
                final CheckBox cb = new CheckBox(FormFill.this);
                cb.setText(title);
                cb.setTextColor(Color.rgb(0, 0, 0));
                ll.addView(cb);
            }

            listView.add(rowView);
            listJson.add(j);
            parentLinearLayout.addView(rowView);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetRadioGroup(JSONObject j) {

        try {
            final JSONObject jsonObj = j;
            String question = jsonObj.get("question").toString();
            JSONArray jsonArray = jsonObj.getJSONArray("group");

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field_fill_radio, null);

            final TextView textView = rowView.findViewById(R.id.field_fill_radio_question);
            textView.setText(question);
            RadioGroup rg = rowView.findViewById(R.id.field_fill_radio_group);

            int len = jsonArray.length();

            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

            for(int i=0; i<len; i++)
            {
                JSONObject o = jsonArray.getJSONObject(i);
                String title = o.getString("title");
                final RadioButton radioButton = new RadioButton(FormFill.this);
                radioButton.setText(title);
                radioButton.setTextColor(Color.rgb(0, 0, 0));
                rg.addView(radioButton, params);
            }

            listView.add(rowView);
            listJson.add(j);
            parentLinearLayout.addView(rowView);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetEditText(JSONObject j) throws JSONException {
        String question=j.getString("question");
        boolean imag = j.getBoolean("image");
        final JSONObject object = j;
        Log.v("testing1", j.toString());

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_fill_edittext, null);
        Button image_answer = rowView.findViewById(R.id.btn_add_image_field_edit_text_fill);
        final TextView textView = rowView.findViewById(R.id.field_fill_edit_text);
        if(imag){
            image_answer.setVisibility(View.VISIBLE);

        }
        textView.setText(question);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        listView.add(rowView);
        listJson.add(j);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void FetchAllAnswer() throws JSONException, IOException {

        for(int i = 0; i < listView.size(); i++){
            View v = listView.get(i);
            JSONObject jsonObj = listJson.get(i);

            switch(jsonObj.getString("type")) {
                case "EditText":
                    fetchEditText(v, jsonObj);
                    break;
                case "MultiText":
                    fetchMultiText(v, jsonObj);
                    break;
                case "RadioGroup":
                    fetchRadioGroup(v, jsonObj);
                    break;
                case "CheckBox":
                    fetchCheckBox(v, jsonObj);
                    break;

            }
        }
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i  < listFillJson.size(); i++){
            jsonArray.put(i, listFillJson.get(i));
        }
        JSONObject js = new JSONObject();
        String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

        File file = new File(DIR_PATH_FILL);
        File fileWrite;
        if(!file.exists()){
            boolean created = file.mkdir();
        }
        fileWrite = new File(DIR_PATH_FILL+"/"+name.toLowerCase()+".json");
        Log.d("Test1", String.valueOf(fileWrite.exists()));

        if(!fileWrite.exists()){
            boolean created = fileWrite.createNewFile();
            JSONArray arr = new JSONArray();
            //int length = arr.length();
            for(int i = 0; i < listFillJson.size(); i++){
                arr.put(i, listFillJson.get(i));
            }
            jsonParent = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("1", arr);

            jsonParent.put(0, obj);
            Log.v("sts", jsonParent.toString());

            File file2 = new File(DIR_PATH_FILL+"/"+ name + ".json");

            FileOutputStream fos = new FileOutputStream(file2);
            fos.write(jsonParent.toString().getBytes());
            fos.close();
        }
        else{
            StringBuilder json = new StringBuilder();
            BufferedReader br = null;

            try {
                br = new BufferedReader(new FileReader(new File(DIR_PATH_FILL+"/"+name+".json")));
                String line;
                while ((line = br.readLine()) != null) {
                    json.append(line);
                }
                br.close();
                Log.v("Test11", json.toString());
                //Toast.makeText(this, json.toString(), Toast.LENGTH_LONG).show();

                jsonParent = new JSONArray(json.toString());
                //Toast.makeText(this, json.toString()+" "+jsonParent2.toString(), Toast.LENGTH_LONG).show();


                JSONArray arr = new JSONArray();
                int length = jsonParent.length();
                for(int i = 0; i < listFillJson.size(); i++){
                    arr.put(i, listFillJson.get(i));
                }
                JSONObject obj = new JSONObject();
                int id = jsonParent.length()+1;
                obj.put(""+id, arr);

                Log.v("Test11", obj.toString());
                jsonParent.put(id-1,obj);
                Log.v("Test11", jsonParent.toString());
                File file2 = new File(DIR_PATH_FILL+"/"+name+".json");

                FileOutputStream fos = new FileOutputStream(file2);
                fos.write(jsonParent.toString().getBytes());
                fos.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private void fetchCheckBox(View v, JSONObject jsonObj) throws JSONException {
        LinearLayout ll = v.findViewById(R.id.field_fill_check_linear_layout);
        int count = ll.getChildCount();
        String str = "";
        ArrayList<CheckBox> listOfCheckBox = new ArrayList<CheckBox>();
        for (int i=0;i<count;i++) {
            View o = ll.getChildAt(i);
            if (o instanceof CheckBox) {
                listOfCheckBox.add((CheckBox) o);
                if(((CheckBox) o).isChecked()){
                    str = ((CheckBox) o).getText().toString() + " | " + str;
                }
            }
        }

        JSONObject fillJson = new JSONObject();
        fillJson.put("CheckBox", str);
        listFillJson.add(fillJson);
    }

    private void fetchRadioGroup(View v, JSONObject jsonObj) throws JSONException {
        RadioGroup rg = v.findViewById(R.id.field_fill_radio_group);
        RadioButton rb = findViewById(rg.getCheckedRadioButtonId());
        String answer = rb.getText().toString();
        JSONObject fillJson = new JSONObject();
        fillJson.put("RadioGroup", answer);
        listFillJson.add(fillJson);
    }

    private void fetchMultiText(View v, JSONObject jsonObj) throws JSONException {
        EditText et = v.findViewById(R.id.answer_fill_multi);
        String answer = et.getText().toString();
        JSONObject fillJson = new JSONObject();
        fillJson.put("MultiText", answer);
        listFillJson.add(fillJson);
    }

    private void fetchEditText(View v, JSONObject jsonObject) throws JSONException {
        boolean imag = jsonObject.getBoolean("image");
        if(!imag) {
            EditText et = v.findViewById(R.id.field_fill_edit_text_value);
            String answer = et.getText().toString();
            JSONObject fillJson = new JSONObject();
            fillJson.put("EditText", answer);
            listFillJson.add(fillJson);
        }
    }
}
