package com.wizsyst.android.app.login.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

/**
 * Created by rmanenti on 04/05/2016.
 */
public class ActivityUtils {

    public static void start( Context context, Class<? extends Activity> activity ) {

        // After logout redirect user to Loing Activity
        Intent i = new Intent( context, activity );
        // Closing all the Activities
        i.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        // Add new Flag to start new Activity
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );

        // Staring Login Activity
        context.startActivity( i );
    }
}
