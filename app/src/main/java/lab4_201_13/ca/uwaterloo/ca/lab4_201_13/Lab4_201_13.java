package lab4_201_13.ca.uwaterloo.ca.lab4_201_13;

import android.graphics.PointF;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;

import ca.uwaterloo.sensortoy.LineGraphView;
import lab4_201_13.ca.uwaterloo.ca.lab4_201_13.R;
import mapper.MapLoader;
import mapper.MapView;
import mapper.NavigationalMap;

public class Lab4_201_13 extends AppCompatActivity {
    MapView map;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lab4_201_13);
        LinearLayout layout = (LinearLayout) findViewById(R.id.ll);
        layout.setOrientation(LinearLayout.VERTICAL);
        map = new MapView(getApplicationContext(),1280, 1200,50, 50);
        registerForContextMenu(map);
        LineGraphView graph = new LineGraphView(getApplicationContext(), 100, Arrays.asList("x", "y", "z"));
        graph.setVisibility(View.VISIBLE);
        layout.addView(graph);
        Button Button = (Button) findViewById(R.id.b);
        Button.setText("Clear");
        NavigationalMap nmap=new NavigationalMap();
        try {

            map.setMap (MapLoader.loadMap(getExternalFilesDir(null), "E2-3344.svg"));
            layout.addView(map);
            map.setVisibility(View.VISIBLE);

        } catch ( NullPointerException e){
            Log.d("EXCEPTION CAUGHT", "NO MAP FILE");
        }
        Graph g=new Graph(map);
        MyPositionListener positionListener=new MyPositionListener(g);

        TextView stepView = (TextView)findViewById(R.id.label1);
        TextView AccView = (TextView)findViewById(R.id.label2);
        TextView orientationView = (TextView)findViewById(R.id.label3);
        //Log.d("PATH: ", map.map.getPaths().toString());

        g.map.addListener(positionListener);


        Log.d("", String.valueOf(map.map.calculateIntersections(map.getOriginPoint(),map.getDestinationPoint())));


        lab4_201_13.ca.uwaterloo.ca.lab4_201_13.OrientationUpdater OrientationUpdater=new OrientationUpdater(orientationView);

        final AccSensorEventListener AccListener = new AccSensorEventListener( AccView,stepView,
                graph, OrientationUpdater,g );

        OrientationUpdater.GravSensorEventListener gravEventListener =  OrientationUpdater.new GravSensorEventListener();
        OrientationUpdater.MagSensorEventListener magEventListener =  OrientationUpdater.new MagSensorEventListener();

        SensorManager sensorManager = (SensorManager) getApplicationContext().getSystemService(SENSOR_SERVICE);
        Sensor accSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        Sensor gravSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Sensor magSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        sensorManager.registerListener(AccListener, accSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(gravEventListener, gravSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(magEventListener, magSensor, SensorManager.SENSOR_DELAY_GAME);
        Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AccListener.clear();
            }
        });


    }
    @Override
    public  void  onCreateContextMenu(ContextMenu menu , View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu , v, menuInfo);
        map.onCreateContextMenu(menu , v, menuInfo);
        Log.d("", map.getOriginPoint().toString());}

    @Override
    public  boolean  onContextItemSelected(MenuItem item) {
        return  super.onContextItemSelected(item) ||  map.onContextItemSelected(item);}

    }