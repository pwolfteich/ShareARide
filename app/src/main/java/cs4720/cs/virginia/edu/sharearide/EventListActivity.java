package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.GetCallback;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.*;

public class EventListActivity extends AppCompatActivity {

    String username;
    ArrayList<Event> eventList;
    ArrayList<String> eventTitles;
    EventsDbHelper dbHelper;
    ListView listEvents;
    EventListAdapter eventListAdapter;
    SensorManager isShaken;
    SensorEventListener accelerometer;

    int chosenCellIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        Intent startingIntent = getIntent();
        username = startingIntent.getStringExtra("username");
        dbHelper = EventsDbHelper.getInstance(this);
        eventList = new ArrayList<Event>();
        eventTitles = new ArrayList<String>();

        //TODO unregister and reregister on pause and resume
        accelerometer = new ShakeListener(this);
        isShaken = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        isShaken.registerListener(accelerometer, isShaken.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        // Setting the banner welcoming the user
//        String customBanner = getResources().getString(R.string.event_list_banner, username);
//        TextView tv = (TextView) findViewById(R.id.textView3);
//        tv.setText(customBanner);

        listEvents = (ListView) findViewById(R.id.eventsListView);
        //eventListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, eventTitles);
        eventListAdapter = new EventListAdapter(this, eventList);
        loadEvents();
        // Retrieving user's events
        /*
        eventListAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, eventTitles);
        listEvents.setAdapter(eventListAdapter);*/
        listEvents.setLongClickable(true);
        listEvents.setClickable(true);
        listEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                //TextView tv = (TextView) ((ListView) arg0).getChildAt(pos);
                //String name = tv.getText().toString();
                Event ev = (Event) arg0.getItemAtPosition(pos);
                String name = ev.toString();
                int eventId = -1;
                for (Event e : eventList) {
                    if (e.getName().equals(name)) {
                        eventId = e.getId();
                        break;
                    }
                }
                if (eventId == -1) {

                    Log.e("No id found", "Event named " + name + " did not have a corresponding name in the database");
                } else {
                    dbHelper.deleteEvent(eventId);
                    loadEvents();
                }
                return true;
            }
        });

        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Event ev = (Event) parent.getItemAtPosition(position);
                String name = ev.toString();
                int eventId = -1;
                for (int ctr = 0; ctr < eventList.size(); ctr++) {

                    if (eventList.get(ctr).getName().equals(ev.getName())) {
                        if (!ev.isExpanded()) {
                            eventList.get(ctr).setExpanded(true);
                            chosenCellIndex = ctr;
                        } else {
                            eventList.get(ctr).setExpanded(false);
                            chosenCellIndex = -1;
                        }
                    } else {
                        eventList.get(ctr).setExpanded(false);
                    }

                }

                listEvents.setClickable(true);

                eventListAdapter.notifyDataSetChanged();
            }
        });

    }

    public void fetchEvents() {

        // fetch events from the server
        // that have the current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        // as an invitee, passenger, driver, or host
        ParseQuery<ParseObject> isInvitee = ParseQuery.getQuery("Event");
        isInvitee.whereEqualTo("invitees", currentUser);
        ParseQuery<ParseObject> isPassenger = ParseQuery.getQuery("Event");
        isPassenger.whereEqualTo("passengers", currentUser);
        ParseQuery<ParseObject> isDriver = ParseQuery.getQuery("Event");
        isDriver.whereEqualTo("drivers", currentUser);
        ParseQuery<ParseObject> isHost = ParseQuery.getQuery("Event");
        isHost.whereEqualTo("host", currentUser);

        // combine all conditions
        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(isInvitee);
        queries.add(isPassenger);
        queries.add(isDriver);
        queries.add(isHost);

        // query for events
        ParseQuery<ParseObject> query = ParseQuery.or(queries);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) { // success

                    // update events
                    updateEvents(list);

                    Toast toast = Toast.makeText(getApplicationContext(), "Fetched events!",
                            Toast.LENGTH_SHORT);
                    toast.show();

                } else { // error
                    Log.e(EventListActivity.class.getName(), "Failed to get events from server.");
                    Toast toast = Toast.makeText(getApplicationContext(), "Failed to fetch events from the server",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void updateEvents(List<ParseObject> list) {

        for (ParseObject obj : list) {
            Log.v(EventListActivity.class.getName(), "Events from the cloud " + obj.get("name") + " " + obj.getObjectId());
        }
        for (Event e : eventList) {
            Log.v(EventListActivity.class.getName(), "Events from the database " + e.get("name") + " " + e.getObjectId());
        }
        // merge list with current list
        // actually, just replace the current list
        eventList.clear();
        for (ParseObject obj : list) {
            // get new event
            Event newEvent = (Event)obj;
            eventList.add(newEvent);
        }
        // notify event list adapter
        eventListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        loadEvents();
        fetchEvents();

        // check if user is logged in
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            // login
            Log.v(EventListActivity.class.getName(), "User not logged in");
            Intent logInIntent = new Intent(this, LoginActivity.class);
            startActivity(logInIntent);
        } else {
            Log.v(EventListActivity.class.getName(), "User logged in");

            // refresh user
            user.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    if (e == null) {
                        ParseUser user = (ParseUser)parseObject;
                        Log.v(EventListActivity.class.getName(), "Refreshed user with objectId " + user.getObjectId());

                        ArrayList<ParseUser> friends = (ArrayList<ParseUser>)user.get("friends");
                        for (ParseUser friend : friends) {
                            Log.v(EventListActivity.class.getName(), friend.getObjectId());
                        }

                    } else {
                        Log.e(EventListActivity.class.getName(), "Error refreshing user.");
                    }
                }
            });
        }
    }
    public void onShake()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_LONG);
        toast.show();
        this.refreshEvents();
    }

    public void refreshEvents()
    {
        fetchEvents();
    }

    public void loadEvents()
    {

        //eventList = dbHelper.getEvents();
        eventList.clear();
        eventList.addAll(dbHelper.getEvents());
        eventTitles.clear();
        for(Event e: eventList)
        {
            eventTitles.add(e.getName());
        }
        if(eventTitles.size() == 0) {
            eventTitles.add("No upcoming events");
        }
        //eventListAdapter.updateData(eventList);
        eventListAdapter.notifyDataSetChanged();
        listEvents.setAdapter(eventListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.menu_event_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_add:
                createEvent();
                // add

                return true;
            case R.id.action_refresh:
                refreshEvents();
                // refresh

                return true;
            case R.id.action_logout:
                logOut();
                // log out
                
                return true;
            case R.id.action_settings:
                // settings

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
    @Override
    public void onResume()
    {
        super.onResume();
        loadEvents();
    }
    */

    public void logOut()
    {
        // remove database

        // log out of parse
        ParseUser.logOut();
        //possibly replace this with finish

        FriendStorageHelper friendHelper = new FriendStorageHelper("friends",this);
        friendHelper.deleteFile();
        Intent logOutEvent = new Intent(this,LoginActivity.class);
        startActivity(logOutEvent);
    }

    public void createEvent() {

        Intent hostEventIntent = new Intent(this, HostEventActivity.class);

        startActivity(hostEventIntent);
    }

    public void respondEvent(View view) {
        Intent rsvpEvent = new Intent(this,RsvpActivity.class);
        //EventListAdapter  ela= (EventListAdapter)(view.getParent());
        Event event = eventList.get(chosenCellIndex);
        Log.v(EventListActivity.class.getName(), "RSVP to event: " + event);
        // set event as the current event
        RideApplication app = (RideApplication)getApplication();
        app.setCurrentEvent(event);
        // add to intent
        rsvpEvent.putExtra("eventId", event.getId());
        startActivity(rsvpEvent);
    }

    public void inviteToEvent(View view)
    {
        Intent inviteEvent = new Intent(this, InviteFriends.class);
        // index is the chosen cell index
        // get the event
        Event event = eventList.get(chosenCellIndex);
        Log.v(EventListActivity.class.getName(), "Invite to event: " + event);
        // set event as the current event
        RideApplication app = (RideApplication)getApplication();
        app.setCurrentEvent(event);
        //Log.v(EventListActivity.class.getName(), event.getObjectId());
        // add to intent
        inviteEvent.putExtra("eventName", event.getName());
        startActivity(inviteEvent);
    }
    
}
