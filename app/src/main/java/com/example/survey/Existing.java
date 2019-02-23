package com.example.survey;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Existing extends AppCompatActivity {

    private String DIR_PATH;
    private List<String>  fileList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<String> mTitle = new ArrayList<>();
    private ArrayList<String> mDirectory = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing);



        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON
        File root = new File(DIR_PATH);
        ListDir(root);

        initRecyclerView();

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
            mDirectory.add(files[i].getAbsolutePath());
            String test = files[i].getName().toUpperCase();
            test = test.substring(0,1)+test.substring(1, test.length()-10).toLowerCase();
            mTitle.add(test);
        }
        fileList.clear();

    }
}
