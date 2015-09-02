package team5.ad.sa40.stationeryinventory;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity implements AdapterView.OnClickListener {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "11233:hello", "11234:world"
    };

    // UI references.
    private AutoCompleteTextView mUseridView;
    private EditText mPasswordView;
    private View mLoginFormView;
    private TextView mStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUseridView = (AutoCompleteTextView) findViewById(R.id.userid);
        mPasswordView = (EditText) findViewById(R.id.password);
        mStatus = (TextView) findViewById(R.id.textViewStatus);
        mLoginFormView = findViewById(R.id.login_form);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Reset errors.
        mUseridView.setError(null);
        mPasswordView.setError(null);
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
            mPasswordView.setError(getString(R.string.error_field_required));
            Log.e("password field blank:",getString(R.string.error_field_required));
            focusView = mPasswordView;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(mUseridView.getText().toString())) {
            mUseridView.setError(getString(R.string.error_field_required));
            focusView = mUseridView;
        }

        if(!TextUtils.isEmpty(mPasswordView.getText().toString())&&!TextUtils.isEmpty(mUseridView.getText().toString())) {
            //attempt login:
            try {
                /*
                JSONObject user = new JSONObject();
                user.put("EmpID", mUseridView.getText().toString());
                user.put("Password", mPasswordView.getText().toString());
                String json = user.toString();
                String result = JSONParser.getStream(
                        String.format("%s/employee.svc/login", Setup.baseurl),
                        json);
                */

                String result = null;
                for (String credential : DUMMY_CREDENTIALS) {
                    String[] pieces = credential.split(":");
                    if (pieces[0].equals(mUseridView.getText().toString()) && (pieces[1].equals(mPasswordView.getText().toString()))) {
                        result = "HttpResponse_OK";
                    }
                }

                if (result == null || result != "HttpResponse_OK") {
                    mStatus.setText(getString(R.string.error_login_failed));
                    mUseridView.setError("");
                    mPasswordView.setError("");
                } else {
                    mStatus.setText("Logged in successfully.");
                    mUseridView.setError(null);
                    mPasswordView.setError(null);
                    Setup.empId = mUseridView.getText().toString();
                    Log.e("empId",Setup.empId);
                    Intent i = new Intent(this, MainActivity.class);
                    startActivity(i);
                }
            } catch (Exception e) {
                Log.e("login", "JSON error");
                mStatus.setText(getString(R.string.error_connection_failed));
            }
        }
    }

}

