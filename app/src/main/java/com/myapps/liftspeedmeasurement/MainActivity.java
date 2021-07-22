package com.myapps.liftspeedmeasurement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Formatter;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //    private SpeedView speedometer;
    private Float lastY;   // used to store sensor first event value
    private Float deltaY;  // used to store sensor 2nd value
    private Double totalSpead; // used to store all value by adding them. e.g totalSpead += deltaY
    private Double totalCount; // It listen and count that how much time sensor call back us.
    private Double minSpeed;  // used to store min calculated speed
    private Double avgSpeed;  // used to store average speed
    private Double maxSpeed;  // to store maximum speed
    private String speedItem; // It store sensor returning event
    private SensorManager sensorManager; // It allow device to manage sensor in device
    private Sensor accelerometer;  // accelemeter sensor used to listen/ sense speed
    private Float deltaYMax; // used to store maximum speed at runtime
    private Button startButton, resetButton,endButton; //these are total button
    private TextView minSpeedText, maxSpeedText, avgSpeedText, textview2; // these are text view that showing out speed output dynamically


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // speedometer = findViewById(R.id.speedView);
        textview2 = findViewById(R.id.Speed);
        minSpeedText = findViewById(R.id.minSpeed);
        avgSpeedText = findViewById(R.id.avgspeed);
        maxSpeedText = findViewById(R.id.maxSpeed);
        endButton = findViewById(R.id.btnend);
        startButton = findViewById(R.id.btnStart);
        resetButton = findViewById(R.id.reset);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initSensor();
            }
        });
        InitData();

        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPause();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deltaY = 0f;
                maxSpeed = 0.0;
                textview2.setText("0.0 in/s2");
                onPause();
                InitData();

            }
        });

    }

    void initSensor() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            // fai! we dont have an accelerometer!
        }
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }


    // This method will listen movements of device.
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null) {
            deltaY = Math.abs(lastY - event.values[1]);
        }
        /* Avg speed calculation*/
        if (deltaY != 0.0f) {
            totalSpead += deltaY.doubleValue();
            totalCount += 1;
            avgSpeed = totalSpead / totalCount;
            avgSpeedText.setText("" + avgSpeed.floatValue());
            if (speedItem.length() == 0) {

                speedItem += (deltaY.toString());

            } else {
                speedItem += ("," + deltaY.toString());
            }
        }
        textview2.setText("" + deltaY);

        /*  Max speed Calculation*/
        if (deltaY > deltaYMax) {
            deltaYMax = deltaY;
            maxSpeed = deltaYMax.doubleValue();
            maxSpeedText.setText("" + maxSpeed.floatValue());
        }
        if (event != null) {
            lastY = event.values[1];
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void InitData() {

        lastY = Float.parseFloat("0");
        totalSpead = 0.0;
        totalCount = 0.0;
        minSpeed = 0.0;
        avgSpeed = 0.0;
        maxSpeed = 0.0;
        speedItem = "";
        deltaYMax = 0f;
        deltaY = 0f;
        maxSpeedText.setText("" + maxSpeed);
        minSpeedText.setText("0.0");
        avgSpeedText.setText("" + avgSpeed);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }
}
