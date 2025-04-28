package com.app.farmaciadelivery.models;

import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Pedido {
    private String idUsuario;
    private String idEmpresa;
    private String idPedido;
    private String nome;
    private List<ItemPedido> itens;
    private String status = " ";
    private String metodoPagamento;
    private String observacao;
    private String observacaoNumero;
    private String endereco;
    private String hora;

    public Pedido() {
    }

    public Pedido(String idUsu, String idEmp) {
        setIdUsuario(idEmp);
        setIdEmpresa(idUsu);

        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidosusuario")
                .child(idEmp)
                .child(idUsu);
        setIdPedido(pedidoRef.push().getKey());
    }


    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getObservacaoNumero() {
        return observacaoNumero;
    }

    public void setObservacaoNumero(String observacaoNumero) {
        this.observacaoNumero = observacaoNumero;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(String idPedido) {
        this.idPedido = idPedido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMetodoPagamento() {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

}





