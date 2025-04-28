package com.app.farmaciadelivery.controllers;

import androidx.annotation.NonNull;

import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.ui.adapters.AdapterHistoricoPedidos;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HistoricoPedidosController {

    private DatabaseReference firebaseRef = ConfigFirebase.getFirebase();
    private String idUsuarioLogado = ConfigFirebase.getIdUsuario();

    public void recuperarPedidos(List<Pedido> pedidos, AdapterHistoricoPedidos adapterClientes) {

        DatabaseReference databaseReference = firebaseRef.child("pedidosclientes").child("oLuJHUaBy3cW04i9laZ6zb18ccW2");
        Query filter = databaseReference.orderByChild("idEmpresa").equalTo(idUsuarioLogado);
        filter.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pedidos.clear();
                if (snapshot.getValue() != null) {

                    for (DataSnapshot ds : snapshot.getChildren()) {

                        Pedido pedido = ds.getValue(Pedido.class);
                        pedidos.add(pedido);

                    }

                    adapterClientes.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
