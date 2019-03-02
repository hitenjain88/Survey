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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PreviewForm extends AppCompatActivity {

    private String DIR_PATH;
    private String name, desc, js, author;

    private LinearLayout parentLinearLayout;
    private ArrayList<JSONObject> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_form);
        list = new ArrayList<JSONObject>();
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        TextView nm = findViewById(R.id.name_preview_form);
        TextView ds = findViewById(R.id.description_preview_form);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        Log.v("testing1", json);

        parentLinearLayout = findViewById(R.id.parent_layout_preview);

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
                    case "document" : SetDocument(j);
                        break;
                    case "DropDownMenu" : SetDropDownMenu(j);
                        break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SetMultiEditText(JSONObject j) throws JSONException {
        String edittext=j.getString("question");
        final JSONObject obj = j;
        list.add(obj);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_multiline, null);

        Button remove = rowView.findViewById(R.id.btn_remove);
        final TextView textView = rowView.findViewById(R.id.text_view);

        textView.setText(edittext);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setVisibility(View.GONE);
    }

    private void SetCheckBox(JSONObject j) {
        try {
            final JSONObject jsonObj = j;
            list.add(jsonObj);
            String question = jsonObj.get("question").toString();

            JSONArray jsonArray = jsonObj.getJSONArray("group");

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field_checkbox, null);

            Button remove = rowView.findViewById(R.id.btn_remove);
            final TextView textView = rowView.findViewById(R.id.field_check_text);
            textView.setText(question);
            LinearLayout ll = rowView.findViewById(R.id.field_check_linearlayout);

            int len = jsonArray.length();

            for(int i=0; i<len; i++)
            {
                JSONObject o = jsonArray.getJSONObject(i);
                String title = o.getString("title");
                final CheckBox cb = new CheckBox(PreviewForm.this);
                cb.setText(title);
                cb.setTextColor(Color.rgb(0, 0, 0));
                ll.addView(cb);
            }

            parentLinearLayout.addView(rowView);
            remove.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetRadioGroup(JSONObject j) {

        try {
            final JSONObject jsonObj = j;
            list.add(jsonObj);
            String question = jsonObj.get("question").toString();
            JSONArray jsonArray = jsonObj.getJSONArray("group");

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.field_radio, null);

            Button remove = rowView.findViewById(R.id.btn_remove);
            final TextView textView = rowView.findViewById(R.id.field_radio_text);
            textView.setText(question);
            RadioGroup rg = rowView.findViewById(R.id.field_radio_group);

            int len = jsonArray.length();


            RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

            for(int i=0; i<len; i++)
            {
                JSONObject o = jsonArray.getJSONObject(i);
                String title = o.getString("title");
                final RadioButton radioButton = new RadioButton(PreviewForm.this);
                radioButton.setText(title);
                radioButton.setTextColor(Color.rgb(0, 0, 0));
                rg.addView(radioButton, params);
            }

            parentLinearLayout.addView(rowView);
            remove.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetEditText(JSONObject j) throws JSONException {
        String edittext=j.getString("question");
        boolean imag = j.getBoolean("value");
        final JSONObject object = j;

        Log.v("testing1", j.toString());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_edittext, null);

        Button remove = rowView.findViewById(R.id.btn_remove);

        final TextView textView = rowView.findViewById(R.id.text_view);
        textView.setText(edittext);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setVisibility(View.GONE);

    }

    private void SetDocument(JSONObject j) throws JSONException {
        String edittext = j.getString("question");
        final JSONObject object = j;
        Log.v("testing123", j.toString());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_document, null);

        final Button remove = rowView.findViewById(R.id.btn_remove);
        final TextView textView = rowView.findViewById(R.id.text_view);

        textView.setText(edittext);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLinearLayout.removeView(rowView);
                remove.setVisibility(View.GONE);
            }
        });

    }

    private void SetDropDownMenu(JSONObject j) throws JSONException {
        String edittext = j.getString("question");
        JSONArray jsonArray = j.getJSONArray("group");
        final JSONObject object = j;
        list.add(object);
        Log.v("testing1", j.toString());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_dropdownmenu, null);

        final Button remove = rowView.findViewById(R.id.btn_remove);
        final TextView textView = rowView.findViewById(R.id.text_view);
        final Spinner spinner = rowView.findViewById(R.id.field_dropdown);


        ArrayList<String> al = new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length() ; i++){
            al.add(i, jsonArray.getString(i));
        }
        ArrayAdapter<String> aa = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, al);
        textView.setText(edittext);

        spinner.setAdapter(aa);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLinearLayout.removeView(rowView);
                remove.setVisibility(View.GONE);
            }
        });


    }

}
