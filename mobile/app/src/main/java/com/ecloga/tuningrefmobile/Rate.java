package com.ecloga.tuningrefmobile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.*;
import java.util.ArrayList;

public class Rate extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        final ArrayList<EditText> arrayList = new ArrayList<>();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        String[] category = new String[] {"~EKSTERIJER",
                "Boja automobila",
                "Tockovi",
                "Spojleri",
                "Modifikovani detalj",
                "Opsti vizuelni utisak eksterijera",
                "~ENTERIJER",
                "Izgled i kvalitet audio - video sistema",
                "Izgled sedista i kokpita",
                "Modifikovani detalj",
                "Opsti vizuelni utisak enterijera",
                "~MOTORNI PROSTOR",
                "Stepen ocuvanosti, sredjenosti i higijene motora",
                "Atraktivnost motornog prostora",
                "~OSTALO",
                "Najnizi deo automobila"};

        int counter = 0;

        for(String cat : category) {
            if(cat.charAt(0) == '~') {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setText(cat.substring(1));
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextScaleX(2);
                textView.setScaleY(2);

                linearLayout.addView(textView);
            }else {
                EditText editText = new EditText(this);
                editText.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                editText.setHint(cat);

                if(MainActivity.competitors.get(List.number).equals("YES")) {
                    editText.setText(List.ratings[counter]);
                }

                arrayList.add(editText);

                linearLayout.addView(editText);

                counter++;
            }
        }

        Button bSave = new Button(this);
        bSave.setBackgroundColor(Color.parseColor("#2ecc71"));
        bSave.setText("Save");
        bSave.setTextColor(Color.WHITE);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] results = new String[arrayList.size()];

                int i = 0;

                for(EditText editText : arrayList) {
                    results[i] = String.valueOf(editText.getText());
                    i++;
                }

                boolean nullFound = false;

                for(String s : results) {
                    if(s == null || s.isEmpty()) {
                        nullFound = true;
                        break;
                    }
                }

                if(nullFound) {
                    try {
                        PrintWriter w = new PrintWriter(MainActivity.directoryName + File.separator + "ratings" + File.separator + List.number + ".trr", "UTF-8");

                        w.println("");

                        w.close();
                    }catch(FileNotFoundException | UnsupportedEncodingException e) {
                        Toast.makeText(getApplicationContext(), "Ratings cannot be saved", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    File list = new File(MainActivity.directoryName + File.separator + "list.trl");

                    try {
                        FileInputStream fis = new FileInputStream(list);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line;
                        i = 0;
                        ArrayList<String> lines = new ArrayList<String>();
                        String sub = "";

                        while ((line = br.readLine()) != null) {
                            i++;

                            if(line.substring(0, line.indexOf(":")).equals(List.number)
                                    && line.substring(line.length() - 3).equals("YES")) {

                                sub = line.replace(line.substring(line.length() - 3), "");
                                sub += "NO";

                                MainActivity.competitors.put(List.number, "NO");

                                lines.add(sub);
                            }else {
                                lines.add(line);
                            }
                        }

                        if(!sub.isEmpty()) {
                            try {
                                PrintWriter w = new PrintWriter(MainActivity.directoryName + File.separator + "list.trl", "UTF-8");

                                for(String l : lines) {
                                    w.println(l);
                                }

                                w.close();
                            }catch(FileNotFoundException | UnsupportedEncodingException e) {
                                Toast.makeText(getApplicationContext(), "Ratings cannot be saved", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }else {
                    try {
                        PrintWriter w = new PrintWriter(MainActivity.directoryName + File.separator + "ratings" + File.separator + List.number + ".trr", "UTF-8");

                        for(String rating : results) {
                            w.println(rating);
                        }

                        w.close();
                    }catch(FileNotFoundException | UnsupportedEncodingException e) {
                        Toast.makeText(getApplicationContext(), "Ratings cannot be saved", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    File list = new File(MainActivity.directoryName + File.separator + "list.trl");

                    try {
                        FileInputStream fis = new FileInputStream(list);
                        DataInputStream in = new DataInputStream(fis);
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String line;
                        i = 0;
                        ArrayList<String> lines = new ArrayList<String>();
                        String sub = "";

                        while ((line = br.readLine()) != null) {
                            i++;

                            if(line.substring(0, line.indexOf(":")).equals(List.number)
                                    && line.substring(line.length() - 2).equals("NO")) {

                                sub = line.replace(line.substring(line.length() - 2), "");
                                sub += "YES";

                                MainActivity.competitors.put(List.number, "YES");

                                lines.add(sub);
                            }else {
                                lines.add(line);
                            }
                        }

                        if(!sub.isEmpty()) {
                            try {
                                PrintWriter w = new PrintWriter(MainActivity.directoryName + File.separator + "list.trl", "UTF-8");

                                for(String l : lines) {
                                    w.println(l);
                                }

                                w.close();
                            }catch(FileNotFoundException | UnsupportedEncodingException e) {
                                Toast.makeText(getApplicationContext(), "Ratings cannot be saved", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }

                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                finish();

                Activity parentActivity;
                parentActivity = (Activity) List.parentContext;
                parentActivity.finish();

                Intent intent = new Intent(getApplicationContext(), List.class);
                startActivity(intent);
            }
        });

        linearLayout.addView(bSave);
    }
}
