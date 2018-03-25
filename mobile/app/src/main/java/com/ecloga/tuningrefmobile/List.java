package com.ecloga.tuningrefmobile;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class List extends ListActivity {
    public static String number;
    public static String[] ratings;
    public static Context parentContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);

        ListView lv = getListView();
        lv.setTextFilterEnabled(true);

        final String[] competitors = new String[MainActivity.competitors.size()];
        int i = 0;

        for(String competitor : MainActivity.competitors.keySet()) {
            competitors[i] = competitor + " [" + MainActivity.competitors.get(competitor) + "]";
            i++;
        }

        setListAdapter(new ArrayAdapter<String>(this, R.layout.item, competitors));

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                number = competitors[position].substring(0, competitors[position].indexOf(" "));
                ratings = MainActivity.ratings.get(number);

                parentContext = List.this;

                Intent intent = new Intent(getApplicationContext(), Rate.class);
                startActivity(intent);
            }
        });
    }
}
