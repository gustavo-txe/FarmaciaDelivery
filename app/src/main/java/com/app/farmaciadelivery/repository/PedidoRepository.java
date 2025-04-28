package com.app.farmaciadelivery.repository;

import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DatabaseReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PedidoRepository {

    //Confirmar status de visualização para cliente/empresa
    public void confirmarStatus(Pedido pedido) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(pedido.getIdUsuario())
                .child(pedido.getIdPedido())
                .child("status");
        pedidoRef.setValue("PEDIDO VISUALIZADO");

        DatabaseReference firebaseRef02 = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef02 = firebaseRef02
                .child("pedidosclientes")
                .child(pedido.getIdUsuario())
                .child(pedido.getIdPedido())
                .child("status");
        pedidoRef02.setValue("PEDIDO VISUALIZADO: " + getHorarioPedido());

    }

    //Excluir pedido confirmado
    public void statusConfirmadoExcluir(Pedido pedido) {
        DatabaseReference firebaseRef02 = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef02 = firebaseRef02
                .child("pedidosclientes")
                .child(pedido.getIdUsuario())
                .child(pedido.getIdPedido())
                .child("status");
        pedidoRef02.setValue("PEDIDO VISUALIZADO: " + getHorarioPedido());

    }

    public void salvarItensCarrinho(Pedido pedido) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidosusuario")
                .child(pedido.getIdEmpresa())
                .child(pedido.getIdUsuario());
        pedidoRef.setValue(pedido);

    }

    public void confirmarPedidoCliente(Pedido pedido) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(pedido.getIdUsuario())
                .child(pedido.getIdPedido());
        pedidoRef.setValue(pedido);

        DatabaseReference firebaseRef01 = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef01 = firebaseRef01
                .child("pedidosclientes")
                .child(pedido.getIdUsuario())
                .child(pedido.getIdPedido());
        pedidoRef01.setValue(pedido);

    }

    public void removerItensCarrinho(Pedido pedido) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidosusuario")
                .child(pedido.getIdEmpresa())
                .child(pedido.getIdUsuario());
        pedidoRef.removeValue();

    }

    public void removerPedidoEmpresa(Pedido pedido) {
        DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("pedidos")
                .child(pedido.getIdUsuario())
                .child(pedido.getIdPedido());
        pedidoRef.removeValue();

    }

    public String getHorarioPedido() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatHora = new SimpleDateFormat("HH:mm");
        SimpleDateFormat simpleDateFormatData = new SimpleDateFormat("dd/MM/yyyy");

        String data = simpleDateFormatData.format(calendar.getTime());
        String hora = simpleDateFormatHora.format(calendar.getTime());

        String horario = data + "  " + hora;

        return horario;

    }

}
