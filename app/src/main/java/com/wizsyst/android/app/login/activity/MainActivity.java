package com.wizsyst.android.app.login.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.wizsyst.android.app.login.R;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class MainActivity extends AppCompatActivity {

    TextView title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate( savedInstanceState );

        setContentView( R.layout.activity_main );

        title = ( TextView ) findViewById( R.id.title );

        Intent it = getIntent();
        title.setText( it.getStringExtra( "username" ) );
    }


}
