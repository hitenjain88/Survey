package com.example.survey;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class ActivityDropDownMenu extends AppCompatActivity {

    Button btn_add;
    TextView tv;
    int flag,flag2=0;
    String s1;
    ArrayList<String> al;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drop_down_menu);
        al = new ArrayList<>();

        final EditText question = findViewById(R.id.question_dropdownmenu);
        btn_add = findViewById(R.id.btn_add_dropdownmenu);
        tv = findViewById(R.id.question_dropdownmenu);

        final Spinner spinner = findViewById(R.id.spinner_dropdownmenu);
        Spinner spinnerP = findViewById(R.id.spinner_dropdownmenu_preview);

        ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(
                getApplicationContext(),
                android.R.layout.simple_spinner_item,
                al
        );
        spinner.setAdapter(adapter);
        spinnerP.setAdapter(adapter);


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag2=flag2+1;
                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDropDownMenu.this);
                builder.setTitle("Enter the Menu Name");

                final EditText input = new EditText(ActivityDropDownMenu.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        al.add(input.getText().toString());
                    }
                });
                builder.show();
            }
        });

        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                s1 = question.getText().toString();
                if (s1.isEmpty()) {
                    flag = 0;
                } else {
                    flag = 1;
                }
            TextView tt = findViewById(R.id.question_dropdownmenu_preview);
            tt.setText(tv.getText().toString());
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
                if(flag==1&&flag2>=2){
                    try {
                        returnResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(ActivityDropDownMenu.this, "Enter your Question", Toast.LENGTH_SHORT).show();
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
        JSONArray jsonArray = new JSONArray();
        for(int i = 0; i < al.size() ; i++){
            jsonArray.put(i, al.get(i));
        }

        obj.put("type", "DropDownMenu");
        obj.put("question", value);
        obj.put("group", jsonArray);
        Log.v("ActivityDropDownMenu1", obj.toString());
        intent.putExtra("json", obj.toString());

        setResult(206,intent);
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
                if(flag==1&&flag2>=2){
                    try {
                        returnResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "Enter your Question or dropdown items", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return true;
    }
}
