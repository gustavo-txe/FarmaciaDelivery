package com.app.farmaciadelivery.controllers;

import androidx.annotation.NonNull;

import com.app.farmaciadelivery.models.ItemPedido;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.models.Usuario;
import com.app.farmaciadelivery.repository.ProdutoRepository;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarrinhoController {

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private String idUsuarioLogado = ConfigFirebase.getIdUsuario();
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private ProdutoRepository produtoRepository = new ProdutoRepository();
    private Double totalCarrinho;
    private int qtdItensCarrinho;
    private Pedido pedidoRecuperado;
    private Usuario usuario;

    private DatabaseReference recuperarPedidosRef = firebaseRef.child("pedidosusuario")
            .child(idUsuarioLogado)
            .child("oLuJHUaBy3cW04i9laZ6zb18ccW2").child("itens");

    public interface CarrinhoCallback {
        void onRecuperarValorTotal(double total);
    }

    //Recuperar itens do carrinho e calcular valor total
    public void recuperarPedido(CarrinhoCallback callback) {

        DatabaseReference pedidoRef = firebaseRef.child("pedidosusuario").child(idUsuarioLogado).
                child("oLuJHUaBy3cW04i9laZ6zb18ccW2");

        pedidoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                itensCarrinho = new ArrayList<>();

                if (dataSnapshot.getValue() != null) {

                    pedidoRecuperado = dataSnapshot.getValue(Pedido.class);
                    itensCarrinho = pedidoRecuperado.getItens();

                    for (ItemPedido itemP : itensCarrinho) {

                        int qtde = itemP.getQuantidade();
                        Double preco = itemP.getPreco();

                        totalCarrinho += (qtde * preco);
                        qtdItensCarrinho += qtde;

                    }

                    ItemPedido taxaEntrega = itensCarrinho.get(0);
                    Double taxa = taxaEntrega.getTaxa();

                    callback.onRecuperarValorTotal(totalCarrinho + taxa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    public void recuperarDadosUsuario(CarrinhoCallback callback) {

        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(idUsuarioLogado);

        usuariosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperarPedido(callback);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public DatabaseReference getProdutos() {
        return recuperarPedidosRef;
    }

    public void deletarItensCarrinho() {
        produtoRepository.removerProduto();
    }

}
