package com.example.traxyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.traxyapp.dummy.HistoryContent;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private TextView fromLabel, toLabel, titleLabel;
    private EditText fromInput, toInput;
    private boolean isLengthMode;
    private boolean isFromCalc;

    public static final int SETTINGS_RESULT = 1;
    public static final int HISTORY_RESULT = 2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ConvCalc");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.design_default_color_primary)));

        isLengthMode = true;

        titleLabel = findViewById(R.id.titleLabel);
        fromLabel = findViewById(R.id.fromLabel);
        toLabel = findViewById(R.id.toLabel);
        fromInput = findViewById(R.id.fromInput);
        toInput = findViewById(R.id.toInput);

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

                toVal = Double.parseDouble(toInput.getText().toString());

                HistoryContent.addItem(new HistoryContent.HistoryItem(fromVal, toVal, isLengthMode,
                        fromLabel.getText().toString(), toLabel.getText().toString(), DateTime.now()));
            } else {
                if (isLengthMode) {
                    fromInput.setText((Double.toString(UnitsConverterWrapper.convert(toVal,
                            UnitsConverterWrapper.LengthUnits.valueOf(toLabel.getText().toString()),
                            UnitsConverterWrapper.LengthUnits.valueOf(fromLabel.getText().toString())))));
                } else {
                    fromInput.setText((Double.toString(UnitsConverterWrapper.convert(toVal,
                            UnitsConverterWrapper.VolumeUnits.valueOf(toLabel.getText().toString()),
                            UnitsConverterWrapper.VolumeUnits.valueOf(fromLabel.getText().toString())))));
                }

                fromVal = Double.parseDouble(fromInput.getText().toString());

                HistoryContent.addItem(new HistoryContent.HistoryItem(toVal, fromVal, isLengthMode,
                        toLabel.getText().toString(), fromLabel.getText().toString(), DateTime.now()));
            }
        });

        modeButton.setOnClickListener(x -> {
            KeyboardWrapper.hideSoftKeyboard(this);
            if (isLengthMode) {
                isLengthMode = false;
                titleLabel.setText(R.string.Volume_Converter);
                fromLabel.setText(R.string.Gallons);
                toLabel.setText(R.string.Liters);
            } else {
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

            startActivityForResult(intent, SETTINGS_RESULT);
            return true;

        } else if (item.getItemId() == R.id.action_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivityForResult(intent, HISTORY_RESULT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SETTINGS_RESULT) {
            this.fromLabel.setText(data.getStringExtra("from"));
            this.toLabel.setText(data.getStringExtra("to"));
        } else if (resultCode == HISTORY_RESULT) {
            if (data.hasExtra("item")) {
                String[] vals = data.getStringArrayExtra("item");
                this.fromInput.setText(vals[0]);
                this.toInput.setText(vals[1]);
                this.isLengthMode = vals[2].equals("true");
                this.fromLabel.setText(vals[3]);
                this.toLabel.setText(vals[4]);
                this.titleLabel.setText(isLengthMode ? R.string.Length_Converter : R.string.Volume_Converter);
            } else {
                System.out.println("History intent has no extra value");
            }
        }
    }
}
