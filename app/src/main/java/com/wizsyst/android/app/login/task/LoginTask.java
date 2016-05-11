package com.wizsyst.android.app.login.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.model.Ano;
import com.wizsyst.android.app.login.model.Erro;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.List;

import com.wizsyst.android.app.login.model.Usuario;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.connection.Service;
import com.wizsyst.android.app.login.utilities.connection.http.Http;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class LoginTask extends AsyncTask<String, String, Boolean> {

    private static final String USER_URI      = "http://192.168.0.7:8080/WizRest2/webresources/Login",
                                YEAR_URI      = "http://192.168.0.7:8080/WizRest2/webresources/ContraCheques/anos",

                                USER_LOGIN    = "usuario",
                                USER_PASSWORD = "senha",

                                YEAR_ID       = "idServ",
                                YEAR_SESSION  = "sessao",

                                SESSION       = "session";

    private static Long MAX_TIMEOUT      = 25000L,
                        MAX_READ_TIMEOUT = 25000L;

    private Context context;
    private Activity caller;

    private View messageBox;
    private TextView title,
                     message;

    private ProgressDialog progressDialog;

    private Usuario usuario;
    private Erro erro;


    private Long startTime,
                 endTime;

    public LoginTask(Context context, Usuario usuario ) {

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

        if ( !Service.isNetworkConnectionAvailable( context ) ) {

            erro = new Erro( "A0001", context.getString( R.string.A0001 ) );
            return false;
        }

        return getUser() && getYears();
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

            SessionManager.getInstance().create( usuario.getSessao(), 30 );

            Intent it = new Intent( context, MainActivity.class );
            context.startActivity( it );
        }
        else {

            SessionManager.getInstance().destroy();

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

    private boolean getUser() {

        if ( usuario == null || TextUtils.isEmpty( usuario.getUsuario() ) || TextUtils.isEmpty( usuario.getSenha() ) ) {

            erro = new Erro( "A0000", context.getString( R.string.A0000 ) );
            return false;
        }

        String uri = new StringBuilder( USER_URI )
                .append( "?" )
                .append( USER_LOGIN )
                .append( "=" )
                .append( usuario.getUsuario() )
                .append( "&" )
                .append( USER_PASSWORD )
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

                    usuario = gs.fromJson(data, Usuario.class);
                    SessionManager.getInstance().addParameter( Usuario.TAG, usuario );
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

    private boolean getYears() {

        if ( usuario == null || usuario.getIdServ() == null ) {

            erro = new Erro( "A0003", context.getString( R.string.A0003 ) );
            return false;
        }

        String uri = new StringBuilder( YEAR_URI )
                .append( "?" )
                .append( YEAR_ID )
                .append( "=" )
                .append( usuario.getIdServ() )
                .append( "&" )
                .append( YEAR_SESSION )
                .append( "=" )
                .append( usuario.getSessao() )
                .toString();

        Log.i( "YEAR.uri", uri );

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

                Log.i( "YEAR.data", data );

                Gson gs = new Gson();

                erro = gs.fromJson(data, Erro.class);

                if ( erro != null && erro.getCodigo() != null ) {
                    return false;
                }
                else {
                    SessionManager.getInstance().addParameter( "anos", gs.fromJson( data, Ano.class ) );
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
}
