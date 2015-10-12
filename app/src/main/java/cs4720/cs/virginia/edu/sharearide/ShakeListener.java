package cs4720.cs.virginia.edu.sharearide;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * Created by McNulty-PC on 10/11/2015.
 */

public class ShakeListener extends Activity implements SensorEventListener {

    private float prevAccel = SensorManager.GRAVITY_EARTH;
    private float curAccel =  SensorManager.GRAVITY_EARTH;
    private float neutralAccel = 0.00f;
    private Activity parent;
    public ShakeListener (Activity act)
    {
        super();
        this.parent=act;
    }
    public void onSensorChanged(SensorEvent se) {

        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];
        prevAccel = curAccel;
        curAccel = (float) Math.sqrt((double) (x*x + y*y + z*z));
        float delta = curAccel - prevAccel;
        neutralAccel = neutralAccel * 0.9f + delta*0.1f;

        if (neutralAccel > 2 )
        {
            //onShake();
            ((EventListActivity)parent).onShake();
        }
    }
    public float getAccel()
    {
        return neutralAccel;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
