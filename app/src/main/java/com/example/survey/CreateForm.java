package com.example.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateForm extends AppCompatActivity{

    private LinearLayout parentLinearLayout;
    private FloatingActionButton float_radio, float_check, float_multi, float_edit_text;
    private boolean isOpen = false;
    private String DIR_PATH;
    private String name, desc, js;
    private ArrayList<JSONObject> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        list = new ArrayList<JSONObject>();

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        this.setContentView(R.layout.activity_create_form);
        parentLinearLayout = findViewById(R.id.linearlayout);

        //btn_add = findViewById(R.id.btn_add);
        //float_add = findViewById(R.id.float_add_layout);
        float_check = findViewById(R.id.float_check);
        float_radio = findViewById(R.id.float_radio);
        float_multi = findViewById(R.id.float_multi);
        float_edit_text = findViewById(R.id.float_edit_text);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        name = title;
        setTitle(title);


        String description = intent.getStringExtra("description");
        desc = description;
        String json = intent.getStringExtra("json");
        js = json;

        //TextView t1 = findViewById(R.id.title);
        //t1.setText(title);
        //t1.setVisibility(View.INVISIBLE);
        TextView t2 = findViewById(R.id.description);
        String t = "Description : " + description;
        t2.setMaxLines(3);
        t2.setText(t);

        float_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateForm.this, Edit_Text_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        float_radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateForm.this, Radio_Button_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        float_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateForm.this, Check_Box_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

        float_multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateForm.this, Multi_Text_Activity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Exit");
        al.setMessage("Saving Form?");
        al.setPositiveButton("Save", new DialogInterface.OnClickListener() {
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
                Toast.makeText(CreateForm.this, "SIH 2019", Toast.LENGTH_SHORT).show();
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
                boolean imag = data.getBooleanExtra("image",false);
                final String json = data.getStringExtra("json");


                try {
                    final JSONObject object = new JSONObject(json);
                    list.add(object);

                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View rowView = inflater.inflate(R.layout.field_edittext, null);

                    Button remove = rowView.findViewById(R.id.btn_remove);
                    Button image_answer = rowView.findViewById(R.id.btn_add_image_field_edit_text);
                    final TextView textView = rowView.findViewById(R.id.text_view);


                    textView.setText(edittext);

                    // Add the new row before the add field button.
                    parentLinearLayout.addView(rowView);
                    if(imag){
                        image_answer.setVisibility(View.VISIBLE);
                    }
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
                        final RadioButton radioButton = new RadioButton(CreateForm.this);
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
                        final CheckBox cb = new CheckBox(CreateForm.this);
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
        Log.v("json2", data);

        File file = new File(DIR_PATH, name + "_Form.json");

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data.getBytes());
        fos.close();
    }
}
