package com.app.farmaciadelivery.models;

import java.io.Serializable;

public class Empresa implements Serializable {

    private String idUsuario;
    private String nome;
    private String tempo;
    private String horarioDeFuncionamento;
    private String endereco;
    private String telefone;
    private double taxaDeEntrega;

    public Empresa() {
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getHorarioDeFuncionamento() {
        return horarioDeFuncionamento;
    }

    public void setHorarioDeFuncionamento(String horarioDeFuncionamento) {
        this.horarioDeFuncionamento = horarioDeFuncionamento;
    }

    public double getTaxaDeEntrega() {
        return taxaDeEntrega;
    }

    public void setTaxaDeEntrega(double taxaDeEntrega) {
        this.taxaDeEntrega = taxaDeEntrega;
    }
}
