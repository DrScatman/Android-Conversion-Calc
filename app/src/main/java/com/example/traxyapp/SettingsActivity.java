package com.example.traxyapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class SettingsActivity extends AppCompatActivity {

    private boolean isLengthMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.design_default_color_primary)));

        FloatingActionButton saveButton = findViewById(R.id.saveButton);
        Spinner fromSpinner = findViewById(R.id.fromSpinner);
        Spinner toSpinner = findViewById(R.id.toSpinner);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            isLengthMode = extras.getBoolean("isLengthMode");
            ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(
                    this,
                    (isLengthMode) ? R.array.lengthUnits : R.array.volumeUnits,
                    R.layout.spinner_item);

            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fromSpinner.setAdapter(arrayAdapter);
            toSpinner.setAdapter(arrayAdapter);

            fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    fromSpinner.setSelection(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    toSpinner.setSelection(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            fromSpinner.setSelection(arrayAdapter.getPosition(extras.getString("from")));
            toSpinner.setSelection(arrayAdapter.getPosition(extras.getString("to")));

            saveButton.setOnClickListener(x -> {
                Intent intent = new Intent();
                intent.putExtra("from", (String) fromSpinner.getSelectedItem());
                intent.putExtra("to", (String) toSpinner.getSelectedItem());
                setResult(MainActivity.SETTINGS, intent);
                finish();
            });
        }
        else
        {
            System.err.println("No extras found in Intent");
        }
    }


}
