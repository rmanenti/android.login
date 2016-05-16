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
import android.widget.Button;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.LoginActivity;
import com.wizsyst.android.app.login.activity.MainActivity;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.adapter.BeanAnoAdapter;
import com.wizsyst.android.app.login.adapter.BeanFolhaAdapter;
import com.wizsyst.android.app.login.adapter.BeanMesAdapter;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.task.ConsultaContrachequeTask;
import com.wizsyst.android.app.login.utilities.ActivityUtils;
import com.wizsyst.android.app.login.utilities.temporal.DateUtils;
import com.wizsyst.sigem.mobile.sleo.beans.BeanAno;
import com.wizsyst.sigem.mobile.sleo.beans.BeanFolha;
import com.wizsyst.sigem.mobile.sleo.beans.BeanFolhas;
import com.wizsyst.sigem.mobile.sleo.beans.BeanMes;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

import java.util.HashMap;
import java.util.List;

public class ConsultaActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "ConsultaActivity";

    private AlertDialog.Builder builder;

    protected TextView year,
                       month,
                       payroll;

    protected Button buttonQuery;

    private BeanFolhas folhas;

    private List<String> years,
                         months,
                         payrolls;

    private BeanAno selectedYear;
    private BeanMes selectedMonth;
    private BeanFolha selectedPayroll;

    private BeanAnoAdapter   adapterAno;
    private BeanMesAdapter   adapterMonth;
    private BeanFolhaAdapter adapterPayroll;

    AdapterView.OnItemSelectedListener selectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_contracheque_consulta );

        builder = new AlertDialog.Builder( new ContextThemeWrapper( this, R.style.portalAlertDialogCustom ) );

        folhas = ( BeanFolhas ) SessionManager.getInstance().getParameter( "folhas" );

        if ( folhas != null && folhas.getFolhas() != null ) {
            load( R.id.year );
        }

        selectionListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //load();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        year = ( TextView ) findViewById( R.id.year );
        year.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                builder.setTitle( getString( R.string.selectYear ) );
                builder.setAdapter( adapterAno, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select( R.id.year, which );
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
                builder.setAdapter( adapterMonth, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select( R.id.month, which );
                    }
                } );

                builder.show();
            }
        } );

        payroll = ( TextView ) findViewById( R.id.payroll );
        payroll.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                builder.setTitle( getString( R.string.selectPayroll ) );
                builder.setAdapter( adapterPayroll, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        select( R.id.payroll, which );
                    }
                } );

                builder.show();
            }
        } );

        buttonQuery = ( Button ) findViewById( R.id.button_paycheck_query );
        buttonQuery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                query();
            }
        } );
        buttonQuery.setEnabled( false );

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
            ActivityUtils.start( getBaseContext(), activity );
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ) {

            case R.id.button_paycheck_query :

                break;
        }
    }

    private void select( int viewId, int position ) {

        switch ( viewId ) {

            case R.id.year :

                selectedYear = ( BeanAno ) adapterAno.getItem( position );

                load( R.id.month );

                break;

            case R.id.month :

                selectedMonth = ( BeanMes ) adapterMonth.getItem( position );

                load( R.id.payroll );

                break;

            case R.id.payroll :

                selectedPayroll = ( BeanFolha ) adapterPayroll.getItem( position );

                load( R.id.button_paycheck_query );

                break;
        }
    }

    private void load( int viewId ) {

        switch ( viewId ) {

            case R.id.year :

                    if ( folhas == null || folhas.getFolhas() == null ) {

                        buttonQuery.setEnabled( false );

                        return;
                    }

                    adapterAno = new BeanAnoAdapter( this, folhas.getFolhas() );

                break;

            case R.id.month :

                    if ( adapterAno == null || adapterAno.isEmpty() || selectedYear == null ) {

                        buttonQuery.setEnabled( false );

                        return;
                    }

                    year.setText( selectedYear.getAno().toString() );
                    month.setText( getString( R.string.selectMonth ) );

                    selectedMonth = null;

                    adapterMonth = new BeanMesAdapter( this, selectedYear.getMeses() );

                break;

            case R.id.payroll :

                    if ( ( adapterAno == null || adapterAno.isEmpty() || selectedYear == null )
                         ||
                         ( adapterMonth == null || adapterMonth.isEmpty() || selectedMonth == null ) ) {

                        buttonQuery.setEnabled( false );

                        return;
                    }

                    month.setText( DateUtils.getMonthName( selectedMonth.getMes() -1 ) );
                    payroll.setText( getString( R.string.selectPayroll ) );

                    selectedPayroll = null;

                    adapterPayroll = new BeanFolhaAdapter( this, selectedMonth.getFolhas() );

                break;

            case R.id.button_paycheck_query :

                if ( ( adapterAno == null || adapterAno.isEmpty() || selectedYear == null )
                      ||
                      ( adapterMonth == null || adapterMonth.isEmpty() || selectedMonth == null )
                      ||
                      ( adapterPayroll == null || adapterPayroll.isEmpty() || selectedPayroll == null ) ) {

                    buttonQuery.setEnabled( false );
                    return;
                }

                payroll.setText( selectedPayroll.getNomeFolha() );

                buttonQuery.setEnabled( true );
        }
    }

    protected void query() {

        ConsultaContrachequeTask ct = new ConsultaContrachequeTask( this );
        ct.execute( new HashMap<String, Object>() {
            {
                put( "sessao", ((BeanUsuario) SessionManager.getInstance().getParameter( "usuario" )).getSessao() );
                put( "idServ", ((BeanUsuario) SessionManager.getInstance().getParameter( "usuario" )).getIdServ() );
                put( "idComp", selectedPayroll.getIdComp() );
            }
        } );
    }
}
