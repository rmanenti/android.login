package com.wizsyst.android.app.login.utilities.temporal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by rmanenti on 11/05/2016.
 */
public class DateUtils {

    public static List<String> getMonthsAsList() {

        List<String> months = new LinkedList<>();

        DateFormat df = new SimpleDateFormat( "MMMM" );

        Calendar c = Calendar.getInstance();

        for ( int m = 0, s = 11; m <= s; m++ ) {

            c.set( Calendar.MONTH, m );

            String mn = df.format( c.getTime() );

            months.add( mn.substring(0, 1).toUpperCase() + mn.substring( 1 ) );
        }

        return months;
    }
}
