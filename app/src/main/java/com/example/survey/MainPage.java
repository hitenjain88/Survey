package com.example.survey;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainPage extends AppCompatActivity {

    private View alertView;
    private Dialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Create new Form Button
        Button b1 = findViewById(R.id.b1);
        //Existing Form Button
        Button b2 = findViewById(R.id.b2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/



               Intent in = new Intent(MainPage.this,navigation_drawer.class);
               startActivity(in);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {
                //Dialaog Box ye Starting aiga activity for getting simple details

                LayoutInflater inflate = LayoutInflater.from(MainPage.this);
                alertView = inflate.inflate(R.layout.create_form_title_dialog, null);

                Button btn = alertView.findViewById(R.id.btn_save);
                alertDialog = new Dialog(MainPage.this);
                alertDialog.setContentView(alertView);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();


                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputLayout tit = alertView.findViewById(R.id.title);
                        TextInputLayout des = alertView.findViewById(R.id.description);

                        String title = tit.getEditText().getText().toString();
                        String description = des.getEditText().getText().toString();

                        alertDialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), CreateForm.class);
                        intent.putExtra("title", title);
                        intent.putExtra("description", description);
                        startActivity(intent);
                    }
                });

                //Dialog code end

            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "WIP abhi kaam baki h", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
