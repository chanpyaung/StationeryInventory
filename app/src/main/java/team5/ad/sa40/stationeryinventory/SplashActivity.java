package team5.ad.sa40.stationeryinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPreferences pref =
                PreferenceManager.getDefaultSharedPreferences
                        (getApplicationContext());
        String username = pref.getString("username", null);
        String password = pref.getString("password", null);

            Thread timerThread = new Thread(){
                public void run(){
                    try{
                        sleep(1200);
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    finally {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            };
            timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
