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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Check_Box_Activity extends AppCompatActivity {

    Button btn_add;
    TextView tv;
    String title,s1;
    int flag;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check__box_);
        linearLayout = findViewById(R.id.preview_check_linearlayout);

        btn_add = findViewById(R.id.btn_add_check);
        tv = findViewById(R.id.question_check_preview);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mView = findViewById(R.id.check_linearlayout);
                //LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final LinearLayout ll = findViewById(R.id.check_linearlayout);
                final LinearLayout llPreview = findViewById(R.id.preview_check_linearlayout);
                final CheckBox cb = new CheckBox(Check_Box_Activity.this);
                cb.setTextColor(Color.rgb(255,255,255));



                AlertDialog.Builder builder = new AlertDialog.Builder(Check_Box_Activity.this);
                builder.setTitle("Enter the Check Box Title");
                final EditText input = new EditText(Check_Box_Activity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        title = input.getText().toString();
                        if (!title.equals("")){
                            cb.setText(title);
                        ll.addView(cb);
                        final CheckBox cb2 = new CheckBox(Check_Box_Activity.this);
                        cb2.setTextColor(Color.rgb(0, 0, 0));
                        cb2.setText(title);
                        llPreview.addView(cb2);

                        cb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Check_Box_Activity.this);
                                builder.setTitle("Enter the Check Box Name");
                                final EditText input2 = new EditText(Check_Box_Activity.this);
                                input2.setInputType(InputType.TYPE_CLASS_TEXT);
                                builder.setView(input2);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (!input2.getText().toString().equals("")) {
                                            cb.setText(input2.getText().toString());
                                            cb2.setText(input2.getText().toString());
                                        } else {
                                            Toast.makeText(Check_Box_Activity.this, "Input some text first", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        ll.removeView(cb);
                                        llPreview.removeView(cb2);
                                        Toast.makeText(Check_Box_Activity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();
                            }
                        });

                        }
                        else{
                            Toast.makeText(Check_Box_Activity.this, "Enter Some text", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
            }
        });

        final EditText et = findViewById(R.id.question_check);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            tv.setText("Q. " + et.getText().toString() + " ?");
                s1=et.getText().toString();
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
                    Toast.makeText(Check_Box_Activity.this, "Enter your Question", Toast.LENGTH_SHORT).show();
                }
            }
        });
        al.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        al.setCancelable(true);
        al.show();
    }

    public void returnResult() throws JSONException {
        Intent intent = new Intent();
        String value = tv.getText().toString();

        intent.putExtra("question", value);
        JSONObject obj = new JSONObject();

        obj.put("type", "CheckBox");
        obj.put("question", value);

        JSONArray arr = new JSONArray();

        int count = linearLayout.getChildCount();
        //Log.v("json", ""+count);
        ArrayList<CheckBox> listOfCheckBox = new ArrayList<CheckBox>();
        for (int i=0;i<count;i++) {
            View o = linearLayout.getChildAt(i);
            if (o instanceof CheckBox) {
                listOfCheckBox.add((CheckBox) o);
                JSONObject temp = new JSONObject();
                temp.put("title", ((CheckBox) o).getText());
                arr.put(i, temp);
            }
        }

        obj.put("group", arr);
        intent.putExtra("json", obj.toString());
        Log.v("json", obj.toString());
        //Toast.makeText(this, obj.toString(), Toast.LENGTH_SHORT).show();
        setResult(203,intent);
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
