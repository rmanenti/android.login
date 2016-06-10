package com.wizsyst.android.app.login.activity.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.LoginActivity;
import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.activity.contracheque.PaycheckQueryActivity;
import com.wizsyst.android.app.login.activity.contracheque.PaycheckQuerySavedActivity;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.ActivityUtils;

/**
 * Created by rmanenti on 10/05/2016.
 */
public class BaseActivity extends AppCompatActivity {

    public static final int DEFAULT_TOOLBAR = R.id.toolbar_default;

    private Toolbar toolbar;

    @Override
    protected void onResume() {

        super.onResume();

        setToolbar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        return openMenuActivity( item.getItemId() );
    }

    public boolean openMenuActivity( int id ) {

        Class activity = this.getClass();

        if ( !SessionManager.getInstance( getBaseContext() ).isLogged() ) {
            SessionManager.getInstance( getBaseContext() ).destroy();
        }
        else {

            switch (id) {

                case R.id.menu_main:

                    activity = MainActivity.class;
                    break;

                case R.id.menu_paycheck_saved:

                    activity = PaycheckQuerySavedActivity.class;
                    break;

                case R.id.menu_paycheck_query:

                    activity = PaycheckQueryActivity.class;
                    break;

                case R.id.menu_logout:

                    SessionManager.getInstance( getBaseContext() ).destroy();
                    return false;

                default:
                    return true;
            }
        }

        if ( !this.getClass().isAssignableFrom( activity ) ) {
            ActivityUtils.start( getBaseContext(), activity );
        }

        return true;
    }

    public void setToolbar() {

        if ( findViewById( DEFAULT_TOOLBAR ) != null ) {

            toolbar =  ( Toolbar ) findViewById( R.id.toolbar_default );
            setSupportActionBar( toolbar );

            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle( getString( R.string.portal ) );
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
