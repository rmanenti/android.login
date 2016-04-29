package com.wizsyst.android.app.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.model.Erro;
import com.wizsyst.android.app.login.model.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.wizsyst.android.app.login.model.UsuarioPortal;
import com.wizsyst.android.app.login.utilities.connection.Service;
import com.wizsyst.android.app.login.utilities.connection.http.Http;
import com.wizsyst.android.app.login.utilities.xml.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class LoginTask extends AsyncTask<String, String, Boolean> {

    private static final String URI                = "http://192.168.0.7:8080/WizRest2/webresources/Login",
                                URI_PARAM_USER     = "usuario",
                                URI_PARAM_PASSWORD = "senha";

    private static Long MAX_TIMEOUT      = 25000L,
                        MAX_READ_TIMEOUT = 25000L;

    private Context context;
    private Activity caller;

    private View messageBox;
    private TextView title,
                     message;

    private ProgressDialog progressDialog;

    private Erro erro;

    private UsuarioPortal usuario;

    private Long startTime,
                 endTime;

    public LoginTask( Context context, UsuarioPortal usuario ) {

        this.context = context;
        this.usuario = usuario;

        caller  = ( Activity ) context;
        messageBox = ( View ) caller.findViewById( R.id.mb );
        title      = ( TextView ) caller.findViewById( R.id.mb_message_title );
        message    = ( TextView ) caller.findViewById( R.id.mb_message_text );
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog = ProgressDialog.show( context, context.getString( R.string.wait ), context.getString( R.string.loggingIn ) );
    }

    @Override
    protected Boolean doInBackground( String... params ) {

        if ( usuario == null || TextUtils.isEmpty( usuario.getUsuario() ) || TextUtils.isEmpty( usuario.getSenha() ) ) {
            return false;
        }

        if ( !Service.isNetworkConnectionAvailable( context ) ) {
            return false;
        }

        String uri = new StringBuilder( URI )
                            .append( "?" )
                            .append( URI_PARAM_USER )
                            .append( "=" )
                            .append( usuario.getUsuario() )
                            .append( "&" )
                            .append( URI_PARAM_PASSWORD )
                            .append( "=" )
                            .append( usuario.getSenha() )
                            .toString();

        Log.i( "LOGIN.uri", uri );

        String data = null;

        URL url                = null;
        HttpURLConnection conn = null;

        try {

            url = new URL( uri );
            conn = ( HttpURLConnection ) url.openConnection();
            conn.setReadTimeout( MAX_READ_TIMEOUT.intValue() );
            conn.setConnectTimeout( MAX_TIMEOUT.intValue() );
            conn.setRequestMethod( "GET" );
            conn.setDoInput( true );
            conn.connect();

            publishProgress( context.getString( R.string.connectionEstablished ) );

            int response = conn.getResponseCode();

            publishProgress( context.getString( R.string.retrievingData ) );

            InputStream in = conn.getInputStream();
            data = Http.readString( in );

            if ( TextUtils.isEmpty( data ) ) {
                return false;
            }

            Log.i( "LOGIN.DATA", data );

            Document doc = Xml.getDomElement(data);

            if ( Xml.hasNode( doc.getDocumentElement(), "erro", true ) ) {

                Node erroNode = Xml.getNode( "erro", doc.getDocumentElement() );

                erro = new Erro( Xml.getElementValue( Xml.getNode( "codigo", erroNode ) ),
                                 Xml.getElementValue( Xml.getNode( "descricao", erroNode ) ),
                                 Xml.getElementValue( Xml.getNode( "solucao", erroNode ) ) );

                Log.i( "LOGIN.TASK.ERRO", erro.toString() );

                return false;
            }

            Node usuarioNode = Xml.getNode( "usuario", doc.getDocumentElement() );

            usuario = new UsuarioPortal();
            usuario.setIdUsua( Long.valueOf( Xml.getElementValue( Xml.getNode( "iduser", usuarioNode ) ) ) );
            usuario.setIdServ( Long.valueOf( Xml.getElementValue( Xml.getNode( "idserv", usuarioNode ) ) ) );
            usuario.setUsuario( Xml.getElementValue( Xml.getNode( "usuario", usuarioNode ) ) );
            usuario.setCodMatricula( Xml.getElementValue( Xml.getNode( "codmatricula", usuarioNode ) ) );
            usuario.setDigMatricula( Xml.getElementValue( Xml.getNode( "digmatricula", usuarioNode ) ) );
            usuario.setNome( Xml.getElementValue( Xml.getNode( "nome", usuarioNode ) ) );
            usuario.setSessao( Xml.getElementValue( Xml.getNode( "sessao", usuarioNode ) ) );
        }
        catch( SocketTimeoutException e ) {
            return false;
        }
        catch( IOException ioex ) {
            return false;
        }
        finally {

            if ( conn != null ) {
                conn.disconnect();
            }
        }
        return true;
   }

    @Override
    protected void onProgressUpdate(String... values) {

        super.onProgressUpdate(values);

        progressDialog.setMessage( values[ 0 ] );
    }

    @Override
    protected void onPostExecute(Boolean success) {

        super.onPostExecute(success);

        if ( success ) {

            Intent it = new Intent( context, MainActivity.class );
            it.putExtra( URI_PARAM_USER, usuario );
            context.startActivity( it );

            Toast.makeText( context, context.getString( R.string.loginSuccessful ), Toast.LENGTH_SHORT ).show();
        }
        else {

            messageBox.setVisibility( View.VISIBLE );
            title.setText( erro.getCodigo() );
            message.setText( erro.getDescricao() );
        }

        progressDialog.dismiss();
    }
}
