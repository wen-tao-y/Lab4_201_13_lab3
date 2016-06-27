package lab3_201_13.ca.uwaterloo.ca.lab3_201_13;

import android.hardware.SensorEventListener;
import android.widget.TextView;
import java.io.FileOutputStream;

import java.io.IOException;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;

import ca.uwaterloo.sensortoy.LineGraphView;


/**
 * Created by Matthew on 2016-06-24.
 */
public class AccSensorEventListener implements SensorEventListener {
    public enum stepState{
        atRest, startStep, stepPeak, stepDescent, stepRebound
    }

    TextView output;
    TextView stepView;
    LineGraphView graph;


    private OrientationManager orientationManager;

    private boolean acceTooGreat = false;

    private stepState currentState;




    private int step = 0;
    private long totalTime;
    private float initialAzimuth = 0 ;
    private final int maxSampleCount = 4;
    private float[] azimuthSamples = new float[maxSampleCount];
    private boolean initialSet = false;
    private int sampleCount = 0;


    private float distanceN = 0;
    private float distanceE = 0;
    private float direction = 0; // current orienation in radians


    private float[] lowPassOut;


    private String sensorString;
    private String sensorValString;

    private String displacementString = "Displacement: \n";

    public AccSensorEventListener( TextView outputView, TextView stepView,LineGraphView graph , OrientationManager orientationManager )
    {


        this.output = outputView;
        this.stepView = stepView;
        this.graph = graph;
        this.currentState = stepState.atRest;



        this.orientationManager = orientationManager;

        // Change initial label to correspond to Sensor being recorded.
        sensorString = "\nAcclerometer Reading:";
    }

    // Resets all record values to 0;
    public void clearRecords()
    {
        distanceN = 0;
        distanceE = 0;
    }

    public void onAccuracyChanged(Sensor s, int i) {}

    public float[] lowPassFilter(float[] in, float[] out)
    {
        float a = 0.25f;
        if (out == null ) return in;
        for ( int i = 0; i <in.length; i++ ) {
            out[i] = out[i] + a *(in[i] - out[i]);
            //out[i] += (in[i] - out[i]) / a;
        }
        return out;
    }

    public void updateOrientation( float Rvalue) {
        // takes an initial orientation
        // then stores the subsequent 10 samples distance from initial into an array
        // distance is averaged, orientation becomes

        if (initialAzimuth == 0) {
            direction = orientationManager.getAzimuth();
        }


        if (!initialSet) {
            initialAzimuth = direction;
            initialSet = true;
            return;
        }

        if (sampleCount < maxSampleCount) {
            azimuthSamples[sampleCount] = Rvalue - initialAzimuth;
            sampleCount++;
        } else {


            float sum = 0;
            float min = 100f;
            float max = -100f;

            for (int i = 0; i < sampleCount; i++) {
                if (i == 0) {
                    min = azimuthSamples[i];
                    max = azimuthSamples[i];
                } else {
                    if (azimuthSamples[i] < min)
                        min = azimuthSamples[i];
                    else if (azimuthSamples[i] > max)
                        max = azimuthSamples[i];

                    sum += azimuthSamples[i];
                }
                sum -= min;
                sum -= max;
                float avgDiff = (sum / (float) (maxSampleCount - 2));


                if (Math.abs(avgDiff) > 1) {
                    // don't smooth if user is detected to be changing drastic direction (initially)
                    direction = initialAzimuth + avgDiff;
                } else {
                    // smooth if user is heading the same relative direction
                    direction += ((initialAzimuth + avgDiff) - direction) / 2.5f;
                }


                // technically not  needed
                while (direction > Math.PI) {
                    direction -= 2 * Math.PI;
                }
                while (direction < -Math.PI) {
                    direction += 2 * Math.PI;
                }
                sampleCount = 0;
                initialSet = false;
            }


        }

    }


    public void onSensorChanged(SensorEvent se) {
        lowPassOut = lowPassFilter(se.values.clone(), lowPassOut);
        se.values[0] = lowPassOut[0];
        se.values[1] = lowPassOut[1];
        se.values[2] = lowPassOut[2];
        lowPassOut[0] = direction;
        direction=orientationManager.getAzimuth();

			/*
			if ( autoCount == 2) {
				updateOrientation(orientationManager.getAzimuth());
				autoCount = 0;
			} else {
				autoCount++;
			}
			*/


        // raw data graph
        //graph.addPoint(se.values);

        // low pass filter graph
        graph.addPoint(lowPassOut);



        switch (currentState)
        {
            case atRest:
                updateOrientation(orientationManager.getAzimuth());
                if ( (lowPassOut[2] > 0.35) && (Math.abs(lowPassOut[1]) > 0.1f)) {
                    currentState = stepState.startStep;
                    totalTime = System.currentTimeMillis();
                }
                break;
            case startStep:
                updateOrientation(orientationManager.getAzimuth());
                if ( lowPassOut[2] < 0.35 )
                    currentState = stepState.atRest;
                else if ( (lowPassOut[2] > 1.3f && lowPassOut[2] < 7) && (Math.abs(lowPassOut[1]) > 0.35f )) {
                    currentState = stepState.stepPeak;
                }
                break;
            case stepPeak:
                if ( lowPassOut[2] > 10.0f){
                    acceTooGreat = true;
                }
                else if ( lowPassOut[2] < 1.3f) {
                    currentState = stepState.stepDescent;
                }
                break;
            case stepDescent:
                if  ( lowPassOut[2] > 1.3f){
                    currentState = stepState.stepPeak;
                } else if ( lowPassOut[2] < -0.25f ){
                    currentState = stepState.stepRebound;
                }
                break;
            case stepRebound:
                if ( lowPassOut[2] > -0.25f) {

                    // how much time has passed since startStep was initiated
                    totalTime = System.currentTimeMillis() - totalTime;
                    if ( totalTime > 200 && !acceTooGreat){
                        // time for step was less than 90ms
                        // unreasonable for human being, reset state without updating counter
                        step++;


                        double headingNS = Math.cos((double)direction);
                        double headingEW = Math.sin((double)direction);
                        distanceN += headingNS;
                        distanceE += headingEW;
                    }
                    currentState = stepState.atRest;
                    totalTime = 0;
                    acceTooGreat = false;

                }
                break;
        }

        stepView.setText(String.format("Step Count: %d" , step));
        sensorValString = String.format("\n x: %.2f y: %.2f z: %.2f", se.values[0], se.values[1], se.values[2]);
        displacementString = String.format("\nDisplacement: \n N: %.5f E: %.5f ", distanceN , distanceE );

        output.setText( displacementString  + String.format("\ndirection: %f", direction*(180f/3.14159f)) + sensorString+sensorValString );
    }

    public void resetCounter()
    {
        step = 0;
    }
}
