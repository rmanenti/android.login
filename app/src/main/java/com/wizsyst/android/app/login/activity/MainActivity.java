package com.wizsyst.android.app.login.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.activity.contracheque.PaycheckQueryActivity;
import com.wizsyst.android.app.login.activity.contracheque.PaycheckQuerySavedActivity;
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
}
