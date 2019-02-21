package com.example.survey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class Testing extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Button btn = findViewById(R.id.btn_testing);
        final TextView result = findViewById(R.id.result_testing);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", "EditText");
                    obj.put("question", "What is your name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                result.setText(obj.toString());
            }
        });
    }
}
