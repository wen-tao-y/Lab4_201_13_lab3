package lab3_201_13.ca.uwaterloo.ca.lab3_201_13;

import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;
import mapper.MapLoader;
import mapper.MapView;

public class Lab3_201_13 extends AppCompatActivity {
    static LineGraphView graph;



    public MapView map;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3_201_13);
        map = new MapView(getApplicationContext(),640, 600,25, 25);
        registerForContextMenu(map);

        //static MapLoader mapLoader = new MapLoader();
        LinearLayout layout = (LinearLayout) findViewById(R.id.ll);
        layout.setOrientation(LinearLayout.VERTICAL);

        try {
            map.setMap(MapLoader.loadMap(getExternalFilesDir(null), "E2-3344.svg"));

            graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
            layout.addView(graph);
            graph.setVisibility(View.VISIBLE);


        } catch ( NullPointerException e){
            Log.d("exception", "null pointer!");
        }
        TextView stepCount = (TextView)findViewById(R.id.label1);
        TextView AccValuesOut = (TextView)findViewById(R.id.label2);
        TextView orientation = (TextView)findViewById(R.id.label3);




        stepCount.setTextSize(35);

        // Create sensor manager and Sensor references for each applicable sensor
        final SensorManager sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        final Sensor AccSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final Sensor AccerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        final Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        OrientationManager orientationManager = new OrientationManager(orientation);

        final SensorEventListener AccListener = new AccSensorEventListener(getApplicationContext(), AccValuesOut,stepCount,
                graph,   true, orientationManager );

        final OrientationManager.GravSensorEventListener gravEventListener =  orientationManager.new GravSensorEventListener();
        final OrientationManager.MagSensorEventListener magEventListener =  orientationManager.new MagSensorEventListener();

        sensorManager.registerListener(AccListener, AccSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(gravEventListener, AccerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(magEventListener, magSensor, SensorManager.SENSOR_DELAY_GAME);

        // add clear button for class
        final Button clearButton = (Button) findViewById(R.id.b);
        clearButton.setText("Clear");
        layout.addView(clearButton);
        clearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((AccSensorEventListener) AccListener).clearRecords();
                ((AccSensorEventListener) AccListener).resetCounter();
            }
        });

        // add clear button for class


        final Button pauseButton = new Button(getApplicationContext());
        pauseButton.setText("Pause");

        layout.addView(pauseButton);
        pauseButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {


                        sensorManager.registerListener(AccListener, AccSensor, SensorManager.SENSOR_DELAY_GAME);
                        sensorManager.registerListener(gravEventListener, AccerometerSensor, SensorManager.SENSOR_DELAY_GAME);
                        sensorManager.registerListener(magEventListener, magSensor, SensorManager.SENSOR_DELAY_GAME);
                        pauseButton.setText("Pause");



                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });
        layout.addView(map);
        map.setVisibility(View.VISIBLE);


    }

    }

