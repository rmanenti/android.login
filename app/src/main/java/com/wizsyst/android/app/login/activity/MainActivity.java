package com.wizsyst.android.app.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.activity.contracheque.ConsultaActivity;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.ActivityUtils;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private TextView title;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        title = ( TextView ) findViewById( R.id.title );

        if ( savedInstanceState == null ) {

            Bundle arguments = new Bundle();
            arguments.putSerializable( getString( R.string.user ), ( BeanUsuario ) SessionManager.getInstance().getParameter( getString( R.string.user ) ) );

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserFragment uf = new UserFragment();
            uf.setArguments( arguments );

            ft.replace( R.id.fragment_user, uf );
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Class activity = this.getClass();

        switch ( item.getItemId() ) {

            case R.id.menu_main :

                activity = MainActivity.class;
                break;

            case R.id.menu_paycheck_query :

                activity = ConsultaActivity.class;
                break;

            case R.id.menu_logout :

                activity = LoginActivity.class;
                break;

        }

        if ( !this.getClass().isAssignableFrom( activity ) ) {
            ActivityUtils.start(getBaseContext(), activity );
        }

        return true;
    }
}
