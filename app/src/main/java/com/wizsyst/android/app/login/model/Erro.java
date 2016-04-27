package com.wizsyst.android.app.login.model;

/**
 * Created by rmanenti on 22/04/2016.
 */
public class Erro {

    private String codigo,
                   descricao,
                   solucao;

    public Erro() {}

    public Erro(String codigo, String descricao ) {

        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Erro(String codigo, String descricao, String solucao ) {

        this( codigo, descricao );
        this.solucao = solucao;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSolucao() {
        return solucao;
    }

    public void setSolucao(String solucao) {
        this.solucao = solucao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Erro erro = (Erro) o;

        if (!codigo.equals(erro.codigo)) return false;
        if (descricao != null ? !descricao.equals(erro.descricao) : erro.descricao != null)
            return false;
        return solucao != null ? solucao.equals(erro.solucao) : erro.solucao == null;

    }

    @Override
    public int hashCode() {
        int result = codigo.hashCode();
        result = 31 * result + (descricao != null ? descricao.hashCode() : 0);
        result = 31 * result + (solucao != null ? solucao.hashCode() : 0);
        return result;
    }
}
