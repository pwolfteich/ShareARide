package cs4720.cs.virginia.edu.sharearide;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    String username;
    ArrayList<String> eventsList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Intent startingIntent = getIntent();
        username = startingIntent.getStringExtra ("username");
        // Setting the banner welcoming the user
        String customBanner = getResources().getString(R.string.event_list_banner, username);
        TextView tv = (TextView) findViewById(R.id.textView3);
        tv.setText(customBanner);

        loadPrefs();
        // Retrieving user's events

        //setSilent(silent);
        //String strMeatMsg = String.format(strMeatFormat, numPoundsMeat);
    }
    public void loadPrefs()
    {
        SharedPreferences settings = getSharedPreferences(username, 0);
        String name = settings.getString("name", "");
        String date = settings.getString("date", "");
        String location = settings.getString("loc","");
        String description = settings.getString("desc","");
        if( eventsList.contains(name) == false)
            eventsList.add(name);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
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
    public void logOut(View view)
    {
        Intent logOutEvent = new Intent(this,LogInActivity.class);
        startActivity(logOutEvent);
    }
    public void createEvent(View view) {

        Intent hostEventIntent = new Intent(this, HostEventActivity.class);
        hostEventIntent.putExtra("username", username);
        startActivity(hostEventIntent);
    }
}
