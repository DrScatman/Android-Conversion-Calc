package com.example.traxyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView fromLabel, toLabel;
    private boolean isLengthMode;
    private boolean isFromCalc;
    public static final int SETTINGS = 1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ConvCalc");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.design_default_color_primary)));

        isLengthMode = true;

        TextView titleLabel = findViewById(R.id.titleLabel);
        fromLabel = findViewById(R.id.fromLabel);
        toLabel = findViewById(R.id.toLabel);
        EditText fromInput = findViewById(R.id.fromInput);
        EditText toInput = findViewById(R.id.toInput);

        Button calcButton = findViewById(R.id.calcButton);
        Button modeButton = findViewById(R.id.modeButton);
        Button clearButton = findViewById(R.id.clearButton);

        fromInput.setOnTouchListener((arg0, arg1) -> {
            isFromCalc = true;
            return false;
        });
        toInput.setOnTouchListener((arg0, arg1) -> {
            isFromCalc = false;
            return false;
        });

        calcButton.setOnClickListener(x -> {
            KeyboardWrapper.hideSoftKeyboard(this);
            double fromVal = 0;
            double toVal = 0;

            if (!fromInput.getText().toString().isEmpty()) {
                fromVal = Double.parseDouble(fromInput.getText().toString());
            }
            if (!toInput.getText().toString().isEmpty()) {
                toVal = Double.parseDouble(toInput.getText().toString());
            }

            if (isFromCalc) {
                if (isLengthMode) {
                    toInput.setText((Double.toString(UnitsConverterWrapper.convert(fromVal,
                            UnitsConverterWrapper.LengthUnits.valueOf(fromLabel.getText().toString()),
                            UnitsConverterWrapper.LengthUnits.valueOf(toLabel.getText().toString())))));
                } else {
                    toInput.setText((Double.toString(UnitsConverterWrapper.convert(fromVal,
                            UnitsConverterWrapper.VolumeUnits.valueOf(fromLabel.getText().toString()),
                            UnitsConverterWrapper.VolumeUnits.valueOf(toLabel.getText().toString())))));
                }
            }
            else
            {
                if (isLengthMode) {
                    fromInput.setText((Double.toString(UnitsConverterWrapper.convert(fromVal,
                            UnitsConverterWrapper.LengthUnits.valueOf(toLabel.getText().toString()),
                            UnitsConverterWrapper.LengthUnits.valueOf(fromLabel.getText().toString())))));
                } else {
                    fromInput.setText((Double.toString(UnitsConverterWrapper.convert(fromVal,
                            UnitsConverterWrapper.VolumeUnits.valueOf(toLabel.getText().toString()),
                            UnitsConverterWrapper.VolumeUnits.valueOf(fromLabel.getText().toString())))));
                }
            }
        });

        modeButton.setOnClickListener(x -> {
            KeyboardWrapper.hideSoftKeyboard(this);
            if (isLengthMode) {
                isLengthMode = false;
                titleLabel.setText(R.string.Volume_Converter);
                fromLabel.setText(R.string.Gallons);
                toLabel.setText(R.string.Liters);
            }
            else {
                isLengthMode = true;
                titleLabel.setText(R.string.Length_Converter);
                fromLabel.setText(R.string.Yards);
                toLabel.setText(R.string.Meters);
            }
        });

        clearButton.setOnClickListener(x -> {
            KeyboardWrapper.hideSoftKeyboard(this);
            fromInput.setText("");
            toInput.setText("");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);

            intent.putExtra("from", fromLabel.getText().toString());
            intent.putExtra("to", toLabel.getText().toString());
            intent.putExtra("isLengthMode", isLengthMode);

            startActivityForResult(intent, SETTINGS);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SETTINGS) {
            TextView fromLabel = findViewById(R.id.fromLabel);
            TextView toLabel = findViewById(R.id.toLabel);

            fromLabel.setText(data.getStringExtra("from"));
            toLabel.setText(data.getStringExtra("to"));
        }

    }

}
