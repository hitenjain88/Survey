package com.example.survey;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateForm extends AppCompatActivity{

    private LinearLayout parentLinearLayout;
    private Button btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_create_form);
        parentLinearLayout = findViewById(R.id.linearlayout);

        btn_add = findViewById(R.id.btn_add);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);

        String description = intent.getStringExtra("description");

        //TextView t1 = findViewById(R.id.title);
        //t1.setText(title);
        //t1.setVisibility(View.INVISIBLE);
        TextView t2 = findViewById(R.id.description);
        String t = "Description : " + description;
        t2.setMaxLines(3);
        t2.setText(t);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddField(getCurrentFocus());


            }
        });

    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_edittext, null);
        Button remove = rowView.findViewById(R.id.btn_remove);


        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentLinearLayout.removeView(rowView);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "WIP Save or Discard msg box", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
