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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class InviteFriends extends AppCompatActivity {

    ArrayList<String> friendsList;
    ArrayList<String> invitedFriends;
    InviteListAdapter inviteAdapter;

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

        loadFriends();
        inviteAdapter = new InviteListAdapter(this,friendsList,invitedFriends);

        inviteAdapter.notifyDataSetChanged();
        listEvents.setAdapter(inviteAdapter);

    }

    public void loadFriends()
    {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> friends = (ArrayList<ParseUser>)currentUser.get("friends");
        for (ParseUser user : friends) {
            user.fetchIfNeededInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject parseObject, ParseException e) {
                    // one more friend fetched
                    friendFetchCount++;
                    // check if all friends are fetched
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    ArrayList<ParseUser> friends = (ArrayList<ParseUser>)currentUser.get("friends");
                    // if all friends fetched
                    // update UI
                    if (friendFetchCount == friends.size()) {
                        loadListView();
                    }
                }
            });
        }
    }

    public void loadListView() {

        ParseUser currentUser = ParseUser.getCurrentUser();
        ArrayList<ParseUser> friends = (ArrayList<ParseUser>)currentUser.get("friends");
        for (int i = 0; i < friends.size(); i++) {
            ParseUser friend = friends.get(i);
            friendsList.add(friend.getUsername());
        }
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
                        CharSequence text = "";
                        int duration = Toast.LENGTH_SHORT;

                        if (list.size() == 0) {
                            text = "Nobody with that username.";
                        } else if (list.size() == 1) {
                            text = "Found friend!";
                            // add friend to database
                            ParseUser friend = list.get(0);
                            addNewFriend(friend);
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
        inviteAdapter.notifyDataSetChanged();
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
                } else {
                    text = "Failed to add friend :(";
                }
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });
        // add friend to database

    }

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
}
