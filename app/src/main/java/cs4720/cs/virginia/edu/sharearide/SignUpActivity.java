package cs4720.cs.virginia.edu.sharearide;

import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

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
        TextView passwordField = (TextView)findViewById(R.id.passwordField);
        TextView reenterPasswordField = (TextView)findViewById(R.id.reenterPasswordField);
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String reenteredPassword = reenterPasswordField.getText().toString();

    }

}
