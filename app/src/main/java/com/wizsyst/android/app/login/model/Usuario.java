package com.wizsyst.android.app.login.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rmanenti on 29/04/2016.
 */
public class Usuario implements Parcelable {

    public static final String TAG = "usuario";

    private Long idUser,
                 idServ;

    private String usuario,
                   codMatricula,
                   digMatricula,
                   nome,
                   sessao;

    private transient String senha;

    public Usuario() {}

    public Usuario(String usuario, String senha ) {

        this.usuario = usuario;
        this.senha   = senha;
    }

    private Usuario(Parcel in ) {

        idUser       = in.readLong();
        idServ       = in.readLong();
        usuario      = in.readString();
        codMatricula = in.readString();
        digMatricula = in.readString();
        nome         = in.readString();
        sessao       = in.readString();
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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

        out.writeLong( idUser );
        out.writeLong( idServ );
        out.writeString( usuario );
        out.writeString( codMatricula );
        out.writeString( digMatricula );
        out.writeString( nome );
        out.writeString( sessao );
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Usuario> CREATOR = new Parcelable.Creator<Usuario>() {

        public Usuario createFromParcel(Parcel in ) {
            return new Usuario( in );
        }

        public Usuario[] newArray(int size ) {
            return new Usuario[ size ];
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

        Usuario that = (Usuario) o;

        if (!idUser.equals(that.idUser)) return false;
        if (idServ != null ? !idServ.equals(that.idServ) : that.idServ != null) return false;

        return usuario.equals(that.usuario);
    }

    @Override
    public int hashCode() {
        int result = idUser.hashCode();
        result = 31 * result + (idServ != null ? idServ.hashCode() : 0);
        result = 31 * result + usuario.hashCode();
        return result;
    }
}