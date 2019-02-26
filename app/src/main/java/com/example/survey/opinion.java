package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class opinion extends AppCompatActivity {
    ImageButton img1,img2;
    TextView tv1,tv2,tv3;
    EditText edit1,edit2,edit3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);

        img1= (ImageButton) findViewById(R.id.image1);
        img2= (ImageButton) findViewById(R.id.image2);
        tv1= (TextView) findViewById(R.id.t1);
        tv2= (TextView) findViewById(R.id.t2);
        tv3= (TextView) findViewById(R.id.t3);
        edit1= (EditText) findViewById(R.id.e1);
        edit2= (EditText) findViewById(R.id.e2);
        edit3= (EditText) findViewById(R.id.e3);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1= new Intent(opinion.this,MainMenu.class);
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
