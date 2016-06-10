package com.wizsyst.android.app.login.activity.contracheque;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.fragment.UserFragment;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.json.Json;
import com.wizsyst.android.app.login.utilities.ui.Dialogs;
import com.wizsyst.sigem.mobile.sleo.beans.BeanContraCheque;
import com.wizsyst.sigem.mobile.sleo.beans.BeanEventoContraCheque;
import com.wizsyst.sigem.mobile.sleo.beans.BeanUsuario;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by rmanenti on 16/05/2016.
 */
public class PaycheckActivity extends BaseActivity implements View.OnClickListener {

    private String payroll,
                   paycheck;

    private Integer idComp,
                    month,
                    year;

    private boolean saved = false;

    private Button buttonSave;

    private BeanContraCheque contraCheque;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_paycheck);

        Intent it = getIntent();

        idComp   = ( Integer ) it.getIntExtra( getString( R.string.id_idComp ), 0 );
        month    = ( Integer ) it.getIntExtra( getString( R.string.month ), 0 );
        year     = ( Integer ) it.getIntExtra( getString( R.string.year ), 0 );
        payroll  = ( String ) it.getStringExtra( getString( R.string.payroll ) );
        paycheck = ( String ) it.getStringExtra( getString( R.string.paycheck ) );
        saved    = ( Boolean ) it.getBooleanExtra( getString( R.string.saved ), false );

        contraCheque = ( BeanContraCheque ) Json.toObject( paycheck, BeanContraCheque.class );

        buttonSave = ( Button ) findViewById( R.id.save );
        buttonSave.setOnClickListener( this );

        if ( saved ) {
            buttonSave.setText( getString( R.string.discard ) );
        }

        TextView textPayroll = ( TextView ) findViewById( R.id.payroll ),
                 textPeriod  = ( TextView ) findViewById( R.id.period ),

                 textTotalEarnings   = ( TextView ) findViewById( R.id.total_earnings ),
                 textTotalDeductions = ( TextView ) findViewById( R.id.total_decutions ),
                 textTotalLiquid     = ( TextView ) findViewById( R.id.total_liquid );

        textPayroll.setText( payroll );
        textPeriod.setText( month + "/" + year );

        TableLayout tableEarnings    = ( TableLayout ) findViewById( R.id.earnings ),
                    tableDeductions  = ( TableLayout ) findViewById( R.id.deductions ),
                    tableInformation = ( TableLayout ) findViewById( R.id.aditional_info );

        if ( contraCheque != null ) {

            if (contraCheque.getEventos() != null) {

                for ( BeanEventoContraCheque evento : contraCheque.getEventos() ) {

                    TableRow row = createValueRow( evento.getDesEvento(), evento.getValor() );

                    if ( "C".equals( evento.getDebitoCredito() ) ) {
                        tableEarnings.addView( row );
                    }
                    else {
                        tableDeductions.addView( row );
                    }
                }
            }

            textTotalEarnings.setText( contraCheque.getProventos() );
            textTotalDeductions.setText( contraCheque.getDescontos() );
            textTotalLiquid.setText( contraCheque.getLiquido() );

            tableInformation.addView( createValueRow( contraCheque.getNivelSalarial(), contraCheque.getSalario() ) );
            tableInformation.addView( createValueRow( "Sal. Base IAPS/INSS", contraCheque.getBasePrevidencia() ) );
            tableInformation.addView( createValueRow( "Base Calc. FGTS", contraCheque.getBaseFGTS() ) );
            tableInformation.addView( createValueRow( "F.G.T.S do Mês", contraCheque.getFGTS() ) );
            tableInformation.addView( createValueRow( "Base Calc. IRRF", contraCheque.getBaseIRRF() ) );
            tableInformation.addView( createValueRow( "Contrapartida Prefeitura - IPE", contraCheque.getIndiceSalarioFamilia() ) );
        }

        if ( savedInstanceState == null ) {

            Bundle arguments = new Bundle();
            arguments.putSerializable( getString( R.string.user ), (BeanUsuario) SessionManager.getInstance( getBaseContext() ).getParameter( getString( R.string.user ) ) );

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            UserFragment uf = new UserFragment();
            uf.setArguments( arguments );

            ft.replace( R.id.fragment_user, uf );
            ft.commit();
        }
    }

    private TableRow createValueRow( String text, String value ) {

        TableRow row = new TableRow( this );
        TableRow.LayoutParams lp = new TableRow.LayoutParams( TableRow.LayoutParams.MATCH_PARENT );

        TextView t = ( TextView ) getLayoutInflater().inflate( R.layout.widget_textview_paycheck_text, null ),
                a = ( TextView ) getLayoutInflater().inflate( R.layout.widget_textview_paycheck_text_amount, null );

        t.setText( text );
        a.setText( value );

        row.addView( t );
        row.addView( a );

        return row;
    }

    @Override
    public void onClick(View v) {

        switch ( v.getId() ) {

            case R.id.save :

                save();
                break;
        }
    }

    private void save() {

        if ( !TextUtils.isEmpty( paycheck ) ) {

            File folderPaychecks = new File( getFilesDir() + File.separator + getString( R.string.paycheck ).toLowerCase() ),
                 yearFolder,
                 monthFolder;

            if ( saved ) {

                final File file = new File(
                        new File(
                                new StringBuilder( folderPaychecks.getPath() )
                                          .append( File.separator )
                                          .append( String.valueOf( year ) )
                                          .append( File.separator )
                                          .append( String.valueOf( month ) )
                                          .toString()
                        ), idComp + ".json" );

                if ( file.exists() && file.delete() ) {

                    Dialogs.Snack.style(
                            Dialogs.Snack.create(findViewById(R.id.paycheck), getString(R.string.paycheckDiscarded), Snackbar.LENGTH_LONG, getString(R.string.undo).toUpperCase(), new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {

                                    saved = false;
                                    save();
                                }
                            }),
                            ContextCompat.getColor(this, R.color.colorAccentDark),
                            ContextCompat.getColor(this, R.color.colorInputText),
                            ContextCompat.getColor(this, R.color.colorPrimary)
                    )
                    .show();
                }

                return;
            }

            boolean created = true;

            if ( !folderPaychecks.exists() ) {
                created = folderPaychecks.mkdir();
            }

            if ( created ) {

                yearFolder = new File( folderPaychecks, String.valueOf( year ) );

                if ( !yearFolder.exists() ) {
                    created = yearFolder.mkdir();
                }

                if ( created ) {

                    monthFolder = new File( yearFolder, String.valueOf( month ) );

                    if ( !monthFolder.exists() ) {
                        created = monthFolder.mkdir();
                    }

                    if ( created ) {

                        final File file = new File( monthFolder, idComp + ".json" );

                        FileOutputStream stream = null;

                        try {

                            stream = new FileOutputStream( file );
                            stream.write( paycheck.getBytes() );

                            Dialogs.Snack.style(
                                    Dialogs.Snack.create( findViewById( R.id.paycheck ), getString( R.string.paycheckSaved ), Snackbar.LENGTH_LONG, getString( R.string.undo ).toUpperCase(), new View.OnClickListener() {

                                        @Override
                                        public void onClick( View v ) {
                                        file.delete();
                                        }
                                    } ),
                                    ContextCompat.getColor( this, R.color.colorAccentDark ),
                                    ContextCompat.getColor( this, R.color.colorInputText ),
                                    ContextCompat.getColor( this, R.color.colorPrimary )
                            )
                            .show();
                        }
                        catch ( Exception e ) {
                            e.printStackTrace();
                        }
                        finally {

                            if ( stream != null ) {

                                try {
                                    stream.close();
                                }
                                catch ( Exception e ) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}
