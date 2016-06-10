package com.wizsyst.android.app.login.session;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.wizsyst.android.app.login.activity.LoginActivity;
import com.wizsyst.android.app.login.utilities.ActivityUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rmanenti on 04/05/2016.
 */
public class SessionManager {

    private static SessionManager sessionManager;

    // Shared Preferences
    private SharedPreferences sharedPreferences;

    // Editor for Shared preferences
    private SharedPreferences.Editor editor;

    // Context
    private Context context;

    // Session parameters
    private Map<String, Object> parameters;

    // Mode
    private Integer PRIVATE_MODE = 0;

    private static final String NAME   = "App.Login.Preferences",
                                ID     = "id",
                                LOGGED = "logged",
                                START  = "start",
                                PERIOD = "period";

    public static final SessionManager getInstance( Context context ) {

        if ( sessionManager == null ) {
            sessionManager = new SessionManager( context );
        }

        return sessionManager;
    }

    private SessionManager( Context context ) {

        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
    }

    public static final boolean isSessionCreated() {
        return ( sessionManager != null );
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

        editor = sharedPreferences.edit();
        editor.putBoolean( LOGGED, true );
        editor.putString( ID, id );
        editor.putLong( START, c.getTimeInMillis() );

        c.add( Calendar.MINUTE, timeout );
        editor.putLong( PERIOD, c.getTimeInMillis() );

        // commit changes
        return editor.commit();
    }

    public void addParameter( String name, Object value ) {

        if ( parameters == null ) {
            parameters = new HashMap<>();
        }

        parameters.put( name, value );
    }

    public Object getParameter( String name ) {

        if ( parameters != null ) {
            return parameters.get( name );
        }

        return null;
    }

    /**
     * Destroys the session previously created.
     */
    public void destroy() {

        if ( editor != null ) {

            editor.clear();
            editor.commit();
        }

        if ( parameters != null ) {
            parameters.clear();
        }

        sessionManager = null;

        Intent i = new Intent( context, LoginActivity.class );
        i.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        context.startActivity( i );
    }

    public boolean isLogged() {
        return sharedPreferences.getBoolean( LOGGED, false );
    }
}
