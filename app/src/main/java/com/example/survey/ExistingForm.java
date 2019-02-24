package com.example.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExistingForm extends AppCompatActivity {

    private LinearLayout parentLinearLayout;
    private FloatingActionButton float_radio, float_check, float_multi, float_edit_text;

    private String DIR_PATH;
    private String name, desc, js, author;
    private ArrayList<JSONObject> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_form);
        list = new ArrayList<JSONObject>();
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        float_check = findViewById(R.id.float_existing_check);
        float_radio = findViewById(R.id.float_existing_radio);
        float_multi = findViewById(R.id.float_existing_multi);
        float_edit_text = findViewById(R.id.float_existing_edit_text);
        Log.v("testing1", "akjflf");

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");
        Log.v("testing1", json);

        parentLinearLayout = findViewById(R.id.parent_layout_existing);

        try {
            JSONObject jsonObject = new JSONObject(json);
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
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        float_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingForm.this, Edit_Text_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        float_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingForm.this, Radio_Button_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        float_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingForm.this, Check_Box_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        float_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExistingForm.this, Multi_Text_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Exit");
        al.setMessage("Saving Form?");
        al.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    FetchJson();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
                finish();
            }
        });
        al.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ExistingForm.this, "No Changes maded", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        al.setCancelable(false);
        al.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){

            if(resultCode == 201){
                String edittext=data.getStringExtra("question");
                final String json = data.getStringExtra("json");


                try {
                    final JSONObject object = new JSONObject(json);
                    list.add(object);
                    Log.v("testing", list.size()+" ");
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.field_edittext, null);

                    Button remove = rowView.findViewById(R.id.btn_remove);
                    final TextView textView = rowView.findViewById(R.id.text_view);

                    textView.setText(edittext);

                    // Add the new row before the add field button.
                    parentLinearLayout.addView(rowView);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            parentLinearLayout.removeView(rowView);
                            list.remove(object);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            if(resultCode == 202){

                final String json = data.getStringExtra("json");
                Toast.makeText(this, json + " WORING", Toast.LENGTH_SHORT).show();


                try {

                    final JSONObject jsonObj = new JSONObject(json);
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

                    for(int j=0; j<len; j++)
                    {
                        JSONObject o = jsonArray.getJSONObject(j);
                        String title = o.getString("title");
                        final RadioButton radioButton = new RadioButton(ExistingForm.this);
                        radioButton.setText(title);
                        radioButton.setTextColor(Color.rgb(0, 0, 0));
                        rg.addView(radioButton, params);
                    }

                    parentLinearLayout.addView(rowView);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            list.remove(jsonObj);
                            parentLinearLayout.removeView(rowView);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            if(resultCode == 203){

                String json = data.getStringExtra("json");
                Toast.makeText(this, json + " WORING", Toast.LENGTH_SHORT).show();
                try {

                    final JSONObject jsonObj = new JSONObject(json);
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

                    for(int j=0; j<len; j++)
                    {
                        JSONObject o = jsonArray.getJSONObject(j);
                        String title = o.getString("title");
                        final CheckBox cb = new CheckBox(ExistingForm.this);
                        cb.setText(title);
                        cb.setTextColor(Color.rgb(0, 0, 0));
                        ll.addView(cb);
                    }

                    parentLinearLayout.addView(rowView);
                    remove.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            parentLinearLayout.removeView(rowView);
                            list.remove(jsonObj);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }

        if(resultCode == 204){
            String edittext=data.getStringExtra("question");
            String json=data.getStringExtra("json");

            try {
                final JSONObject obj = new JSONObject(json);
                list.add(obj);

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field_multiline, null);

                Button remove = rowView.findViewById(R.id.btn_remove);
                final TextView textView = rowView.findViewById(R.id.text_view);

                textView.setText(edittext);

                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentLinearLayout.removeView(rowView);
                        list.remove(obj);

                    }
                });

            } catch (JSONException e) {
                e.printStackTrace();
            }

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
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLinearLayout.removeView(rowView);
                list.remove(obj);

            }
        });
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
                final CheckBox cb = new CheckBox(ExistingForm.this);
                cb.setText(title);
                cb.setTextColor(Color.rgb(0, 0, 0));
                ll.addView(cb);
            }

            parentLinearLayout.addView(rowView);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parentLinearLayout.removeView(rowView);
                    list.remove(jsonObj);
                }
            });

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
                final RadioButton radioButton = new RadioButton(ExistingForm.this);
                radioButton.setText(title);
                radioButton.setTextColor(Color.rgb(0, 0, 0));
                rg.addView(radioButton, params);
            }

            parentLinearLayout.addView(rowView);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(jsonObj);
                    parentLinearLayout.removeView(rowView);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void SetEditText(JSONObject j) throws JSONException {
        String edittext=j.getString("question");

        final JSONObject object = j;
        list.add(object);
        Log.v("testing1", j.toString());
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_edittext, null);

        Button remove = rowView.findViewById(R.id.btn_remove);
        final TextView textView = rowView.findViewById(R.id.text_view);

        textView.setText(edittext);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLinearLayout.removeView(rowView);
                list.remove(object);
            }
        });

    }

    public void FetchJson() throws JSONException, IOException {
        //JSONObject json = new JSONObject(js);

        JSONObject jsonObj = new JSONObject();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(c);
        jsonObj.put("title", name);
        jsonObj.put("description", desc);
        jsonObj.put("date", formattedDate);

        JSONArray type = new JSONArray();


        for(int i = 0;i < list.size();i++){
            JSONObject obj = list.get(i);
            type.put(i, obj);
        }

        jsonObj.put("type", type);
        String data = jsonObj.toString();
        Log.v("json2", data+ " "+ name);

        File file = new File(DIR_PATH, name + "_Form.json");
        Log.v("json2", data+ " "+ name + " " + file.canWrite());

        if(!file.exists()){
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data.getBytes());
        fos.close();
    }
}
