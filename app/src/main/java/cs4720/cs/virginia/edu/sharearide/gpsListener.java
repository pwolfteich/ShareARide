package cs4720.cs.virginia.edu.sharearide;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by McNulty-PC on 9/15/2015.
 */
public class gpsListener implements LocationListener{
    public Location lastKnownLocation;
    public void onLocationChanged(Location location) {
        // Called when a new location is found by the network location provider.
        Log.v("gpsListener","Got new location\n");
        lastKnownLocation = location;
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {}

    public void onProviderEnabled(String provider) {}

    public void onProviderDisabled(String provider) {}

    public Location getLastKnownLocation ()
    {
        return lastKnownLocation;
    }
}
