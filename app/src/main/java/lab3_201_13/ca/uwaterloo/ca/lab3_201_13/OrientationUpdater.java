package lab3_201_13.ca.uwaterloo.ca.lab3_201_13;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by ywt on 6/24/16.
 */
public class OrientationUpdater {



    private TextView outputView;
    private float[] R = new float[9];
    float[] values = new float[3];
    private float[] grav = new float[3];
    private float[] mag = new float[3];

    public class GravSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            grav = event.values.clone();
            OrientationUpdater.this.updateDirection();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }


    }

    public class MagSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            mag = event.values.clone();
            OrientationUpdater.this.updateDirection();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }


    }
    public OrientationUpdater( TextView outputView){
        this.outputView = outputView;

    }

    public float getAzimuth(){
        return values[0];
    }



    public void updateDirection(){

        SensorManager.getRotationMatrix(R, null, grav, mag);
        SensorManager.getOrientation(R, values);
        outputView.setText(String.format("Orientation: %.2f , %.2f , %.2f", values[0]*(180.0f/(3.14159)), values[1], values[2]));


    }
}
