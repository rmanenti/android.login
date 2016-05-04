package com.wizsyst.android.app.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.model.Usuario;
import com.wizsyst.android.app.login.model.UsuarioPortal;
import com.wizsyst.android.app.login.utilities.ActivityUtils;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class MainActivity extends AppCompatActivity {

    private TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        title = ( TextView ) findViewById( R.id.title );

        Intent it = getIntent();
        UsuarioPortal usuario = ( UsuarioPortal ) it.getParcelableExtra( "usuario" );
        title.setText( String.format( "IdUsua = %s, IdServ = %s, Usuario = %s, CodMatricula = %s, DigMatricula = %s, Nome = %s, Sessao = %s ",
                                            usuario.getIdUser().toString(),
                                            usuario.getIdServ().toString(),
                                            usuario.getUsuario(),
                                            usuario.getCodMatricula(),
                                            usuario.getDigMatricula(),
                                            usuario.getNome(),
                                            usuario.getSessao() ) );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ) {

            case R.id.menu_logout :
                ActivityUtils.start( getBaseContext(), LoginActivity.class );
        }

        return true;
    }
}
