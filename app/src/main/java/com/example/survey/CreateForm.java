package com.example.survey;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

public class CreateForm extends AppCompatActivity{

    private LinearLayout parentLinearLayout;
    private Button btn_add;
    private FloatingActionButton float_radio, float_check, float_multi, float_edit_text;
    private Animation FabOpen, FabClose, FabRClockwise, FabARClockwise;
    private boolean isOpen = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        this.setContentView(R.layout.activity_create_form);
        parentLinearLayout = findViewById(R.id.linearlayout);

        btn_add = findViewById(R.id.btn_add);
        //float_add = findViewById(R.id.float_add_layout);
        float_check = findViewById(R.id.float_check);
        float_radio = findViewById(R.id.float_radio);
        float_multi = findViewById(R.id.float_multi);
        float_edit_text = findViewById(R.id.float_edit_text);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        setTitle(title);


        String description = intent.getStringExtra("description");

        //TextView t1 = findViewById(R.id.title);
        //t1.setText(title);
        //t1.setVisibility(View.INVISIBLE);
        TextView t2 = findViewById(R.id.description);
        String t = "Description : " + description;
        t2.setMaxLines(3);
        t2.setText(t);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddField(getCurrentFocus());
            }
        });

        float_edit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateForm.this, Edit_Text_Activity.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    public void onAddField(View v) {

    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Exit");
        al.setMessage("Saving Form?");
        al.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        al.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CreateForm.this, "SIH 2019", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        al.setCancelable(false);
        al.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){

            if(resultCode == 201){
                String edittext= data.getStringExtra("question");

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.field_edittext, null);

                Button remove = rowView.findViewById(R.id.btn_remove);
                final TextView textView = rowView.findViewById(R.id.text_view);

                textView.setText(edittext);

                // Add the new row before the add field button.
                parentLinearLayout.addView(rowView);
                remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        parentLinearLayout.removeView(rowView);
                    }
                });

            }



        }
    }
}
