package com.wizsyst.android.app.login.task;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.LoginActivity;
import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.model.Erro;
import com.wizsyst.android.app.login.model.Usuario;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.wizsyst.android.app.login.model.UsuarioPortal;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.connection.Service;
import com.wizsyst.android.app.login.utilities.connection.http.Http;
import com.wizsyst.android.app.login.utilities.xml.Xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class LoginTask extends AsyncTask<String, String, Boolean> {

    private static final String URI      = "http://192.168.0.7:8080/WizRest2/webresources/Login",
                                USER     = "usuario",
                                PASSWORD = "senha",
                                SESSION  = "session";

    private static Long MAX_TIMEOUT      = 25000L,
                        MAX_READ_TIMEOUT = 25000L;

    private Context context;
    private Activity caller;

    private View messageBox;
    private TextView title,
                     message;

    private ProgressDialog progressDialog;

    private SessionManager session;
    private UsuarioPortal usuario;
    private Erro erro;


    private Long startTime,
                 endTime;

    public LoginTask(Context context, SessionManager session, UsuarioPortal usuario ) {

        this.context = context;
        this.session = session;
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

            erro = new Erro( "A0000", context.getString( R.string.A0000 ) );
            return false;
        }

        if ( !Service.isNetworkConnectionAvailable( context ) ) {

            erro = new Erro( "A0001", context.getString( R.string.A0001 ) );
            return false;
        }

        String uri = new StringBuilder( URI )
                            .append( "?" )
                            .append( USER )
                            .append( "=" )
                            .append( usuario.getUsuario() )
                            .append( "&" )
                            .append( PASSWORD )
                            .append( "=" )
                            .append( usuario.getSenha() )
                            .toString();

        Log.i( "LOGIN.uri", uri );

        String data = null;

        URL url                = null;
        HttpURLConnection conn = null;

        InputStream in         = null;

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

            if ( response == 200 ) {

                in = conn.getInputStream();

                data = Http.readString( in );

                Gson gs = new Gson();

                erro = gs.fromJson(data, Erro.class);

                if ( erro != null && erro.getCodigo() != null ) {
                    return false;
                }
                else {
                    usuario = gs.fromJson(data, UsuarioPortal.class);
                }
            }
            else {

                erro = new Erro( "A0002", context.getString( R.string.A0002 ) );
                return false;
            }
        }
        catch( SocketTimeoutException e ) {
            return false;
        }
        catch( IOException ioex ) {
            return false;
        }
        finally {

            try {

                if (in != null) {
                    in.close();
                }
            }
            catch( IOException ioex ) {
                return false;
            }

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

            session.create( usuario.getSessao(), 30 );

            Intent it = new Intent( context, MainActivity.class );
            it.putExtra( USER, usuario );
            context.startActivity( it );
        }
        else {

            session.destroy();

            Handler messageBoxHandler = new Handler();
            Runnable messageBoxRunnable = new Runnable() {
                @Override
                public void run() {
                    messageBox.setVisibility( View.GONE );
                }
            };

            messageBox.setVisibility( View.VISIBLE );
            title.setText( erro.getCodigo() );
            message.setText( erro.getMensagem() );

            messageBoxHandler.removeCallbacks( messageBoxRunnable );
            messageBoxHandler.postDelayed( messageBoxRunnable, 5000 );
        }

        progressDialog.dismiss();
    }
}
