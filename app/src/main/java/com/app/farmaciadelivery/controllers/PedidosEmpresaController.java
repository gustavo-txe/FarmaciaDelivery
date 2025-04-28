package com.app.farmaciadelivery.controllers;

import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.repository.PedidoRepository;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

public class PedidosEmpresaController {

    private PedidoRepository pedidoRepository = new PedidoRepository();
    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private String idEmpresa = UsuarioFirebase.getIdUsuario();
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferencePedidos;

    public DatabaseReference getDatabaseReferencePedidos() {
        databaseReference = firebaseRef.child("pedidos").child("oLuJHUaBy3cW04i9laZ6zb18ccW2");
        return databaseReference;
    }

    public DatabaseReference getPedidos() {
        databaseReferencePedidos = firebaseRef.child("pedidos").child(idEmpresa);
        return databaseReferencePedidos;
    }

    public void confirmarStatus(Pedido pedido) {
        pedidoRepository.confirmarStatus(pedido);
    }

    public void removerPedidoEmpresa(Pedido pedido) {
        pedidoRepository.removerPedidoEmpresa(pedido);
    }

    public void statusConfirmadoExcluir(Pedido pedido) {
        pedidoRepository.statusConfirmadoExcluir(pedido);
    }

}
