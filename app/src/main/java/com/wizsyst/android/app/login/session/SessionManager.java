package com.wizsyst.android.app.login.session;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wizsyst.android.app.login.utilities.ActivityUtils;

import java.util.Calendar;

/**
 * Created by rmanenti on 04/05/2016.
 */
public class SessionManager {

    // Shared Preferences
    private SharedPreferences sharedPreferences;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context context;

    // Mode
    private Integer PRIVATE_MODE = 0;

    private static final String NAME   = "App.Login.Preferences",
                                ID     = "id",
                                LOGGED = "logged",
                                START  = "start",
                                PERIOD = "period";

    public SessionManager( Context context ) {

        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        editor            = sharedPreferences.edit();
    }

    /**
     * Attempts to create a session for logging the user in the application.
     *
     * @param id      Session identification;
     * @param timeout Minutes until session is destroyed.
     * @return
     */
    public boolean create( String id, Integer timeout ) {

        Calendar c = Calendar.getInstance();

        editor.putBoolean( LOGGED, true );
        editor.putString( ID, id );
        editor.putLong( START, c.getTimeInMillis() );

        c.add( Calendar.MINUTE, timeout );
        editor.putLong( PERIOD, c.getTimeInMillis() );

        // commit changes
        return editor.commit();
    }

    /**
     * Destroys the session previously created.
     */
    public void destroy() {

        editor.clear();
        editor.commit();
    }

    public void destroy( Class<? extends Activity> startActivity ) {

        destroy();
        ActivityUtils.start( context, startActivity );
    }


    public boolean isLogged(){
        return sharedPreferences.getBoolean( LOGGED, false );
    }
}
