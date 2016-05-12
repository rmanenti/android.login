package com.wizsyst.android.app.login.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

/**
 * Created by rmanenti on 05/05/2016.
 */
public class UserFragment extends Fragment {

    private static final String TAG = "Fragment.UserFragment";

    private BeanUsuario user;

    private TextView code,
                     name;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i( TAG, "onCreateView" );

        return inflater.inflate(R.layout.fragment_user, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Log.i( TAG, "onViewCreated" );

        code = ( TextView ) getActivity().findViewById( R.id.uh_code );
        name = ( TextView ) getActivity().findViewById( R.id.uh_name );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        Log.i( TAG, "onActivityCreated" );

        Bundle ba = getArguments();

        if ( user == null && ba != null && ba.containsKey( "usuario" ) ) {
            setUser( ( BeanUsuario ) getArguments().getParcelable( "usuario" ) );
        }
    }

    /**
     * Sets the user object related to this fragment.
     * @param user
     */
    public void setUser( BeanUsuario user ) {

        this.user = user;
        display();
    }

    private void display() {

        if ( user != null ) {

            code.setText( user.getCodMatricula() );
            name.setText( user.getNome() );
        }
    }
}
