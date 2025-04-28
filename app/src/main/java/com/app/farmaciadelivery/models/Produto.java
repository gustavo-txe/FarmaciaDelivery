package com.app.farmaciadelivery.models;

import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class Produto {

    private String idUsuario;
    private String urlImage;
    private String nome;
    private String idProduto;
    private String descricao;
    private String nomeProduto;
    private double preco;
    private double precoAnterior;
    private int quantidade;
    private String categoria;
    private String promocao;

    public Produto() {

        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef.
                child("produtos");
        setIdProduto(produtoRef.push().getKey());

    }

    public Produto(String urlImage) {

        this.urlImage = urlImage;

    }

    public double getPrecoAnterior() {
        return precoAnterior;
    }

    public void setPrecoAnterior(double precoAnterior) {
        this.precoAnterior = precoAnterior;
    }

    public String getPromocao() {
        return promocao;
    }

    public void setPromocao(String promocao) {
        this.promocao = promocao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }


}
