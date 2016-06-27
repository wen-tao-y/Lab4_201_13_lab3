package lab3_201_13.ca.uwaterloo.ca.lab3_201_13;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

/**
 * Created by ywt on 6/24/16.
 */
public class OrientationManager {



    private TextView directionLabel;
    private float[] R = new float[9];
    float[] Rvalues = new float[3];
    private float[] gravity = new float[3];
    private float[] mag = new float[3];

    public class GravSensorEventListener implements SensorEventListener {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // TODO Auto-generated method stub
            gravity = event.values.clone();
            OrientationManager.this.updateDirection();

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
            OrientationManager.this.updateDirection();

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // TODO Auto-generated method stub

        }


    }
    public OrientationManager( TextView directionLabel){
        this.directionLabel = directionLabel;

    }

    public float getAzimuth(){
        // return the value of the azimuth reading in radians
        // 0 is north, positive for clockwise rotations.
        return Rvalues[0];
    }



    public void updateDirection(){
		/* returns the orientation of the phone ,
		 * after appropriate filtering
		 * and calibrations
		 */
        SensorManager.getRotationMatrix(R, null, gravity, mag);
        SensorManager.getOrientation(R, Rvalues);



		/* updated directionLabel to current orientation */
        directionLabel.setText(String.format("Orientation: %.2f , %.2f , %.2f", Rvalues[0]*(180.0f/(3.14159)), Rvalues[1], Rvalues[2]));


    }
}
