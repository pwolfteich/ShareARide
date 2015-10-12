package cs4720.cs.virginia.edu.sharearide;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/**
 * Created by McNulty-PC on 10/11/2015.
 */
public class ShakeListener implements SensorEventListener {

    public void onSensorChanged(SensorEvent se) {
        /*
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        mAccelLast = mAccelCurrent;
        mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = mAccelCurrent - mAccelLast;
        mAccel = mAccel * 0.9f + delta; // perform low-cut filter
        */
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
