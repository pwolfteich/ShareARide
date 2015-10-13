package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import android.widget.Button;

public class RsvpActivity extends AppCompatActivity {

    EventsDbHelper dbHelper;
    Event event;
    SharedPreferences prefs;
    String resp;
    int seats = 0;

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

        prefs = this.getSharedPreferences("rsvp", Context.MODE_PRIVATE);
        if (prefs.contains("Response"))
        {
            RadioButton rid = null;

            if(prefs.getString("Response","").equals("riding")) {
                rid = (RadioButton) findViewById(R.id.ridingButton);
                setDisplaysVisible(false, true);
                resp = "riding";
                isPassenger = true;
            }
            else {
                rid = (RadioButton) findViewById(R.id.drivingButton);
                setDisplaysVisible(true, true);
                TextView tv = (TextView) findViewById(R.id.editText);
                int tmp = prefs.getInt("Seats",0);
                tv.setText(tmp+"");
                resp = "driving";
                isDriver = true;
            }

            rid.setChecked(true);
        }
        else {
            setDisplaysVisible(false, false);
        }
        populateFields();

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
        tv = (TextView) findViewById(R.id.editText);
        tv.setVisibility((vis) ? View.VISIBLE : View.INVISIBLE);
        tv = (TextView) findViewById(R.id.textView12);
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

        TextView tv = (TextView) findViewById(R.id.textView12);
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

        TextView tv = (TextView) findViewById(R.id.textView12);
        tv.setText("Your driver is: ");
        setDisplaysVisible(false, true);

        isPassenger = true;
        isDriver = false;
    }

    public void respond(View view)
    {
        TextView tv = (TextView) findViewById(R.id.editText);
        try {
            seats = Integer.parseInt(tv.getText().toString());
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        if (isDriver) {
            if (seats <= 0) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "You need seats in your car to drive people...", Toast.LENGTH_SHORT);
                toast.show();
            }
            signUpAsDriver(seats);
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

                    // save into preferences
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("Response", resp);
                    if (resp.equals("driving")) {
                        edit.putInt("Seats", seats);
                    }
                    edit.commit();

                } else {

                    // enable button again
                    Button button = (Button)findViewById(R.id.submitButton);
                    button.setEnabled(true);
                    // toast
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Failed to RSVP as passenger.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

}
