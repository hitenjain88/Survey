package com.example.survey;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLConnection;
import java.util.ArrayList;

import tyrantgit.explosionfield.ExplosionField;

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
        final String name = mTitle.get(i);
        //Log.v("testt12", name);
        final String name2 = name.toLowerCase();

        if(new File(DIR_PATH+"/data/"+name2+".json").exists()){
            viewHolder.btn_edit.setVisibility(View.INVISIBLE);
            viewHolder.btn_analytics.setVisibility(View.VISIBLE);
        }

        viewHolder.directory.setText(mDirectory.get(i));
        final String directory2 = mDirectory.get(i);
        viewHolder.btn_edit.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.parentlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.menu.setVisibility(View.VISIBLE);
            }
        });

        viewHolder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder al=new AlertDialog.Builder(mContext);
                al.setTitle("Delete File?");

                al.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File file = new File(directory2);

                        ExplosionField explosionField = ExplosionField.attach2Window((Activity) mContext);
                        explosionField.explode(viewHolder.parentlayout);
                        mTitle.remove(position);
                        mDirectory.remove(position);
                        notifyItemRangeRemoved(position,mTitle.size());
                        notifyDataSetChanged();

                        boolean deleted = file.delete();
                        if(deleted){
                            Toast.makeText(mContext, name+"Deleted", Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(mContext, "Something went Wrong", Toast.LENGTH_SHORT).show();

                    }
                });
                al.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "Delete kar rha mardarchod", Toast.LENGTH_SHORT).show();
                    }
                });
                al.setCancelable(true);
                al.show();


            }
        });

        viewHolder.btn_preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, mTitle.get(position), Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(mContext, PreviewForm.class);

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

         viewHolder.btn_share.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Toast.makeText(mContext, "sharing file", Toast.LENGTH_SHORT).show();

             }
         });


        viewHolder.btn_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, mTitle.get(position), Toast.LENGTH_SHORT).show();
                Intent intent =new Intent(mContext, FormFill.class);

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


        viewHolder.btn_analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(DIR_PATH+"/data/"+name2+".json");
                Intent intent =new Intent(mContext, Analitics_List.class);

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
                    intent.putExtra("name", name2);
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
            Button btn_edit, btn_delete, btn_fill, btn_preview, btn_share, btn_analytics;
            LinearLayout parentlayout, menu;

            public ViewHolder(View itemView){
                super(itemView);
                title = itemView.findViewById(R.id.name);
                directory = itemView.findViewById(R.id.directory);
                parentlayout = itemView.findViewById(R.id.parent_layout);
                menu = itemView.findViewById(R.id.existing_menu);
                btn_preview = itemView.findViewById(R.id.open);
                btn_delete = itemView.findViewById(R.id.existing_delete);
                btn_edit = itemView.findViewById(R.id.existing_edit);
                btn_fill = itemView.findViewById(R.id.existing_fill);
                btn_share = itemView.findViewById(R.id.existing_share);
                btn_analytics = itemView.findViewById(R.id.btn_Analytics);
            }
        }
}
