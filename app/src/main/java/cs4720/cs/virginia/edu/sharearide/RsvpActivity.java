package cs4720.cs.virginia.edu.sharearide;

import android.content.Intent;
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

        setDisplaysVisible(false, false);
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
        tv.setVisibility( (vis) ? View.VISIBLE : View.INVISIBLE);
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

        TextView tv = (TextView) findViewById(R.id.textView12);
        tv.setText("Your riders are: ");
        setDisplaysVisible(true, true);
    }
    public void riding(View view)
    {
        RadioButton rid = (RadioButton) findViewById(R.id.drivingButton);
        rid.setChecked(false);

        TextView tv = (TextView) findViewById(R.id.textView12);
        tv.setText("Your driver is: ");
        setDisplaysVisible(false, true);
    }
}
