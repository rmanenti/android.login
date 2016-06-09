package com.wizsyst.android.app.login.activity.contracheque;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.fragment.ListSavedPaychecksFragment;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

public class PaycheckQuerySavedActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "PaycheckQuerySavedActivity";

    private ListSavedPaychecksFragment fragmentList;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_paycheck_query_saved);

        if ( savedInstanceState == null ) {

            Bundle arguments = new Bundle();
            arguments.putSerializable( getString( R.string.user ), (BeanUsuario) SessionManager.getInstance().getParameter( getString( R.string.user ) ));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserFragment uf = new UserFragment();
            uf.setArguments( arguments );

            ft.replace( R.id.fragment_user, uf );
            ft.commit();

            addListFragment();
        }
    }

    @Override
    public void onBackPressed() {

        if ( fragmentList.getCurrentFragmentType() > ListSavedPaychecksFragment.FRAGMENT_lIST_YEARS ) {
            fragmentList.setFragmentType( fragmentList.getCurrentFragmentType() -1 );
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick( View v ) {}

    private void addListFragment() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        fragmentList = new ListSavedPaychecksFragment();

        ft.replace( R.id.fragment_list_saved_paychecks, fragmentList );
        ft.commit();
    }
}
