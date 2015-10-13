package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class RsvpActivity extends AppCompatActivity {

    EventsDbHelper dbHelper;
    Event event;
    SharedPreferences prefs;
    String resp;
    int seats = 0;
    Ride ride;

    String responseKey = "Response_";

    boolean isDriver = false;
    boolean isPassenger = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp);
        Intent startingIntent = getIntent();
        int id = startingIntent.getIntExtra("eventId", -1);
        //dbHelper = new EventsDbHelper(this);
        dbHelper = EventsDbHelper.getInstance(this);
        //event = dbHelper.getEvent(id);
        RideApplication app = (RideApplication)getApplication();
        event = app.getCurrentEvent();

        responseKey = responseKey + event.getName() + ParseUser.getCurrentUser().getObjectId();

        prefs = this.getSharedPreferences("rsvp", Context.MODE_PRIVATE);
        if (prefs.contains(responseKey))
        {
            RadioButton rid = null;

            String value = prefs.getString(responseKey,"");

            if (value.equals("riding")) {
                rid = (RadioButton) findViewById(R.id.ridingButton);
                setDisplaysVisible(false, true);
                resp = "riding";
                isPassenger = true;

                freezeControls();
            }
            else if (value.equals("driving")) {
                rid = (RadioButton) findViewById(R.id.drivingButton);
                setDisplaysVisible(true, true);
                TextView tv = (TextView) findViewById(R.id.seatsField);
                int tmp = prefs.getInt("Seats",0);
                tv.setText(tmp+"");
                resp = "driving";
                isDriver = true;

                freezeControls();
            }

            rid.setChecked(true);
        }
        else {
            setDisplaysVisible(false, false);
        }

        fetchRide();

        populateFields();

    }

    public void fetchRide() {

        ParseQuery<Ride> query = ParseQuery.getQuery("Ride");
        query.whereEqualTo("passengers", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Ride>() {
            @Override
            public void done(List<Ride> list, ParseException e) {
                if (e == null) {

                    if (list.size() > 0) {
                        ride = list.get(0);
                        updateRideInfo();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Could not find driver for your ride.", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Could not find driver for your ride.", Toast.LENGTH_SHORT);
                    toast.show();
                    Log.e("tag", "error " + e.getMessage());
                }
            }
        });
    }

    public void updateRideInfo() {

        TextView textView = (TextView)findViewById(R.id.rideInfoTextView);

        if (isDriver) {

            CharSequence rideInfo = "You are driving!";
            textView.setText(rideInfo);

        } else if (isPassenger) {

            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.getInBackground(ride.getDriver().getObjectId(), new GetCallback<ParseUser>() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {
                        TextView textView = (TextView)findViewById(R.id.rideInfoTextView);
                        CharSequence rideInfo = "Your driver is " + parseUser.getUsername() + ".";
                        textView.setText(rideInfo);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Could not find driver.", Toast.LENGTH_SHORT);
                        toast.show();
                        Log.e(RsvpActivity.class.getName(), e.getMessage());
                    }
                }
            });
        }

    }

    public void populateFields()
    {
        TextView tv = (TextView) findViewById(R.id.editText2);
        tv.setText(event.getName());

        tv = (TextView) findViewById(R.id.editText3);
        tv.setText(event.getDate());

        tv = (TextView) findViewById(R.id.editText4);
        tv.setText(event.getLocation());

        tv = (TextView) findViewById(R.id.editText5);
        tv.setText(event.getDescription());
    }
    public void setDisplaysVisible(boolean vis, boolean selected)
    {
        TextView tv = (TextView) findViewById(R.id.textView13);
        tv.setVisibility((vis) ? View.VISIBLE : View.INVISIBLE);
        tv = (TextView) findViewById(R.id.seatsField);
        tv.setVisibility((vis) ? View.VISIBLE : View.INVISIBLE);
        tv = (TextView) findViewById(R.id.rideInfoTextView);
        tv.setVisibility((selected) ? View.VISIBLE : View.INVISIBLE);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_rsvp, menu);
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

    public void driving(View view)
    {
        RadioButton rid = (RadioButton) findViewById(R.id.ridingButton);
        rid.setChecked(false);
        resp="driving";

        TextView tv = (TextView) findViewById(R.id.rideInfoTextView);
        tv.setText("Your riders are: ");
        setDisplaysVisible(true, true);

        isDriver = true;
        isPassenger = false;
    }

    public void riding(View view)
    {
        RadioButton rid = (RadioButton) findViewById(R.id.drivingButton);
        rid.setChecked(false);
        resp = "riding";

        TextView tv = (TextView) findViewById(R.id.rideInfoTextView);
        tv.setText("Your driver is: ");
        setDisplaysVisible(false, true);

        isPassenger = true;
        isDriver = false;
    }

    public void respond(View view)
    {
        TextView tv = (TextView) findViewById(R.id.seatsField);
        try {
            seats = Integer.parseInt(tv.getText().toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            seats = -1;
        }

        if (isDriver) {
            if (seats <= 0) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You need seats in your car to drive people...", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                signUpAsDriver(seats);
            }
        } else if (isPassenger) {

            boolean alreadyPassenger = false;

            for (ParseUser passenger : event.getPassengers()) {
                if (passenger.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                    alreadyPassenger = true;
                    break;
                }
            }

            if (!alreadyPassenger) {
                signUpAsPassenger();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You are already signed up as a passenger.", Toast.LENGTH_SHORT);
                toast.show();
                Log.v(RsvpActivity.class.getName(), "Already a passenger.");
            }
        }

    }

    public void signUpAsDriver(int seats) {

        Log.v(RsvpActivity.class.getName(), "Will sign up as driver.");

        // not update drivers in events

        Ride ride = new Ride();
        ride.setDriver(ParseUser.getCurrentUser());
        ride.setSeats(seats);
        ride.setPassengers(new ArrayList<ParseUser>());
        ride.setEvent(event);
        ride.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {

                    // toast
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "RSVP as driver!", Toast.LENGTH_SHORT);
                    toast.show();

                    // preferences
                    savePreferences();

                    freezeControls();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Failed to RSVP as a driver.", Toast.LENGTH_SHORT);
                    toast.show();

                    unfreezeControls();
                }
            }
        });
    }

    public void signUpAsPassenger() {

        Log.v(RsvpActivity.class.getName(), "Will sign up as passenger.");

        Button button = (Button)findViewById(R.id.submitButton);
        button.setEnabled(false);

        // move current user to passengers and passengers without rides
        event.getPassengers().add(ParseUser.getCurrentUser());
        // save
        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(RsvpActivity.class.getName(), "Added to passengers.");

                    // toast
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "RSVP as passenger!", Toast.LENGTH_SHORT);
                    toast.show();

                    // preferences
                    savePreferences();

                    freezeControls();
                } else {

                    // enable button again
                    Button button = (Button)findViewById(R.id.submitButton);
                    button.setEnabled(true);
                    // toast
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Failed to RSVP as passenger.", Toast.LENGTH_SHORT);
                    toast.show();

                    unfreezeControls();
                }
            }
        });
    }

    public void savePreferences() {
        // save into preferences
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString(responseKey, resp);
        if (resp.equals("driving")) {
            edit.putInt("Seats", seats);
        }
        edit.commit();
    }

    public void freezeControls() {
        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setEnabled(false);
        EditText seatsField = (EditText)findViewById(R.id.seatsField);
        seatsField.setEnabled(false);
        RadioButton drivingButton = (RadioButton)findViewById(R.id.drivingButton);
        drivingButton.setEnabled(false);
        RadioButton ridingButton = (RadioButton)findViewById(R.id.ridingButton);
        ridingButton.setEnabled(false);
    }

    public void unfreezeControls() {
        Button submitButton = (Button)findViewById(R.id.submitButton);
        submitButton.setEnabled(true);
        EditText seatsField = (EditText)findViewById(R.id.seatsField);
        seatsField.setEnabled(true);
        RadioButton drivingButton = (RadioButton)findViewById(R.id.drivingButton);
        drivingButton.setEnabled(true);
        RadioButton ridingButton = (RadioButton)findViewById(R.id.ridingButton);
        ridingButton.setEnabled(true);
    }

}
