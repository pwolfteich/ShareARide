package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import android.view.inputmethod.InputMethodManager;

public class InviteFriends extends AppCompatActivity {

    ArrayList<String> friendsList;
    ArrayList<String> invitedFriends;
    InviteListAdapter inviteAdapter;
    FriendStorageHelper friendHelper;

    ArrayList<ParseUser> friendUserList;
    ArrayList<ParseUser> invitedFriendUserList;

    Event currentEvent;

    int friendFetchCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_invite_friends);
        ListView listEvents = (ListView) findViewById( R.id.listView );
        //eventListAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, eventTitles);
        Intent startingIntent = getIntent();
        String name = startingIntent.getStringExtra("eventName");
        String customBanner = getResources().getString(R.string.invite_to_event_banner, name);
        TextView tv = (TextView) findViewById(R.id.textView14);
        tv.setText(customBanner);
        friendsList = new ArrayList<String>();
        invitedFriends = new ArrayList<String>();
        friendHelper = new FriendStorageHelper("friends",this);
        friendsList.addAll(friendHelper.readFriends());

        friendUserList = new ArrayList<ParseUser>();
        invitedFriendUserList = new ArrayList<ParseUser>();
        
        inviteAdapter = new InviteListAdapter(this,friendsList,invitedFriends);
        inviteAdapter.notifyDataSetChanged();
        listEvents.setAdapter(inviteAdapter);
        loadFriends();
        //inviteAdapter = new InviteListAdapter(this, friendsList, invitedFriends);

        inviteAdapter.notifyDataSetChanged();
        listEvents.setAdapter(inviteAdapter);

    }

    @Override
    public void onResume() {
        super.onResume();

        TextView textView = (TextView)findViewById(R.id.searchFriendField);

    }

    public void loadFriends()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> friends = (ArrayList<ParseUser>)currentUser.get("friends");
        if (friends.size() > 0) {
            for (ParseUser user : friends) {
                user.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {
                        // one more friend fetched
                        friendFetchCount++;
                        // check if all friends are fetched
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        ArrayList<ParseUser> friends = (ArrayList<ParseUser>) currentUser.get("friends");
                        // if all friends fetched
                        // update UI
                        if (friendFetchCount == friends.size()) {
                            loadListView();
                            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

                Log.v(InviteFriends.class.getName(), user.getObjectId());
            }
        } else {
            ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    public void loadListView() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> friends = (ArrayList<ParseUser>)currentUser.get("friends");
        friendsList.clear();
        for (int i = 0; i < friends.size(); i++) {
            ParseUser friend = friends.get(i);
            if (!friendsList.contains(friend.getUsername())) {
                friendsList.add(friend.getUsername());
            }
            if (!friendUserList.contains(friend)) {
                friendUserList.add(friend);
            }
        }
        friendHelper.setFriends(friendsList);
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invite_friends, menu);
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

    public void addFriend(View view)
    {
        TextView tv = (TextView) findViewById(R.id.editText6);
        String newFriendName = tv.getText().toString();
        if (!friendsList.contains(newFriendName))
        {
            //friendsList.add(newFriend);
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereEqualTo("username", newFriendName);
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> list, ParseException e) {
                    if (e == null) {
                        Context context = getApplicationContext();
                        CharSequence text;
                        int duration = Toast.LENGTH_SHORT;

                        if (list.size() == 0) {
                            text = "Nobody with that username.";
                        } else if (list.size() == 1) {
                            text = "Found friend!";
                            // add friend to database
                            ParseUser friend = list.get(0);
                            addNewFriend(friend);
                            //------------
                            loadFriends();
                        } else {
                            text = "More than one friend with that username.";
                        }
                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    } else {

                    }
                }
            });
        }
        //inviteAdapter.notifyDataSetChanged();
        //friendHelper.addFriend(newFriendName);
    }

    public void addNewFriend(ParseUser friend) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> friends = (ArrayList<ParseUser>)currentUser.get("friends");
        friends.add(friend);
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Context context = getApplicationContext();
                CharSequence text;
                int duration = Toast.LENGTH_SHORT;
                if (e == null) {
                    text = "Added friend!";
                    TextView textView = (TextView)findViewById(R.id.editText6);
                    textView.setText("");

                    // need this for some reason
                    EditText editText = (EditText)findViewById(R.id.searchFriendField);
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                } else {
                    text = "Failed to add friend :(";
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        // add friend to database

        //inviteAdapter.notifyDataSetChanged();
        //friendHelper.addFriend(friend.getUsername());
    }

    public void inviteButtonTapped(View view) {

        // get button
        Button inviteButton = (Button)view;
        inviteButton.setEnabled(false);
        // get event
        RideApplication app = (RideApplication)getApplication();
        Event currentEvent = app.getCurrentEvent();
        ArrayList<ParseUser> invitees = (ArrayList<ParseUser>)currentEvent.get("invitees");
        invitees.addAll(invitedFriendUserList);
        //currentEvent.setInvitees(invitees);
        currentEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.v(InviteFriends.class.getName(), "Invited friends to event.");
                    // toast
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Invited friends.", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                } else {
                    Log.e(InviteFriends.class.getName(), "Failed to invite friends to event.");
                }
            }
        });
    }

    public void inviteCheckBoxTapped(View view) {
        CheckBox checkBox = (CheckBox)view;
        ListView listView = (ListView) findViewById(R.id.listView);

        Log.v(InviteFriends.class.getName(), "check box " + checkBox);
        Log.v(InviteFriends.class.getName(), "check box parent " + checkBox.getParent());
        Log.v(InviteFriends.class.getName(), "check box parent parent " + checkBox.getParent().getParent());
        for (int i = 0; i < listView.getChildCount(); i++) {
            View child = listView.getChildAt(i);
            if (child == checkBox.getParent()) { // get index of checkbox

                // get friend
                ParseUser friend = friendUserList.get(i);

                if (checkBox.isChecked()) {
                    invitedFriendUserList.add(friend);
                } else {
                    invitedFriendUserList.remove(friend);
                }
                Log.v(InviteFriends.class.getName(), "Selected " + i);
            }
        }

        Log.v(InviteFriends.class.getName(), "Invited count " + invitedFriendUserList.size());

        for (int i = 0; i < invitedFriendUserList.size(); i++) {
            ParseUser friend = invitedFriendUserList.get(i);
            Log.v(InviteFriends.class.getName(), "Invited friend: " + i + " " + friend.getUsername());
        }

    }

    /*
    public void invite(View view)
    {
        String name =(String) view.getTag();
        boolean add = ((CheckBox) view).isChecked();
        if(invitedFriends.contains(name))
        {
            if( !add )
            {
                invitedFriends.remove(name);
            }
        }
        else if (add)
        {
            invitedFriends.add(name);
        }

    }
    */
}
