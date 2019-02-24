package com.example.survey;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";
    private String DIR_PATH;
    private ArrayList<String>  mTitle = new ArrayList<>();
    private ArrayList<String> mDirectory = new ArrayList<>();

    private Context mContext;

    public RecyclerViewAdapter( Context mContext,ArrayList<String> mTitle, ArrayList<String> mDirectory) {
        this.mTitle = mTitle;
        this.mDirectory = mDirectory;
        this.mContext = mContext;
        DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_view, viewGroup, false);
       ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Log.d("TAG", "onBind");
        final int position = i;
        viewHolder.title.setText(mTitle.get(i));
        viewHolder.directory.setText(mDirectory.get(i));
        final String directory2 = mDirectory.get(i);
        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mTitle.get(position), Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(mContext, ExistingForm.class);

                File file = new File(directory2);
                StringBuilder json = new StringBuilder();
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));

                    String line;

                    while ((line = br.readLine()) != null) {
                        json.append(line);
                    }
                    br.close();

                    Log.v("READFILE", String.valueOf(json));
                    intent.putExtra("json", String.valueOf(json));
                    mContext.startActivity(intent);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mTitle.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
            TextView title, directory;
            Button btn;
            LinearLayout parentlayout;

            public ViewHolder(View itemView){
                super(itemView);
                title = itemView.findViewById(R.id.name);
                directory = itemView.findViewById(R.id.directory);
                parentlayout = itemView.findViewById(R.id.parent_layout);
                btn = itemView.findViewById(R.id.open);

            }
        }
}
