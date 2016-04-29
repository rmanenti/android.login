package com.wizsyst.android.app.login.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rmanenti on 29/04/2016.
 */
public class UsuarioPortal implements Parcelable {

    private Long idUsua,
                 idServ;

    private String usuario,
                   codMatricula,
                   digMatricula,
                   nome,
                   sessao;

    private transient String senha;

    public UsuarioPortal() {}

    public UsuarioPortal( String usuario, String senha ) {

        this.usuario = usuario;
        this.senha   = senha;
    }

    private UsuarioPortal( Parcel in ) {

        idUsua       = in.readLong();
        idServ       = in.readLong();
        usuario      = in.readString();
        codMatricula = in.readString();
        digMatricula = in.readString();
        nome         = in.readString();
        sessao       = in.readString();
    }

    public Long getIdUsua() {
        return idUsua;
    }

    public void setIdUsua(Long idUsua) {
        this.idUsua = idUsua;
    }

    public Long getIdServ() {
        return idServ;
    }

    public void setIdServ(Long idServ) {
        this.idServ = idServ;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getCodMatricula() {
        return codMatricula;
    }

    public void setCodMatricula(String codMatricula) {
        this.codMatricula = codMatricula;
    }

    public String getDigMatricula() {
        return digMatricula;
    }

    public void setDigMatricula(String digMatricula) {
        this.digMatricula = digMatricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSessao() {
        return sessao;
    }

    public void setSessao(String sessao) {
        this.sessao = sessao;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong( idUsua );
        out.writeLong( idServ );
        out.writeString( usuario );
        out.writeString( codMatricula );
        out.writeString( digMatricula );
        out.writeString( nome );
        out.writeString( sessao );
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UsuarioPortal> CREATOR = new Parcelable.Creator<UsuarioPortal>() {

        public UsuarioPortal createFromParcel( Parcel in ) {
            return new UsuarioPortal( in );
        }

        public UsuarioPortal[] newArray( int size ) {
            return new UsuarioPortal[ size ];
        }
    };

    @Override
    public boolean equals( Object o ) {

        if ( this == o ) {
            return true;
        }

        if ( o == null || getClass() != o.getClass() ) {
            return false;
        }

        UsuarioPortal that = ( UsuarioPortal ) o;

        if (!idUsua.equals(that.idUsua)) return false;
        if (idServ != null ? !idServ.equals(that.idServ) : that.idServ != null) return false;

        return usuario.equals(that.usuario);
    }

    @Override
    public int hashCode() {
        int result = idUsua.hashCode();
        result = 31 * result + (idServ != null ? idServ.hashCode() : 0);
        result = 31 * result + usuario.hashCode();
        return result;
    }
}