package com.wizsyst.android.app.login.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.model.UsuarioPortal;

/**
 * Created by rmanenti on 05/05/2016.
 */
public class UserFragment extends Fragment {

    private UsuarioPortal user;

    private TextView code,
                     name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle ba = getArguments();

        code = ( TextView ) getActivity().findViewById( R.id.uh_code );
        name = ( TextView ) getActivity().findViewById( R.id.uh_name );

        if ( ba != null && ba.containsKey( "usuario" ) ) {
            setUser( ( UsuarioPortal ) getArguments().getParcelable( "usuario" ) );
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    public void setUser( UsuarioPortal user ) {

        this.user = user;

        code.setText( user.getCodMatricula() );
        name.setText( user.getNome() );

    }
}
