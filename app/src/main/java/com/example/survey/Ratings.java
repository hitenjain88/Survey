package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

public class Ratings extends AppCompatActivity {
    ImageButton img1, img2;
    RatingBar ratingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);

        img1 = (ImageButton) findViewById(R.id.i1);
        img2 = (ImageButton) findViewById(R.id.i2);
        ratingbar = (RatingBar) findViewById(R.id.rb);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(Ratings.this, MainMenu.class);
                startActivity(i1);

            }
        });
    img2.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Toast.makeText(getApplicationContext(),"send this",Toast.LENGTH_SHORT).show();
    }
    });
    }
}
