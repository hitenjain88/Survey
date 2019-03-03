package com.example.survey;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Existing extends AppCompatActivity {

    private String DIR_PATH;
    private View alertView;
    private Dialog alertDialog;
    private final int STORAGE_PERMISSION_CODE = 100;
    private List<String>  fileList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mDirectory = new ArrayList<>();
    private ArrayList<String> actualtitle=new ArrayList<>();
    private ArrayList<String> actualdir=new ArrayList<>();
    private ArrayList<String> filteredtitle=new ArrayList<>();
    private ArrayList<String> filtereddir=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme2);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing);

        getSupportActionBar().setElevation(0);

        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON
        File root = new File(DIR_PATH);
        ListDir(root);

        initRecyclerView();
        actualtitle.addAll(mTitle);
        actualdir.addAll(mDirectory);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);

        MenuItem searchitem=menu.findItem(R.id.search);
        SearchView searchview=(SearchView) searchitem.getActionView();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //adapter.getFilter().filter(newText);
                filteredtitle.clear();
                filtereddir.clear();
                CharSequence constraint=newText.toLowerCase().trim();
                if(/*constraint==null || */((String)constraint).length()==0){
                    filteredtitle.addAll(actualtitle);
                    filtereddir.addAll(actualdir);
                }
                else{
                    for(int i=0;i<actualtitle.size();i++){
                        if(((actualtitle.get(i)).toLowerCase()).contains(constraint)){
                            filteredtitle.add(actualtitle.get(i));
                            filtereddir.add(actualdir.get(i));
                        }
                    }
                }


                mTitle.clear();
                mTitle.addAll(filteredtitle);
                mDirectory.clear();
                mDirectory.addAll(filtereddir);
                initRecyclerView();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search:
                Toast.makeText(this,"search bar",Toast.LENGTH_SHORT).show();
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
                            ActivityCompat.requestPermissions(Existing.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
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

    private void initRecyclerView(){
        recyclerView = findViewById(R.id.recycler);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, mTitle, mDirectory);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private void ListDir(File root) {
        File[] files = root.listFiles();
        for(int i = 0; i < files.length; i++){
            if(files[i].getName().contains(".json")) {
                mDirectory.add(files[i].getAbsolutePath());
                String test = files[i].getName().toUpperCase();
                test = test.substring(0, 1) + test.substring(1, test.length() - 10).toLowerCase();
                mTitle.add(test);
            }
        }
        fileList.clear();

    }
}
