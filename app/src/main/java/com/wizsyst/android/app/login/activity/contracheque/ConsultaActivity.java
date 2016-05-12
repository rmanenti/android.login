package com.wizsyst.android.app.login.activity.contracheque;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.LoginActivity;
import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.model.Ano;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.ActivityUtils;
import com.wizsyst.android.app.login.utilities.temporal.DateUtils;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

import java.util.List;

public class ConsultaActivity extends BaseActivity {

    public static final String TAG = "ConsultaActivity";

    private AlertDialog.Builder builder;

    protected TextView year,
                       month,
                       payroll;

    String[]     years;
    List<String> months;

    AdapterView.OnItemSelectedListener selectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_contracheque_consulta );

        builder = new AlertDialog.Builder( new ContextThemeWrapper( this, R.style.portalAlertDialogCustom ) );

        years  = ( ( Ano ) SessionManager.getInstance().getParameter( Ano.TAG ) ).getAnos();
        months =  DateUtils.getMonthsAsList();

        selectionListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                load();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        year = ( TextView ) findViewById( R.id.year );
        year.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                builder.setTitle( getString( R.string.selectYear ) );
                builder.setItems( years, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        year.setText( years[ which ] );
                    }
                } );

                builder.show();
            }
        } );


        month = ( TextView ) findViewById( R.id.month );
        month.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                builder.setTitle( getString( R.string.selectMonth ) );
                builder.setItems( months.toArray( new String[] {} ), new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        month.setText( months.get( which ) );
                    }
                } );

                builder.show();
            }
        } );

        payroll = ( TextView ) findViewById( R.id.payroll );

        if ( savedInstanceState == null ) {

            Bundle arguments = new Bundle();
            arguments.putSerializable( "usuario", (BeanUsuario) SessionManager.getInstance().getParameter( "usuario" ));

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserFragment uf = new UserFragment();
            uf.setArguments( arguments );

            ft.replace( R.id.fragment_user, uf );
            ft.commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.main, menu );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Class activity = this.getClass();

        switch ( item.getItemId() ) {

            case R.id.menu_main :

                activity = MainActivity.class;
                break;

            case R.id.menu_paycheck_query :

                activity = ConsultaActivity.class;
                break;

            case R.id.menu_logout :

                activity = LoginActivity.class;
                break;

        }

        if ( !this.getClass().isAssignableFrom( activity ) ) {
            ActivityUtils.start(getBaseContext(), activity );
        }

        return true;
    }

    private void load() {
//        Log.i( TAG, String.format( "Select year is %s and select month is %s ", years[ year.getSelectedItemPosition() ], months.get( month.getSelectedItemPosition() ) ) );
    }
}
