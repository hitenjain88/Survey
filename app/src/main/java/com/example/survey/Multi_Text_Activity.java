package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Multi_Text_Activity extends AppCompatActivity {

    TextView questionText;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi__text_);

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
        try {
            returnResult();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void returnResult() throws JSONException {
        Intent intent = new Intent();
        value = questionText.getText().toString();

        intent.putExtra("question", value);
        JSONObject obj = new JSONObject();

        obj.put("type", "EditText");
        obj.put("question", value);

        intent.putExtra("json", obj.toString());

        setResult(204,intent);
        finish();

    }
}
