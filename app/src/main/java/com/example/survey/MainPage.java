package com.example.survey;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.security.Permission;

public class MainPage extends AppCompatActivity {

    private View alertView;
    private Dialog alertDialog;
    private String DIR_PATH;
    private final int STORAGE_PERMISSION_CODE = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        //Create new Form Button
        Button b1 = findViewById(R.id.b1);
        //Existing Form Button
        Button b2 = findViewById(R.id.b2);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainPage.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestStoragePermission();
                }else{

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



                            if(!title.equals("") && !description.equals("")) {

                                File file = new File(DIR_PATH);
                                if(!file.exists()){
                                    file.mkdir();
                                    Toast.makeText(MainPage.this, "Directory Created", Toast.LENGTH_SHORT).show();
                                }

                                alertDialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), CreateForm.class);
                                intent.putExtra("title", title);
                                intent.putExtra("description", description);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(MainPage.this, "Enter the Details Correctly", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    //Dialog code end

                }



            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainPage.this, "WIP abhi kaam baki h", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
        new AlertDialog.Builder(this)
                .setTitle("Storage Permission Needed")
                .setMessage("This permission is needed to save and retrive your form")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainPage.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        .create()
        .show();

        }else{
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "PERMISSON IS NOT GRANTED", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
