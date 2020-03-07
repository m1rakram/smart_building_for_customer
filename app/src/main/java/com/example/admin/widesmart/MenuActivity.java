package com.example.admin.widesmart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class MenuActivity extends AppCompatActivity {

    public LinearLayout menu;
    public TextView timing;
    public TextView weathering;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        menu=(LinearLayout) findViewById(R.id.menu);
        timing=(TextView) findViewById(R.id.time);


        Thread myThread = null;
        Runnable runnable = new CountDownRunner();
        myThread= new Thread(runnable);
        myThread.start();

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent("com.example.admin.widesmart.MainActivity");
                startActivity(i);
            }
        });
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try{
                    String curTime="-";
                    Calendar c = Calendar.getInstance();
                    String sDate = convertDate(c.get(Calendar.DAY_OF_MONTH)) + "/" + convertDate(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR);
                    Date dt = new Date();
                    int hours = dt.getHours();
                    int minutes = dt.getMinutes();
                    int seconds = dt.getSeconds();
                    curTime = convertDate(hours) + ":" + convertDate(minutes);
                    timing.setText(curTime);
                }catch (Exception e) {}
            }
        });
    }
    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }
    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
        }
    }
}
