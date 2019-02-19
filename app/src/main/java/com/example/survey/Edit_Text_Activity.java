package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;

public class Edit_Text_Activity extends AppCompatActivity {

    TextView questionText;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__text_);


        final EditText question = findViewById(R.id.question_edit_text);
        questionText = findViewById(R.id.question);

        question.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                questionText.setText("Q. "+ question.getText().toString()+" ?");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        returnResult();

    }

    public void returnResult(){
        Intent intent = new Intent();
        value = questionText.getText().toString();
        intent.putExtra("question", value);
        setResult(201,intent);
        finish();

    }
}
