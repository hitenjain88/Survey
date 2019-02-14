package com.example.survey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class CreateForm extends AppCompatActivity {

    private String title;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_form);




    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "WIP Save or Discard msg box", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
