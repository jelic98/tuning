package com.ecloga.tuningrefmobile;

import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class  MainActivity extends AppCompatActivity {
    public static HashMap<String, String> competitors = new HashMap<String, String>();
    public static HashMap<String, String[]> ratings = new HashMap<String, String[]>();
    public static String directoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bStart = (Button) findViewById(R.id.bStart);
        bStart.setBackgroundColor(Color.parseColor("#2ecc71"));
        bStart.setTextColor(Color.WHITE);
        bStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "TuningRef");

                boolean success = true;

                if(!dir.exists()) {
                    success = dir.mkdirs();
                }

                if(success) {
                    File[] contents = dir.listFiles();

                    if(contents == null || contents.length == 0) {
                        Toast.makeText(getApplicationContext(), "No sessions were found", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), DirectoryPicker.class);
                        intent.putExtra("startDir", Environment.getExternalStorageDirectory() + File.separator + "TuningRef");
                        startActivityForResult(intent, DirectoryPicker.PICK_DIRECTORY);
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Program folder could not be found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == DirectoryPicker.PICK_DIRECTORY && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String path = (String) extras.get(DirectoryPicker.CHOSEN_DIRECTORY);
            directoryName = path;

            File list = new File(path + File.separator + "list.trl");

            try{
                FileInputStream fis = new FileInputStream(list);
                DataInputStream in = new DataInputStream(fis);
                BufferedReader br = new BufferedReader(new InputStreamReader(in));
                String line;
                int i = 0;

                competitors.clear();

                while((line = br.readLine()) != null) {
                    i++;

                    String numberValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(numberValue + ":", "");

                    String nameValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(nameValue + ":", "");

                    String vehicleValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(vehicleValue + ":", "");

                    String classValue = line.substring(0, line.indexOf(":"));
                    line = line.replaceFirst(classValue + ":", "");

                    String ratedValue = line;

                    competitors.put(numberValue, ratedValue);
                }

                in.close();
            }catch(Exception e){
                e.printStackTrace();
            }

            ratings.clear();

            File dir = new File(path + File.separator + "ratings");
            File[] contents = dir.listFiles();

            if(contents == null || contents.length == 0) {
                Toast.makeText(getApplicationContext(), "This session has no competitors", Toast.LENGTH_SHORT).show();
            }else {
                for(File file : contents) {
                    String numberValue = file.getName().replace(".tlr", "");
                    numberValue = numberValue.replace(".trr", "");

                    try{
                        FileInputStream fis = new FileInputStream(file);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line;
                        int i = 0;
                        ArrayList<String> temp = new ArrayList<String>();

                        while((line = br.readLine()) != null) {
                            temp.add(line);
                            i++;
                        }

                        String[] rating = new String[i + 1];

                        i = 0;

                        for(String t : temp) {
                            rating[i] = t;

                            i++;
                        }

                        ratings.put(numberValue, rating);

                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                Intent intent = new Intent(getApplicationContext(), List.class);
                startActivity(intent);
            }
        }
    }
}
