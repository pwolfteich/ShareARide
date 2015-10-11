package cs4720.cs.virginia.edu.sharearide;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * Created by harangju on 9/28/15.
 */
public class SignUpActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);
    }


    public void signUpButtonTapped(View view) {

        // get text
        TextView emailField = (TextView)findViewById(R.id.emailField);
        TextView usernameField = (TextView)findViewById(R.id.usernameField);
        TextView passwordField = (TextView)findViewById(R.id.passwordField);
        TextView reenterPasswordField = (TextView)findViewById(R.id.reenterPasswordField);
        String email = emailField.getText().toString();
        String username = usernameField.getText().toString();
        String password = passwordField.getText().toString();
        String reenteredPassword = reenterPasswordField.getText().toString();

        // passwords should be the same
        if (!password.equals(reenteredPassword)) {
            Context context = getApplicationContext();
            CharSequence text = "Passwords are not the same.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) { // sign up succeeded
                    setResult(LoginActivity.SIGN_UP_SUCCEEDED);
                    finish();
                } else { // sign up failed
                    Context context = getApplicationContext();
                    CharSequence text = "Sign Up Failed.";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    e.printStackTrace();
                }
            }
        });
    }

}
