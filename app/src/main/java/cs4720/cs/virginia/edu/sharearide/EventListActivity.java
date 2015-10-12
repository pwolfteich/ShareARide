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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
        isShaken.registerListener(accelerometer ,isShaken.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

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
                    if (eventList.get(ctr).getId() == ev.getId()) {
                        eventList.get(ctr).setExpanded(!ev.isExpanded());
                        break;
                    }
                }

                listEvents.setClickable(true);

                eventListAdapter.notifyDataSetChanged();
                /*TextView txt_view = (TextView) view.findViewById(R.id.);

                txt_view.setVisibility(View.GONE);

                RelativeLayout rl_inflate = (RelativeLayout) view.findViewById(R.id.
                View child = getLayoutInflater().inflate(R.layout.inflate);
                rl_inflate.addView(child);

/*
                Button my_btn = (Button) child.findViewById(R.id.btn_replace);
                EditText enter_txt = (EditText) child.findViewById(R.id.enter_txt);

                my_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        txt_view.setText(enter_txt.getText().toString());
                        txt_view.setVisibility(View.VISIBLE);
                    }
                });*/
            }
        });

//        ParseObject obj = new ParseObject("TestObject");
//        obj.put("foo", "new object");
//        obj.pinInBackground();

//        ParseQuery<ParseObject> query = ParseQuery.getQuery("TestObject");
//        query.whereEqualTo("foo", "new object");
//        query.fromLocalDatastore();
//        query.findInBackground(new FindCallback<ParseObject>() {
//            public void done(List<ParseObject> list,
//                             ParseException e) {
//                if (e == null) {
//                    Log.d("score", "Retrieved " + list.size());
//                    ParseObject obj = list.get(0);
//                    Log.v(EventListActivity.class.getName(), obj.getString("foo"));
//
//                } else {
//                    Log.d("score", "Error: " + e.getMessage());
//                }
//            }
//        });

    }

    @Override
    public void onResume()
    {
        super.onResume();

        loadEvents();

        // check if user is logged in
        ParseUser user = ParseUser.getCurrentUser();
        if (user == null) {
            // login
            Log.v(EventListActivity.class.getName(), "User not logged in");
            Intent logInIntent = new Intent(this, LoginActivity.class);
            startActivity(logInIntent);
        } else {
            Log.v(EventListActivity.class.getName(), "User logged in");


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
        Event event = (Event) view.getTag();
        rsvpEvent.putExtra("eventId", event.getId());
        startActivity(rsvpEvent);
    }
    public void inviteToEvent(View view)
    {
        Intent inviteEvent = new Intent(this,InviteFriends.class);
        //EventListAdapter  ela= (EventListAdapter)(view.getParent());
        Event event = (Event) view.getTag();
        inviteEvent.putExtra("eventName", event.getName());
        startActivity(inviteEvent);
    }
}
