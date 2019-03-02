package com.example.survey;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;


public class ActivityDocument extends AppCompatActivity {

    TextView questionText;
    String value,s1;
    Button btn_image;
    int flag;

    private static final int REQUEST_CODE = 1;
    private Bitmap bitmap;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);
        final EditText question = findViewById(R.id.question_document);
        questionText = findViewById(R.id.question);
        imageView = findViewById(R.id.image_edit);
        btn_image = findViewById(R.id.btn_add_image_document);


        question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionText.setText("Q. " + question.getText().toString() + " ?");
                s1 = question.getText().toString();
                if (s1.isEmpty()) {
                    flag = 0;
                } else {
                    flag = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ActivityDocument.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Glide.with(this)
                        .load(resultUri)
                        .apply(new RequestOptions())
                        .into(imageView);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder al=new AlertDialog.Builder(this);
        al.setTitle("Exit");
        al.setMessage("Saving data or not");

        al.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(flag==1){
                    try {
                        returnResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(ActivityDocument.this, "Enter your Question", Toast.LENGTH_SHORT).show();
                }
            }
        });
        al.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        al.setCancelable(true);
        al.show();

    }


    public void returnResult() throws JSONException {
        Intent intent = new Intent();
        value = questionText.getText().toString();

        intent.putExtra("question", value);
        JSONObject obj = new JSONObject();

        obj.put("type", "document");
        obj.put("question", value);

        intent.putExtra("json", obj.toString());

        setResult(205,intent);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_option:
                if(flag==1){
                    try {
                        returnResult();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }}
                else{
                    Toast.makeText(this, "Enter your Question", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        return true;
    }
}
