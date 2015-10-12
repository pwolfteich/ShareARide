package cs4720.cs.virginia.edu.sharearide;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.parse.*;

public class LoginActivity extends AppCompatActivity {

    public static int SIGN_UP_SUCCEEDED = 1;

    static String LogTag = "LogInActivityLogTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // log
        Log.v(LogTag, "LogInActivity onCreate()");

        setContentView(R.layout.activity_login);
        //Should check if previously logged in (check local dbase), and if so auto login
        //Will do with sharedPreferences

        //ParseObject testObject = new ParseObject("TestObject");
        //testObject.put("foo", "bar");
        //testObject.saveInBackground();
        Log.v(LogTag, "LogInActivity saved test Parse object");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_login, menu);
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

    //
    // Buttons

    public void logInButtonTapped(View view) {

        // get username
        TextView usernameView = (TextView)findViewById(R.id.usernameTextView);
        String username = usernameView.getText().toString();
        TextView passwordView = (TextView)findViewById(R.id.passwordField);
        String password = passwordView.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Log.v(LoginActivity.class.getName(), "User logged in.");
                    finish();
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    // Show toast
                    Context context = getApplicationContext();
                    CharSequence text = "Log In Failed.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
    }

    public void signUpButtonTapped(View view) {
        Log.v("LogInActivity", "Sign up button tapped");
        // start sign up activity
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivityForResult(signUpIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == SIGN_UP_SUCCEEDED) {
            Log.v(LoginActivity.class.getName(), "Sign up succeeded.");
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

}
