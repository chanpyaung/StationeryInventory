package team5.ad.sa40.stationeryinventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.EmployeeAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;

public class LoginActivity extends Activity implements AdapterView.OnClickListener {

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "11233:hello", "11235:rep", "11236:delegate", "11272:head", "99877:clerk", "98765:supervisor"
    };

    // UI references.
    private AutoCompleteTextView mUseridView;
    private EditText mPasswordView;
    private TextView mStatus;
    private Button forgetPasswordBtn;
    private Setup setup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUseridView = (AutoCompleteTextView) findViewById(R.id.userid);
        mPasswordView = (EditText) findViewById(R.id.password);
        mStatus = (TextView) findViewById(R.id.textViewStatus);
        forgetPasswordBtn = (Button) findViewById(R.id.forgetPassword);

        Button signInButton = (Button) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        forgetPasswordBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.forgetPassword) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("To reset your password, please contact Logic University Stationery Store administrator at (65)6542-2123.")
                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Uri uri = Uri.parse("tel:"+ "65422123");
                            Intent i = new Intent(Intent.ACTION_DIAL, uri);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            builder.show();
        }

        else {
            // Reset errors.
            mUseridView.setError(null);
            mPasswordView.setError(null);
            View focusView = null;

            // Check for a valid password, if the user entered one.
            if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                mPasswordView.setError(getString(R.string.error_field_required));
                Log.e("password field blank:", getString(R.string.error_field_required));
                focusView = mPasswordView;
            }

            // Check for a valid email address.
            if (TextUtils.isEmpty(mUseridView.getText().toString())) {
                mUseridView.setError(getString(R.string.error_field_required));
                focusView = mUseridView;
            }

            if (!TextUtils.isEmpty(mPasswordView.getText().toString()) && !TextUtils.isEmpty(mUseridView.getText().toString())) {
                //hash password:
                MD5 md = new MD5();
                String pwHashed = MD5.getMD5(mPasswordView.getText().toString());
                //String pwHashed = "81dc9bdb52d04dc20036dbd8313ed055";
                Log.i("pwhashed:", pwHashed);


                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("EmpID", mUseridView.getText().toString());
                jsonObject.addProperty("Password", pwHashed);

                //attempt employeeAPI.svc/login API usig retrofit

                //create an adapter for retrofit with base url
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(setup.baseurl).build();

                //creating a service for adapter with our POST class
                EmployeeAPI empAPI = restAdapter.create(EmployeeAPI.class);

                //call for response
                //retrofit will convert gson for JSON-POJO (JSONEmployee)
                empAPI.login(jsonObject, new Callback<JSONEmployee>() {
                    @Override
                    public void success(JSONEmployee employee, Response response) {
                        Log.i("Return :", employee.getEmpID().toString()+" "+ employee.getEmpName().toString());
                        Log.i("User ROle: ", employee.getRoleID().toString());
                        Log.i("Response: ", response.getBody().toString());
                        System.out.println("Response Status "+response.getStatus());
                        Setup.user = employee;
                        Intent i = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error: ", error.toString());
                    }
                });

                //attempt login:
            /*
            try {
                JSONObject user = new JSONObject();
                user.put("EmpID", mUseridView.getText().toString());
                user.put("Password",pwHashed);
                String json = user.toString();
                Toast.makeText(this,json,Toast.LENGTH_SHORT);

                new AsyncTask<String, Void, Employee>() {
                    @Override
                    protected Employee doInBackground(String... params) {
                        return getEmployee(params[0]);
                    }

                    @Override
                    protected void onPostExecute(Employee result) {
                        TextView t = (TextView) findViewById(R.id.textViewStatus);
                        String p = String.format("Employee: %s\n%s\n%s\n%s",
                                result.get("EmpID"), result.get("EmpName"),
                                result.get("RoleID"));
                        t.setText(p);
                        mStatus.setText("Logged in successfully.");
                    }
                }.execute(json);

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("login json error:", e.toString());
                mStatus.setText(getString(R.string.error_connection_failed));
            }

        }
    }

    Employee getEmployee (String data) {
        Employee emp = null;
        try {
            JSONObject e = JSONParser.getJSONFromUrlPOST("http://www.team5.com:8425/employeeapi.svc/login", data);
            emp = new Employee(e.getString("EmpID"), e.getString("EmpName"), e.getString("DeptID"), e.getString("RoleID"), e.getInt("Phone"), e.getString("Email"), e.getString("Password"));
            mStatus.setText(emp.toString());
        } catch (Exception e) {
        }
        return emp;
    }
}*/
                /* by John
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
                    Intent i = new Intent(this, MainActivity.class);
                    String userRole = mPasswordView.getText().toString();
                    Log.i("Extra", userRole);
                    i.putExtra("User", userRole);
                    startActivity(i);
                }
                */
            }
        }
    }
}



