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
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.NavigationMenuItemView;
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
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MainMenu extends AppCompatActivity  {

    private View alertView;
    private Dialog alertDialog;
    private String DIR_PATH;
    private final int STORAGE_PERMISSION_CODE = 100;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = findViewById(R.id.tb);

        toolbar.setBackgroundColor(Color.rgb(29,29,29));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>SURVEY APP</font>"));


        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON

        //Create new Form Button
        ImageButton b1 = findViewById(R.id.b1);
        //Existing Form Button
        ImageButton b2 = findViewById(R.id.b2);

        b1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("InflateParams")
            @Override
            public void onClick(View v) {

                if(ContextCompat.checkSelfPermission(MainMenu.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestStoragePermission();
                }else{

                    //Dialaog Box ye Starting aiga activity for getting simple details

                    LayoutInflater inflate = LayoutInflater.from(MainMenu.this);
                    alertView = inflate.inflate(R.layout.create_form_title_dialog, null);

                    Button btn = alertView.findViewById(R.id.btn_save);
                    alertDialog = new Dialog(MainMenu.this);
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
                                    Toast.makeText(MainMenu.this, "Directory Created " +DIR_PATH, Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(MainMenu.this, "File Not Created There was an Internal Error", Toast.LENGTH_SHORT).show();

                                    alertDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            }
                            else{
                                Toast.makeText(MainMenu.this, "Enter the Details Correctly", Toast.LENGTH_SHORT).show();
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

                Intent intent = new Intent(MainMenu.this,Existing.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_drawer,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.feedback:
                Intent feed=new Intent(MainMenu.this,feedback.class);
                startActivity(feed);
                break;
            case R.id.exit:
                AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainMenu.this);
                alertDialogBuilder.setMessage("Are you sure you want to Exit");
                alertDialogBuilder.setIcon(R.drawable.alert);
                alertDialogBuilder.setTitle("Exit");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainMenu.this,"Application Terminated",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                //AlertDialog alertDialog=alertDialogBuilder.create();
                alertDialogBuilder.show();
                break;
        }
        return true;
    }

    public void requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this)
                    .setTitle("Storage Permission Needed")
                    .setMessage("This permission is needed to save and retrive your form")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainMenu.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 1000);
    }
}
