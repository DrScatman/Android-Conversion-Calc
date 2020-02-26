package com.example.traxyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SettingsActivity extends AppCompatActivity {

    private boolean isLengthMode;
    private ArrayAdapter<CharSequence> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        Spinner fromSpinner = findViewById(R.id.fromSpinner);
        Spinner toSpinner = findViewById(R.id.toSpinner);

        TextView fromLabel = findViewById(R.id.fromText);
        TextView toLabel = findViewById(R.id.toText);


        arrayAdapter = ArrayAdapter.createFromResource(this,
                (isLengthMode) ? R.array.lengthUnits : R.array.volumeUnits,
                android.R.layout.simple_spinner_item);

        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(arrayAdapter);
        toSpinner.setAdapter(arrayAdapter);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                fromLabel.setText((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toLabel.setText((String) adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromSpinner.setSelection();
            fromLabel.setText(extras.getString("from"));
            toLabel.setText(extras.getString("to"));
            isLengthMode = extras.getBoolean("isLengthMode");
        }
    }

    private void test() {
        for (int s : arrayAdapter.) {

        }
    }
}
