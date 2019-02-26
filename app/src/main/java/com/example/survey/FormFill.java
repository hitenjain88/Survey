package com.example.survey;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FormFill extends AppCompatActivity {

    private String DIR_PATH;
    private String name, desc, js, author;

    private LinearLayout parentLinearLayout;
    private ArrayList<View> listView;
    private ArrayList<JSONObject> listJson;
    private ArrayList<JSONObject> listFillJson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_fill);

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

    private void FetchAllAnswer(){


    }
}
