/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package team5.ad.sa40.stationeryinventory;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class StarterApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    // Enable Local Datastore.
    Parse.enableLocalDatastore(this);

    //initialise
    Parse.initialize(this, "EQnJ9kuR5DKhU0UfVPr6VwN0QA0wVys00jnrv4KU", "7Zl9KD105R31YzV7h3dUskdjk3LJwv8FsdHLMOBh");
    ParseInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
      @Override
      public void done(ParseException e) {
        String deviceToken = (String) ParseInstallation.getCurrentInstallation().get("deviceToken");
        Log.i("deviceToken", deviceToken);
        Setup.deviceToken = deviceToken;
      }
    });

    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    // Optionally enable public read access.
    // defaultACL.setPublicReadAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
