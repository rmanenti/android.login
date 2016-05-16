package com.wizsyst.android.app.login.task;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.activity.contracheque.ContrachequeActivity;
import com.wizsyst.android.app.login.utilities.connection.Service;
import com.wizsyst.android.app.login.utilities.connection.http.Http;
import com.wizsyst.sigem.mobile.sleo.beans.BeanContraCheque;
import com.wizsyst.sigem.mobile.sleo.beans.BeanErro;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

/**
 * Created by rmanenti on 16/05/2016.
 */
public class ConsultaContrachequeTask extends AsyncTask<Map<String, Object>, String, Boolean> {

    private static final String PAYCHECK_URI      = "http://192.168.0.7:8080/WizSigemMobile/WS/SLeo/ContraCheques/cc",

                                PAYCHECK_ID_SERV = "idServ",
                                PAYCHECK_ID_COMP = "idComp",
                                PAYCHECK_SESSION = "sessao",

                                SESSION       = "session";

    private static Long MAX_TIMEOUT      = 25000L,
                        MAX_READ_TIMEOUT = 25000L;

    private Context context;
    private Activity caller;

    private View messageBox;
    private TextView title,
                     message;

    private ProgressDialog progressDialog;

    private BeanContraCheque contracheque;
    private BeanErro erro;

    private Long startTime,
                 endTime;

    public ConsultaContrachequeTask(Context context ) {

        this.context = context;

        caller  = ( Activity ) context;
        messageBox = ( View ) caller.findViewById( R.id.mb );
        title      = ( TextView ) caller.findViewById( R.id.mb_message_title );
        message    = ( TextView ) caller.findViewById( R.id.mb_message_text );
    }

    @Override
    protected void onPreExecute() {

        super.onPreExecute();

        progressDialog = ProgressDialog.show( context, context.getString( R.string.wait ), context.getString( R.string.queryingPaycheck ) );
    }


    @Override
    protected Boolean doInBackground(Map<String, Object>... params) {

        if ( !Service.isNetworkConnectionAvailable( context ) ) {

            erro = new BeanErro();
            erro.setCodigo( "A0001" );
            erro.setMensagem( context.getString( R.string.A0001 ) );

            return false;
        }

        return getContracheque( params );
    }

    @Override
    protected void onProgressUpdate(String... values) {

        super.onProgressUpdate(values);

        progressDialog.setMessage( values[ 0 ] );
    }

    @Override
    protected void onPostExecute( Boolean success ) {

        super.onPostExecute( success );

        if ( success ) {

            Intent it = new Intent( context, ContrachequeActivity.class );
            it.putExtra( "contracheque", contracheque );
            context.startActivity( it );
        }
        else {

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

    protected boolean getContracheque( Map<String, Object>... params ) {

        if ( params == null || params.length == 0 ) {

            erro = new BeanErro();
            erro.setCodigo( "A0004" );
            erro.setMensagem( context.getString( R.string.A0004 ) );

            return false;
        }

        String uri = new StringBuilder( PAYCHECK_URI )
                .append( "?" )
                .append( PAYCHECK_ID_SERV )
                .append( "=" )
                .append( params[ 0 ].get( PAYCHECK_ID_SERV ) )
                .append( "&" )
                .append( PAYCHECK_ID_COMP )
                .append( "=" )
                .append( params[ 0 ].get( PAYCHECK_ID_COMP ) )
                .append( "&" )
                .append( PAYCHECK_SESSION )
                .append( "=" )
                .append( params[ 0 ].get( PAYCHECK_SESSION ) )
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
                    contracheque = gs.fromJson( data, BeanContraCheque.class );
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
