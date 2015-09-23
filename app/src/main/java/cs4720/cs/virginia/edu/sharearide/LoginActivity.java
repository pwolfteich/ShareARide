package cs4720.cs.virginia.edu.sharearide;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //Should check if previously logged in (check local dbase), and if so auto login
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    public void login(View view) {


        TextView tv = (TextView)findViewById(R.id.textView2);
        String username = tv.getText().toString();
        Intent eventListIntent = new Intent(this,EventListActivity.class);
        eventListIntent.putExtra("username",username);
        startActivity(eventListIntent);
        /*
        tv.setText("");

        Location currentLocation = locListener.getLastKnownLocation();
        if (currentLocation != null) {
            inputMessage = inputMessage + " at " + currentLocation.getLatitude() + " degrees lat, " + currentLocation.getLongitude() + " degrees long";
        }
        tv = (TextView) findViewById(R.id.MessageOutput);
        tv.setText(inputMessage);
        */
    }
}