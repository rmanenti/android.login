package com.wizsyst.android.app.login.activity.contracheque;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.sigem.mobile.sleo.beans.BeanContraCheque;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

/**
 * Created by rmanenti on 16/05/2016.
 */
public class ContrachequeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contracheque );

        Intent it = getIntent();
        BeanContraCheque contraCheque = ( BeanContraCheque ) it.getSerializableExtra( "contracheque" );

        TextView cc = ( TextView ) findViewById( R.id.cc );
        cc.setText( contraCheque.getCargo() );

        if ( savedInstanceState == null ) {

            Bundle arguments = new Bundle();
            arguments.putSerializable( "usuario", (BeanUsuario) SessionManager.getInstance().getParameter( "usuario" ));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserFragment uf = new UserFragment();
            uf.setArguments( arguments );

            ft.replace( R.id.fragment_user, uf );
            ft.commit();
        }
    }
}
