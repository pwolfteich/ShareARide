package cs4720.cs.virginia.edu.sharearide;

/**
 * Created by harangju on 9/28/15.
 */
import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;
import com.parse.Parse;
import com.parse.ParseObject;

public class RideApplication extends Application {

    static String LogTag = "RideApplicationLogTag";

    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    Event currentEvent;



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(LogTag, "Application onCreate()");

        // Parse
        //Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Ride.class);
        Parse.initialize(this, "Sh5dYjUxshRrnA3G1uHQua4LzisHV5o8eexJXu74", "r80yIRA4BOPx3Ex9SABv5C3FVu9l39CemDkyGc3d");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
