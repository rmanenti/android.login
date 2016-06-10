package com.wizsyst.android.app.login.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wefika.flowlayout.FlowLayout;
import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class MainActivity extends BaseActivity {

    public static final String TAG = "MainActivity";

    private Menu menu = null;
    private FlowLayout layoutMenu;

    private TextView title;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        title = ( TextView ) findViewById( R.id.title );
        layoutMenu = ( FlowLayout ) findViewById( R.id.menu );

        if ( savedInstanceState == null ) {

            Bundle arguments = new Bundle();
            arguments.putSerializable( getString( R.string.user ), ( BeanUsuario ) SessionManager.getInstance( getBaseContext() ).getParameter( getString( R.string.user ) ) );

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserFragment uf = new UserFragment();
            uf.setArguments( arguments );

            ft.replace( R.id.fragment_user, uf );
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m) {

        super.onCreateOptionsMenu( m );

        createDashboard( m );

        return true;
    }

    private void createDashboard( Menu m ) {

        if ( menu != null ) {
            return;
        }

        menu = m;

        LayoutInflater inflater = getLayoutInflater();

        for ( int i = 0, s = menu.size(); i < s; i++ ) {

            MenuItem mi = menu.getItem( i );

            layoutMenu.addView(
                    createDashboardItem( inflater, mi.getIcon(),mi.getTitle().toString(), mi.getItemId() ) );
        }
    }

    private View createDashboardItem( LayoutInflater inflater, Drawable icon, String text, int id ) {

        /*
        * Ao inflar o layout, caso não seja configurado o parent (segundo argumento) as propriedades para calcular sua posição e tamanho com relação àquele são sobrescritas, ou zeradas.
        * Dessa forma, é necessário passá-lo para evitar erros. O terceiro argumento indica se o próprio parent deve ou não adicioná-lo por conta prória.
        *
        * Link: http://blog.caelum.com.br/conhecendo-melhor-o-metodo-inflate/
        */
        LinearLayoutCompat layout = ( LinearLayoutCompat ) inflater.inflate( R.layout.widget_button_text, layoutMenu, false );

        ImageButton ib;
        TextView t;

        for ( int i = 0; i < layout.getChildCount(); i++ ) {

            View v = layout.getChildAt( i );

            if ( v instanceof TextView ) {

                t = ( TextView ) v;
                t.setText( text );
            }
            else if ( v instanceof ImageButton ) {

                final int _id = id;

                ib = ( ImageButton ) v;
                ib.setImageDrawable( icon );
                ib.setOnClickListener( new View.OnClickListener() {

                    @Override
                    public void onClick( View v ) {
                        openMenuActivity( _id );
                    }
                } );
            }
        }

        return layout;
    }

}
