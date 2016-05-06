package com.wizsyst.android.app.login.model;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class Erro {

    public static final String TAG = "erro";

    private String codigo,
                   mensagem,
                   correcao;

    public Erro() {}

    public Erro(String codigo, String mensagem ) {

        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public Erro(String codigo, String mensagem, String correcao ) {

        this( codigo, mensagem );
        this.correcao = correcao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Erro erro = (Erro) o;

        if (!codigo.equals(erro.codigo)) return false;
        if (mensagem != null ? !mensagem.equals(erro.mensagem) : erro.mensagem != null)
            return false;
        return correcao != null ? correcao.equals(erro.correcao) : erro.correcao == null;

    }

    @Override
    public int hashCode() {
        int result = codigo.hashCode();
        result = 31 * result + (mensagem != null ? mensagem.hashCode() : 0);
        result = 31 * result + (correcao != null ? correcao.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format( "Erro de código %s, descrito por '%s'. Sua solução é: %s", codigo, mensagem, correcao );
    }
}
