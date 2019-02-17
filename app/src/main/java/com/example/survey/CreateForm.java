package com.example.survey;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CreateForm extends AppCompatActivity{

    private LinearLayout parentLinearLayout;
    private Button btn_add, btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);
        parentLinearLayout = findViewById(R.id.linearlayout);

        btn_add = findViewById(R.id.btn_add);
        btn_del = findViewById(R.id.btn_del);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        TextView t1 = findViewById(R.id.title);
        t1.setText(title);
        TextView t2 = findViewById(R.id.description);
        t2.setText(description);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddField(getCurrentFocus());


            }
        });
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View mview = findViewById(R.id.linearlayout);
                onDelete(mview);

            }
        });
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field_edittext, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }


    @Override
    public void onBackPressed() {
        Toast.makeText(this, "WIP Save or Discard msg box", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
