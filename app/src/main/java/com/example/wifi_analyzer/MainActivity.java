package com.example.wifi_analyzer;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    TextView st;
    Button wifi_check,save_file;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);
        st = (TextView)findViewById(R.id.output);
        wifi_check = (Button)findViewById(R.id.check_wifi);
        save_file = (Button)findViewById(R.id.file_save);


        wifi_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display_output();

            }
        });

        save_file.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                save_to_file();

            }
        });

        handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                display_output();
                save_to_file();
                handler.postDelayed(this, 30000);
            }
        };

        handler.postDelayed(r, 1000);
    }


    private void save_to_file(){

        File directory = new File(Environment.getExternalStorageDirectory() + java.io.File.separator +"Wifi_analyzer");
        if (!directory.exists())
            Toast.makeText(this, (directory.mkdirs() ? "Directory created" : "Unable to create Directory"), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "File has been Updated", Toast.LENGTH_SHORT).show();
        System.out.println(directory);
        File file = new File(Environment.getExternalStorageDirectory() + java.io.File.separator +"Wifi_analyzer" + java.io.File.separator + "Wifi_Analyzer.txt");
        System.out.println(file);

        Date Time = Calendar.getInstance().getTime();
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }

        try {
            OutputStreamWriter file_writer = new OutputStreamWriter(new FileOutputStream(file,true));
            BufferedWriter buffered_writer = new BufferedWriter(file_writer);
            buffered_writer.write("############\n"+Time+"\n"+st.getText().toString()+"\n");
            buffered_writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void display_output(){
        WifiManager wifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        int rssi = wifiManager.getConnectionInfo().getRssi();
        int level = WifiManager.calculateSignalLevel(rssi,5);
        String ssid = wifiManager.getConnectionInfo().getSSID();
        String Mac = wifiManager.getConnectionInfo().getMacAddress();

        st.setText("\t\tSignal Strength of "+ ssid+"\n\n\t\tRSSI = "+ rssi + " dbm \n\n\t\tLevel = "+ level + " out of 5"+"\n\n\t\tMac Address = "+ Mac);
    }
}
