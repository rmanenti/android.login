package com.wizsyst.android.app.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.model.User;

import org.w3c.dom.Document;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import com.wizsyst.android.app.login.utilities.connection.Service;
import com.wizsyst.android.app.login.utilities.connection.http.Http;
import com.wizsyst.android.app.login.utilities.xml.Xml;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class LoginTask extends AsyncTask<String, String, Boolean> {

    private static final String URI                = "http://192.168.0.239:8082/WizRest2/webresources/Login",
                                URI_PARAM_USER     = "usuario",
                                URI_PARAM_PASSWORD = "senha";

    private static Long MAX_TIMEOUT      = 25000L,
                        MAX_READ_TIMEOUT = 25000L;

    private Context context;

    private ProgressDialog progressDialog;

    private User user;

    private Long startTime,
                 endTime;

    public LoginTask( Context context, User user ) {

        this.context = context;
        this.user    = user;
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog = ProgressDialog.show( context, context.getString( R.string.wait ), context.getString( R.string.loggingIn ) );
    }

    @Override
    protected Boolean doInBackground( String... params ) {

        if ( user == null || TextUtils.isEmpty( user.getLogin() ) || TextUtils.isEmpty( user.getPassword() ) ) {
            return false;
        }

        if ( !Service.isNetworkConnectionAvailable( context ) ) {
            return false;
        }

        String uri = new StringBuilder( URI )
                            .append( "?" )
                            .append( URI_PARAM_USER )
                            .append( "=" )
                            .append( user.getLogin() )
                            .append( "&" )
                            .append( URI_PARAM_PASSWORD )
                            .append( "=" )
                            .append( user.getPassword() )
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

            Document doc = Xml.getDomElement(data);

            if ( Xml.hasNode( doc.getDocumentElement(), "erro", true ) ) {
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
            it.putExtra( "username", user.getLogin() );
            context.startActivity( it );

            Toast.makeText( context, "Login bem sucedido.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText( context, context.getString( R.string.loginUnsuccessful ), Toast.LENGTH_SHORT).show();
        }

        progressDialog.dismiss();
    }
}
