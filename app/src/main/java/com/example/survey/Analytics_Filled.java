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
import java.util.Iterator;

public class Analytics_Filled extends AppCompatActivity {
    String json;
    String answer;

    private String DIR_PATH;
    private String name, desc, js, author;
    private LinearLayout parentLinearLayout;
    private ArrayList<JSONObject> list;
    private ArrayList<String> keys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics__filled);
        keys = new ArrayList<String>();
        Intent intent = getIntent();
        name =intent.getStringExtra("name");
        json = intent.getStringExtra("json");
        answer = intent.getStringExtra("Answer");


        Log.v("NAME_1", json);
        Log.v("NAME_1", answer);

        list = new ArrayList<JSONObject>();
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        TextView nm = findViewById(R.id.name_preview_form_analytics);
        TextView ds = findViewById(R.id.description_preview_form_analytics);


        parentLinearLayout = findViewById(R.id.parent_layout_preview_analytics);

        try {
            JSONArray arr = new JSONArray(answer);
            for(int i=0;i<arr.length();i++){
                JSONObject obj  = arr.getJSONObject(i);
                Iterator<String> itr = obj.keys();
                keys.add(itr.next());

            }

            JSONObject jsonObject = new JSONObject(json);
            Log.v("test_1", jsonObject.toString());
            String name2 = jsonObject.getString("title");
            desc = jsonObject.getString("description");
            int length = name2.length();
            name2 = name2.substring(0,1).toUpperCase() + name2.substring(1,length);
            nm.setText(name2);
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

                JSONObject x = arr.getJSONObject(i);
                String ans = x.getString(keys.get(i));

                Log.v("testing1", j.toString()+"10011");
                switch (j.getString("type")){
                    case "EditText" : SetEditText(j, ans);
                        break;
                    case "RadioGroup" : SetRadioGroup(j,ans);
                        break;
                    case "CheckBox" : SetCheckBox(j,ans);
                        break;
                    case "MultiEditText" : SetMultiEditText(j,ans);
                        break;
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SetMultiEditText(JSONObject j, String Answer) throws JSONException {
        String edittext=j.getString("question");
        final JSONObject obj = j;
        //list.add(obj);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_multiline, null);

        Button remove = rowView.findViewById(R.id.btn_remove);
        final TextView textView = rowView.findViewById(R.id.text_view);
        final TextView textViewAnswer = rowView.findViewById(R.id.field_multi_fill);
        textViewAnswer.setText(Answer);
        textView.setText(edittext);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setVisibility(View.GONE);
    }

    private void SetCheckBox(JSONObject j, String Answer) {
        try {
            final JSONObject jsonObj = j;
            //list.add(jsonObj);
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
                final CheckBox cb = new CheckBox(Analytics_Filled.this);
                cb.setText(title);
                if(Answer.contains(title)){
                    cb.setChecked(true);
                }
                cb.setClickable(false);
                cb.setTextColor(Color.rgb(0, 0, 0));
                ll.addView(cb);
            }

            parentLinearLayout.addView(rowView);
            remove.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetRadioGroup(JSONObject j, String Answer) {

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
                final RadioButton radioButton = new RadioButton(Analytics_Filled.this);
                radioButton.setText(title);
                Log.v("Answer0", Answer);
                if(title.equals(Answer)){
                    radioButton.setChecked(true);
                }
                radioButton.setTextColor(Color.rgb(0, 0, 0));
                rg.addView(radioButton, params);
            }

            parentLinearLayout.addView(rowView);
            remove.setVisibility(View.GONE);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetEditText(JSONObject j, String Answer) throws JSONException {
        String edittext=j.getString("question");
        boolean imag = j.getBoolean("image");
        final JSONObject object = j;
        list.add(object);
        Log.v("testing1", j.toString());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_edittext, null);

        Button remove = rowView.findViewById(R.id.btn_remove);
        Button image_answer = rowView.findViewById(R.id.btn_add_image_field_edit_text);
        final TextView textView = rowView.findViewById(R.id.text_view);
        final TextView answer = rowView.findViewById(R.id.field_edit_text_fill);

        if(imag){
            image_answer.setVisibility(View.VISIBLE);
            answer.setVisibility(View.INVISIBLE);
        }
        textView.setText(edittext);
        answer.setText(Answer);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setVisibility(View.GONE);

    }
}
