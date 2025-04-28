package com.app.farmaciadelivery.controllers.home;

import android.widget.NumberPicker;

import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.models.ItemPedido;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.models.Usuario;
import com.app.farmaciadelivery.repository.PedidoRepository;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class HomeController {
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    private DatabaseReference produtosRef;
    private String idUsuarioLogado = ConfigFirebase.getIdUsuario();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private DatabaseReference empresaRef;
    private DatabaseReference usuariosRef;
    private PedidoRepository pedidoRepository = new PedidoRepository();
    private ItemPedido itemPedido = new ItemPedido();

    public DatabaseReference getFirebaseRef() {
        return firebaseRef;
    }

    public String getIdUsuarioLogado() {
        return idUsuarioLogado;
    }

    public DatabaseReference getEmpresaRef() {
        empresaRef = firebaseRef.child("empresa")
                .child("oLuJHUaBy3cW04i9laZ6zb18ccW2");
        return empresaRef;
    }

    public DatabaseReference getProdutosRef() {
        produtosRef = databaseReference.child("produtos")
                .child("oLuJHUaBy3cW04i9laZ6zb18ccW2");
        return produtosRef;
    }

    public DatabaseReference getUsuariosRef() {
        usuariosRef = firebaseRef.child("usuarios")
                .child(idUsuarioLogado);
        return usuariosRef;
    }

    public DatabaseReference getPedidoRef(String idEmpresa) {
        DatabaseReference pedidoRef = firebaseRef.child("pedidosusuario")
                .child(idUsuarioLogado)
                .child(idEmpresa);

        return pedidoRef;
    }

    public void setPedido(Pedido pedido, List<ItemPedido> itensCarrinho, Usuario usuario) {
        pedido.setNome(usuario.getNome());
        pedido.setItens(itensCarrinho);
    }

    public void setItemPedido(Produto produtoSelecionado, Empresa empresa,
                              List<ItemPedido> itensCarrinho, NumberPicker numberPicker) {

        itemPedido.setPreco(produtoSelecionado.getPreco());
        itemPedido.setNomeProduto(produtoSelecionado.getNome());
        itemPedido.setTaxa(empresa.getTaxaDeEntrega());
        itemPedido.setIdProduto(produtoSelecionado.getIdProduto());
        itemPedido.setQuantidade(numberPicker.getValue());
        itensCarrinho.add(itemPedido);

    }

    public void salvarItensCarrinho(Pedido pedido) {
        pedidoRepository.salvarItensCarrinho(pedido);
    }


}