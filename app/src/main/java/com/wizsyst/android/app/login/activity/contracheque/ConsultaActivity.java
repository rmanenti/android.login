package com.wizsyst.android.app.login.activity.contracheque;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.model.Ano;
import com.wizsyst.android.app.login.session.SessionManager;
import com.wizsyst.android.app.login.utilities.temporal.DateUtils;

import java.util.List;

public class ConsultaActivity extends BaseActivity {

    public static final String TAG = "ConsultaActivity";

    Spinner year,
            month;

    String[]     years;
    List<String> months;

    AdapterView.OnItemSelectedListener selectionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_contracheque_consulta );

        years  = ( ( Ano ) SessionManager.getInstance().getParameter( Ano.TAG ) ).getAnos();
        months =  DateUtils.getMonthsAsList();

        ArrayAdapter<String> yearAdapter  =  new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, years );
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>( this, android.R.layout.simple_spinner_dropdown_item, months );

        selectionListener = new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                load();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        };

        year  = ( Spinner ) findViewById( R.id.year );
        year.setAdapter( yearAdapter );
        year.setOnItemSelectedListener( selectionListener );
        year.setPrompt( getString( R.string.selectYear ) );


        month = ( Spinner ) findViewById( R.id.month );
        month.setAdapter( monthAdapter );
        month.setOnItemSelectedListener( selectionListener );
        month.setPrompt( getString( R.string.selectMonth ) );
    }

    private void load() {
        Log.i( TAG, String.format( "Select year is %s and select month is %s ", years[ year.getSelectedItemPosition() ], months.get( month.getSelectedItemPosition() ) ) );
    }
}
