package com.wizsyst.android.app.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.model.Usuario;
import com.wizsyst.android.app.login.model.UsuarioPortal;

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
                                            usuario.getIdUsua().toString(),
                                            usuario.getIdServ().toString(),
                                            usuario.getUsuario(),
                                            usuario.getCodMatricula(),
                                            usuario.getDigMatricula(),
                                            usuario.getNome(),
                                            usuario.getSessao() ) );
    }


}
