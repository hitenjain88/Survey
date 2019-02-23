package com.example.survey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.TooltipCompat;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        Thread th = new Thread(ru);
        th.start();
    }
    Runnable ru = new Runnable() {
        @Override
        public void run() {
            try{
                Thread.sleep(2000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            Intent in = new Intent(SplashScreen.this,MainMenu.class);
            startActivity(in);
            finish();

        }



    };

}
