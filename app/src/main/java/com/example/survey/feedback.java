package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class feedback extends AppCompatActivity {
ImageButton img1,img2,img3;
Button button1,button2;



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        img1= (ImageButton) findViewById(R.id.image1);
        img2= (ImageButton) findViewById(R.id.i1);
        img3= (ImageButton) findViewById(R.id.i2);
        button1= (Button) findViewById(R.id.b1);
        button2= (Button) findViewById(R.id.b2);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1 = new Intent(feedback.this,MainMenu.class);
                startActivity(i1);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(feedback.this,opinion.class);
                startActivity(i2);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(feedback.this,Ratings.class);
                startActivity(i3);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new Intent(feedback.this,opinion.class);
                startActivity(i4);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new Intent(feedback.this,Ratings.class);
                startActivity(i5);
            }
        });

    }

}
