package com.wizsyst.android.app.login.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wizsyst.android.app.login.activity.base.BaseActivity;
import com.wizsyst.android.app.login.task.LoginTask;
import com.wizsyst.android.app.login.R;
import com.wizsyst.android.app.login.model.Usuario;
import com.wizsyst.android.app.login.session.SessionManager;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "Activity.LoginActivity";

    protected EditText inputUsername,
                       inputPassword;

    protected Button buttonLogin;

    protected View messageBox;

    protected SessionManager session;
    protected LoginTask lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login);

        inputUsername = ( EditText ) findViewById( R.id.username );
        inputPassword = ( EditText ) findViewById( R.id.password );
        buttonLogin   = ( Button ) findViewById( R.id.button );
        buttonLogin.setOnClickListener( this );

        messageBox    = findViewById( R.id.mb );

        if ( session != null ) {
            session.destroy();
        }
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

        session = SessionManager.getInstance( getBaseContext() );

        lt = new LoginTask( LoginActivity.this, new Usuario( inputUsername.getText().toString(), inputPassword.getText().toString() ) );
        lt.execute();
    }
}
