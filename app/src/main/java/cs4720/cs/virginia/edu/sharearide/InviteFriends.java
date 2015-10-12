package cs4720.cs.virginia.edu.sharearide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class InviteFriends extends AppCompatActivity {

    ArrayList<String> friendsList;
    ArrayList<String> invitedFriends;
    InviteListAdapter inviteAdapter;
    FriendStorageHelper friendHelper;
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

        loadFriends();
        inviteAdapter = new InviteListAdapter(this,friendsList,invitedFriends);

        inviteAdapter.notifyDataSetChanged();
        listEvents.setAdapter(inviteAdapter);

    }

    public void loadFriends()
    {
        /*
        friendsList.clear();
        friendsList.add("First friend");
        friendsList.add("second Friend");

        invitedFriends.add("First friend");*/
        friendsList = friendHelper.readFriends();
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
        String newFriend = tv.getText().toString();
        /*if(!friendsList.contains(newFriend))
        {
            friendsList.add(newFriend);
        }*/
        inviteAdapter.notifyDataSetChanged();
        friendHelper.addFriend(newFriend);
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
