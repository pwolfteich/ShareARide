package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class HostEventActivity extends AppCompatActivity {

    String username;
    EventsDbHelper dbHelper;
    gpsListener locListener;
    LocationManager locationManager;
    String oldLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_event);
        locListener = new gpsListener();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        } catch (SecurityException e) {
            e.printStackTrace();
            Log.w("MainScreen", "                                                SecurityException");

        }
        Intent startingIntent = getIntent();
        username = startingIntent.getStringExtra ("username");
        dbHelper = new EventsDbHelper(this);
        oldLocation="Location";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_host_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void checkedUseLocation(View view)
    {
        TextView tv = (TextView)findViewById(R.id.editText4);
        String msg=oldLocation;
        //tv.setText("");

        final CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        if (checkBox.isChecked()) {
            oldLocation= tv.getText().toString();
            Location currentLocation = locListener.getLastKnownLocation();
            if (currentLocation != null) {
                msg = currentLocation.getLatitude() + " degrees lat, " + currentLocation.getLongitude() + " degrees long";
            }
        }
        else {
            msg = oldLocation;
        }

        tv.setText(msg);
    }

    public void makeEvent(View view)
    {
        Event newEvent;
        TextView tv = (TextView)findViewById(R.id.editText2);
        String name = tv.getText().toString();
        tv = (TextView)findViewById(R.id.editText3);
        String date = tv.getText().toString();
        tv = (TextView)findViewById(R.id.editText4);
        String loc = tv.getText().toString();
        tv = (TextView)findViewById(R.id.editText5);
        String desc = tv.getText().toString();
        newEvent = new Event(0,name,date,loc,desc);
        dbHelper.addEvent(newEvent);
        finish();
    }
}
