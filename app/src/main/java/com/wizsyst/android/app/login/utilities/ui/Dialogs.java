package com.wizsyst.android.app.login.utilities.ui;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

/**
 * Created by rmanenti on 25/05/2016.
 */
public class Dialogs {

    public static final class Snack {

        public static Snackbar create( View view, String text, int duration ) {
            return Snackbar.make( view, text, duration );
        }

        public static Snackbar create( View view, String text, int duration, String actionText, View.OnClickListener action ) {

            Snackbar sb = create( view, text, duration );
            sb.setAction( actionText, action );

            return sb;
        }

        public static Snackbar style( Snackbar snackbar, int actionTextColor, int textColor, int backgroundColor ) {

            if ( snackbar != null ) {

                snackbar.setActionTextColor( actionTextColor );

                View view = snackbar.getView();
                view.setBackgroundColor( backgroundColor );

                TextView textView = ( TextView ) view.findViewById( android.support.design.R.id.snackbar_text );
                textView.setTextColor( textColor );
            }

            return snackbar;
        }
    }
}
