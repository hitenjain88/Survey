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
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        //Create new Form Button
        Button b1 = findViewById(R.id.b1);
        //Existing Form Button
        Button b2 = findViewById(R.id.b2);


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
                            TextInputLayout auth = alertView.findViewById(R.id.author);

                            String title = tit.getEditText().getText().toString();
                            String description = des.getEditText().getText().toString();
                            String author = auth.getEditText().getText().toString();



                            if(!title.equals("") && !description.equals("") && !author.equals("")) {

                                File dir = new File(DIR_PATH);
                                if(!dir.exists()){
                                    dir.mkdir();
                                    Toast.makeText(MainPage.this, "Directory Created " +DIR_PATH, Toast.LENGTH_SHORT).show();
                                }

                                File file = new File(DIR_PATH, title + "_Form.json");
                                try {

                                    JSONObject jsonObj = new JSONObject();

                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                                    String formattedDate = df.format(c);

                                    jsonObj.put("title", title);
                                    jsonObj.put("description", description);
                                    jsonObj.put("author", author);
                                    jsonObj.put("date", formattedDate);


                                    JSONArray type = new JSONArray();

                                    JSONObject edittext = new JSONObject();

                                    edittext.put("type", "EditText");
                                    edittext.put("title", "NULL");
                                    type.put(0,edittext);

                                    jsonObj.put("type", type);

                                    String data = jsonObj.toString();

                                    FileOutputStream fos = new FileOutputStream(file);
                                    fos.write(data.getBytes());
                                    fos.close();
                                    alertDialog.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), CreateForm.class);
                                    intent.putExtra("title", title);
                                    intent.putExtra("description", description);
                                    startActivity(intent);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Toast.makeText(MainPage.this, "File Not Created There was an Internal Error", Toast.LENGTH_SHORT).show();

                                    alertDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.exit) {
            return true;
        }
        else if(id == R.id.copy) {
            return true;
        }
        else if(id == R.id.paste){
            return true;
        }
        else if(id == R.id.edit){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            // Handle the camera action
        } else if (id == R.id.current_form) {

        } else if (id == R.id.create_form) {

        } else if (id == R.id.delete_form) {

        } else if (id == R.id.report) {

        } else if (id == R.id.download) {

        } else if (id == R.id.settings) {

        } else if (id == R.id.help) {

        } else if (id == R.id.share) {

        } else if (id == R.id.send) {

        } else if (id == R.id.feedback) {

        }






        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
