package com.example.survey;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Radio_Button_Activity extends AppCompatActivity {

    RadioGroup rg, rg_preview;
    Button btn_add;
    TextView tv;
    int flag;
    String s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio__button_);

        final EditText question = findViewById(R.id.question_radio);


        btn_add = findViewById(R.id.btn_add_radio);
        rg = findViewById(R.id.radio_grp);
        tv = findViewById(R.id.question_radio_preview);
        rg_preview = findViewById(R.id.preview_radio_grp);

        final String[] m_Text = new String[1];

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Radio_Button_Activity.this);
                builder.setTitle("Enter the Radio Name");

                final EditText input = new EditText(Radio_Button_Activity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text[0] = input.getText().toString();

                        if (!m_Text[0].equals("")){
                            final RadioButton radioButton = new RadioButton(Radio_Button_Activity.this);
                        radioButton.setText(m_Text[0]);
                        radioButton.setTextColor(Color.rgb(255, 255, 255));
                        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                        rg.addView(radioButton, params);

                        final RadioButton radioButtonPreview = new RadioButton(Radio_Button_Activity.this);
                        radioButtonPreview.setText(m_Text[0]);
                        radioButtonPreview.setTextColor(Color.rgb(0, 0, 0));
                        rg_preview.addView(radioButtonPreview, params);

                        radioButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Radio_Button_Activity.this);
                                builder.setTitle("Enter the Radio Name");
                                final EditText input2 = new EditText(Radio_Button_Activity.this);
                                input2.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input2);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(!input2.getText().toString().equals("")) {
                                            radioButton.setText(input2.getText().toString());
                                            radioButtonPreview.setText(input2.getText().toString());
                                        }else{
                                            Toast.makeText(Radio_Button_Activity.this, "Input some text first", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        View v = findViewById(radioButton.getId());
                                        rg.removeView(v);
                                        View v2 = findViewById(radioButtonPreview.getId());
                                        rg_preview.removeView(v2);
                                        Toast.makeText(Radio_Button_Activity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();
                            }
                        });

                    }else{
                            Toast.makeText(Radio_Button_Activity.this, "Input Some Text", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();

            }
        });


        question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv.setText("Q. " + question.getText().toString() + "?");
                s1=question.getText().toString();
                if(s1.isEmpty()){
                    flag=0;
                }
                else{flag=1;}


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Exit");
        al.setMessage("Saving data or not");

        al.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(flag==1){
                    try {
                        returnResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(Radio_Button_Activity.this, "Enter your Question", Toast.LENGTH_SHORT).show();
                }
            }
        });
        al.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    finish();
            }
        });
        al.setCancelable(false);
        al.show();



    }

    public void returnResult() throws JSONException {
        Intent intent = new Intent();
        String value = tv.getText().toString();

        intent.putExtra("question", value);
        JSONObject obj = new JSONObject();

        obj.put("type", "RadioGroup");
        obj.put("question", value);

        JSONArray arr = new JSONArray();


        int count = rg_preview.getChildCount();
        ArrayList<RadioButton> listOfRadioButtons = new ArrayList<RadioButton>();
        for (int i=0;i<count;i++) {
            View o = rg_preview.getChildAt(i);
            if (o instanceof RadioButton) {
                listOfRadioButtons.add((RadioButton)o);
                JSONObject temp = new JSONObject();
                temp.put("title", ((RadioButton) o).getText());

                arr.put(i, temp);
            }
        }

        obj.put("group", arr);
        intent.putExtra("json", obj.toString());
        Log.v("json", obj.toString());
        //Toast.makeText(this, obj.toString(), Toast.LENGTH_SHORT).show();
        setResult(202,intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.option_menu,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_option:
                if(flag==1){
                    try {
                        returnResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "Enter your Question", Toast.LENGTH_SHORT).show();
                }

                break;
        }

        return true;
    }
}

