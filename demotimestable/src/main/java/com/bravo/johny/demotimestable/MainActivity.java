package com.bravo.johny.demotimestable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView list;
    SeekBar seekBar;

    public void generateTable(int position) {

        List<String> tableContent = new ArrayList<>();
        for(int i=1; i<=10; i++) {
            tableContent.add(position+" X "+i+" = "+(position*i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, tableContent);
        list.setAdapter(arrayAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = findViewById(R.id.listView);
        seekBar = findViewById(R.id.seekBar);

        generateTable(10);

        seekBar.setMax(20);
        seekBar.setProgress(10);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int min = 1;
                int timesTable;
                if(progress < min) {
                    timesTable = min;
                    seekBar.setProgress(timesTable);
                } else {
                    timesTable = progress;
                }
                generateTable(timesTable);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}
