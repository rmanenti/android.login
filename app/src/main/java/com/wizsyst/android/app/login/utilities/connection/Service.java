package com.wizsyst.android.app.login.utilities.connection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rmanenti on 25/04/2016.
 */
public class Service {

    /**
     * Check whether a network connection service is available to use.
     *
     * @param context
     * @see http://developer.android.com/intl/pt-br/training/basics/network-ops/connecting.html
     * @return
     */
    public static boolean isNetworkConnectionAvailable( Context context ) {

        ConnectivityManager connMgr = ( ConnectivityManager ) context.getSystemService( Context.CONNECTIVITY_SERVICE );
        NetworkInfo networkInfo     = connMgr.getActiveNetworkInfo();

        return ( networkInfo != null && networkInfo.isConnected() );
    }
}
