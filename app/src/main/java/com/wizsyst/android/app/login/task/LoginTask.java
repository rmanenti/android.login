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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.connection.Service;
import com.wizsyst.android.app.login.utilities.connection.http.Http;
import com.wizsyst.sigem.mobile.sleo.beans.BeanErro;
import com.wizsyst.sigem.mobile.sleo.beans.BeanFolhas;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class LoginTask extends AsyncTask<String, String, Boolean> {

    private static final String URI_USER      = "http://192.168.0.7:8080/WizSigemMobile/WS/SLeo/Login",
                                URI_PAYROLLS  = "http://192.168.0.7:8080/WizSigemMobile/WS/SLeo/ContraCheques/folhas",

                                USER_LOGIN    = "usuario",
                                USER_PASSWORD = "senha",

                                PAYROLL_ID_SERV = "idServ",
                                PAYROLL_ID_FILL = "idFill",
                                PAYROLL_SESSION = "sessao";

    private static Long MAX_TIMEOUT      = 25000L,
                        MAX_READ_TIMEOUT = 25000L;

    private Context context;
    private Activity caller;

    private View messageBox;
    private TextView title,
                     message;

    private ProgressDialog progressDialog;

    private BeanUsuario usuario;
    private BeanErro erro;

    String user,
           password;

    private Long startTime,
                 endTime;

    public LoginTask(Context context, String u, String p ) {

        this.context = context;

        user     = u;
        password = p;

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

            erro = new BeanErro();
            erro.setCodigo( "A0001" );
            erro.setMensagem( context.getString( R.string.A0001 ) );

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

            if ( erro != null ) {

                messageBox.setVisibility(View.VISIBLE);
                title.setText(erro.getCodigo());
                message.setText(erro.getMensagem());

                messageBoxHandler.removeCallbacks(messageBoxRunnable);
                messageBoxHandler.postDelayed(messageBoxRunnable, 5000);
            }
        }

        progressDialog.dismiss();
    }

    private boolean getUser() {

        if ( TextUtils.isEmpty( user ) || TextUtils.isEmpty( password ) ) {

            erro = new BeanErro();
            erro.setCodigo( "A0000" );
            erro.setMensagem( context.getString( R.string.A0000 ) );

            return false;
        }

        String uri = new StringBuilder( URI_USER )
                .append( "?" )
                .append( USER_LOGIN )
                .append( "=" )
                .append( user )
                .append( "&" )
                .append( USER_PASSWORD )
                .append( "=" )
                .append( password )
                .toString();

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

                erro = gs.fromJson( data, BeanErro.class );

                if ( erro != null && erro.getCodigo() != null ) {
                    return false;
                }
                else {

                    usuario = gs.fromJson(data, BeanUsuario.class);
                    SessionManager.getInstance().addParameter( context.getString( R.string.user ), usuario );
                }
            }
            else {

                erro = new BeanErro();
                erro.setCodigo( "A0002" );
                erro.setMensagem( context.getString( R.string.A0002 ) );

                return false;
            }
        }
        catch( SocketTimeoutException e ) {

            erro = new BeanErro();
            erro.setCodigo( "A0002" );
            erro.setMensagem( context.getString( R.string.A0002 ) + "\n" + e.getLocalizedMessage() );

            return false;
        }
        catch( IOException ioex ) {

            erro = new BeanErro();
            erro.setCodigo( "A0002" );
            erro.setMensagem( context.getString( R.string.A0002 ) + "\n" + ioex.getLocalizedMessage() );

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

        if ( usuario == null || usuario.getIdServ() < 0 ) {

            erro = new BeanErro();
            erro.setCodigo( "A0003" );
            erro.setMensagem( context.getString( R.string.A0003 ) );

            return false;
        }

        String uri = new StringBuilder( URI_PAYROLLS )
                .append( "?" )
                .append( PAYROLL_ID_SERV )
                .append( "=" )
                .append( usuario.getIdServ() )
                .append( "&" )
                .append( PAYROLL_ID_FILL )
                .append( "=" )
                .append( usuario.getIdFill() )
                .append( "&" )
                .append( PAYROLL_SESSION )
                .append( "=" )
                .append( usuario.getSessao() )
                .toString();

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

                Log.i( "PAYROLL.data", data );

                Gson gs = new Gson();

                erro = gs.fromJson( data, BeanErro.class );

                if ( erro != null && erro.getCodigo() != null ) {
                    return false;
                }
                else {
                    SessionManager.getInstance().addParameter( context.getString( R.string.payrolls ), gs.fromJson( data, BeanFolhas.class ) );
                }
            }
            else {

                erro = new BeanErro();
                erro.setCodigo( "A0002" );
                erro.setMensagem( context.getString( R.string.A0002 ) );

                return false;
            }
        }
        catch( SocketTimeoutException e ) {

            erro = new BeanErro();
            erro.setCodigo( "A0002" );
            erro.setMensagem( context.getString( R.string.A0002 ) + "\n" + e.getLocalizedMessage() );

            return false;
        }
        catch( IOException ioex ) {

            erro = new BeanErro();
            erro.setCodigo( "A0002" );
            erro.setMensagem( context.getString( R.string.A0002 ) + "\n" + ioex.getLocalizedMessage() );

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
