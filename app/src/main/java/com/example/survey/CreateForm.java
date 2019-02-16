package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CreateForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        TextView t1 = findViewById(R.id.title);
        t1.setText(title);
        TextView t2 = findViewById(R.id.description);
        t2.setText(description);



    }




    @Override
    public void onBackPressed() {
        Toast.makeText(this, "WIP Save or Discard msg box", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
