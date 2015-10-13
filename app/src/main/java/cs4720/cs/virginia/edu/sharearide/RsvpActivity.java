package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

public class RsvpActivity extends AppCompatActivity {

    EventsDbHelper dbHelper;
    Event event;
    SharedPreferences prefs;
    String resp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsvp);
        Intent startingIntent = getIntent();
        int id = startingIntent.getIntExtra("eventId", -1);
        //dbHelper = new EventsDbHelper(this);
        dbHelper = EventsDbHelper.getInstance(this);
        event = dbHelper.getEvent(id);

        prefs = this.getSharedPreferences("rsvp", Context.MODE_PRIVATE);
        if (prefs.contains("Response"))
        {
            RadioButton rid = null;

            if(prefs.getString("Response","").equals("riding")) {
                rid = (RadioButton) findViewById(R.id.ridingButton);
                setDisplaysVisible(false, true);
                resp = "riding";
            }
            else {
                rid = (RadioButton) findViewById(R.id.drivingButton);
                setDisplaysVisible(true, true);
                TextView tv = (TextView) findViewById(R.id.editText);
                int tmp = prefs.getInt("Seats",0);
                tv.setText(tmp+"");
                resp = "driving";
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
    }
    public void riding(View view)
    {
        RadioButton rid = (RadioButton) findViewById(R.id.drivingButton);
        rid.setChecked(false);
        resp = "riding";

        TextView tv = (TextView) findViewById(R.id.textView12);
        tv.setText("Your driver is: ");
        setDisplaysVisible(false, true);
    }
    public void respond(View view)
    {
        RadioButton rid = (RadioButton) findViewById(R.id.drivingButton);
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("Response",resp);
        TextView tv = (TextView) findViewById(R.id.editText);
        int seats = 0;
        try {
            seats = Integer.parseInt(tv.getText().toString());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        if(resp.equals("driving")) {
            edit.putInt("Seats",seats);
        }
        edit.commit();
        finish();
    }
}
