package com.wizsyst.android.app.login.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class Usuario implements Parcelable {

    private Long id;

    private String login,
                   password,
                   nome;

    private boolean deletado,
                    bloqueado;

    public Usuario() {}

    public Usuario(String login, String password ) {

        this.login    = login;
        this.password = password;
    }

    public Usuario(String login, String password, String nome ) {

        this( login, password );
        this.nome = nome;
    }

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private Usuario( Parcel in ) {

        id       = in.readLong();
        login    = in.readString();
        password = in.readString();
        nome     = in.readString();

        boolean[] b = new boolean[ 2 ];
        in.readBooleanArray( b );

        deletado  = b[ 0 ];
        bloqueado = b[ 1 ];
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isDeletado() {
        return deletado;
    }

    public void setDeletado(boolean deletado) {
        this.deletado = deletado;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong( id );
        out.writeString( login );
        out.writeString( password );
        out.writeString( nome );
        out.writeBooleanArray( new boolean[] { deletado, bloqueado } );
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {

        public Usuario createFromParcel(Parcel in) {
            return new Usuario( in );
        }

        public Usuario[] newArray( int size ) {
            return new Usuario[ size ];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario user = (Usuario) o;

        if (!id.equals(user.id)) return false;
        return login.equals(user.login);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + login.hashCode();
        return result;
    }
}
