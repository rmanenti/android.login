package com.wizsyst.android.app.login.utilities.json;

import com.google.gson.Gson;

/**
 * Created by rmanenti on 25/05/2016.
 */
public class Json {

    private static final Gson gson = new Gson();

    public static Object toObject( String data, Class template ) {
        return gson.fromJson( data, template );
    }
}
