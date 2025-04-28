package com.app.farmaciadelivery.ui.clientes.pedidos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.app.farmaciadelivery.controllers.HistoricoPedidosController;
import com.app.farmaciadelivery.ui.adapters.AdapterHistoricoPedidos;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class HistoricoPedidosActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidosClientes;
    private AdapterHistoricoPedidos adapterClientes;
    private List<Pedido> pedidos = new ArrayList<>();
    private ImageView imageIconBack;
    private FloatingActionButton floatingActionButtonZap;
    private HistoricoPedidosController historicoPedidosController = new HistoricoPedidosController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_pedidos);
        getSupportActionBar().hide();

        InicializarComponente();

        imageIconBack.setOnClickListener(v -> {
            onBackPressed();
        });

        historicoPedidosController.recuperarPedidos(pedidos, adapterClientes);

        floatingActionButtonZap.setOnClickListener(v -> {
            openZap();
        });

    }

    public void openZap() {
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=5531995598975&text=Ol%C3%A1!%20Gostaria%20de%20tirar%20d%C3%BAvidas%20sobre%20meu%20pedido!");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void InicializarComponente() {

        imageIconBack = findViewById(R.id.imageBackIcon);
        floatingActionButtonZap = findViewById(R.id.zapActionButton);

        //RecyclerView
        recyclerPedidosClientes = findViewById(R.id.recyclerPedidosCliente);
        recyclerPedidosClientes.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidosClientes.setHasFixedSize(true);
        adapterClientes = new AdapterHistoricoPedidos(pedidos);
        recyclerPedidosClientes.setAdapter(adapterClientes);

    }
}



