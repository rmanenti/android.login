package com.wizsyst.android.app.login.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.wizsyst.android.app.login.LoginTask;
import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.model.User;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    protected EditText inputUsername,
                       inputPassword;

    protected Button buttonLogin;

    protected LoginTask lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        inputUsername = (EditText) findViewById( R.id.username );
        inputPassword = ( EditText ) findViewById( R.id.password );
        buttonLogin   = ( Button ) findViewById( R.id.button );
        buttonLogin.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {

        switch( v.getId() ) {

            case R.id.button :

                login();

            default :
                return;
        }
    }

    private void login() {

        lt = new LoginTask( LoginActivity.this, new User( inputUsername.getText().toString(), inputPassword.getText().toString() ) );
        lt.execute();
    }
}
