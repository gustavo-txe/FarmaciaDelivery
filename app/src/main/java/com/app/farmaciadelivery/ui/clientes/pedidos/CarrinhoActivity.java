package com.app.farmaciadelivery.ui.clientes.pedidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.farmaciadelivery.controllers.CarrinhoController;
import com.app.farmaciadelivery.ui.adapters.AdapterCarrinho;
import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.ui.listener.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class CarrinhoActivity extends AppCompatActivity {

    private RecyclerView recyclerPedidos;
    private AdapterCarrinho adapterCarrinho;
    private List<Produto> produtos = new ArrayList<>();
    private FloatingActionButton fButtonBackHome, ConfirmButton, deleteActionButton;
    private TextView textCarrinhoTotal;
    private CarrinhoController carrinhoController = new CarrinhoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        getSupportActionBar().hide();

        inicializarComponente();
        recuperarPedido();
        recuperarProdutos();

        deleteActionButton.setOnClickListener(view -> {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarrinhoActivity.this);
            alertDialog.setTitle("Limpar Carrinho");
            alertDialog.setMessage("Deseja limpar os itens do Carrinho?");
            alertDialog.setCancelable(false);

            alertDialog.setPositiveButton("Confirmar", (dialog, which) -> {

                Toast.makeText(CarrinhoActivity.this, "Carrinho limpo com sucesso!",
                        Toast.LENGTH_SHORT).show();

                carrinhoController.deletarItensCarrinho();
                finish();

            });
            alertDialog.setNegativeButton("Cancelar", (dialog, which) ->
                    Toast.makeText(CarrinhoActivity.this, "Cancelado",
                            Toast.LENGTH_SHORT).show());

            AlertDialog alert = alertDialog.create();
            alert.show();

        });
        ConfirmButton.setOnClickListener(view -> {
            startActivity(new Intent(CarrinhoActivity.this, ConfirmarPedidoActivity.class));
            finish();
        });

        fButtonBackHome.setOnClickListener(v -> onBackPressed());

        recyclerPedidos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerPedidos,
                        new RecyclerItemClickListener.OnItemClickListener() {

                            @Override
                            public void onItemClick(View view, int position) {

                                recyclerPedidos.setEnabled(false);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarrinhoActivity.this);
                                alertDialog.setTitle("Limpar Carrinho");
                                alertDialog.setMessage("Deseja limpar os itens do Carrinho?");
                                alertDialog.setCancelable(false);

                                alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(CarrinhoActivity.this, "Carrinho limpo com sucesso!", Toast.LENGTH_SHORT).show();
                                        carrinhoController.deletarItensCarrinho();
                                        finish();

                                    }
                                });

                                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Toast.makeText(CarrinhoActivity.this, "Cancelado", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                AlertDialog alert = alertDialog.create();
                                alert.show();

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            }
                        }
                )
        );

    }

    public void recuperarPedido() {

        carrinhoController.recuperarDadosUsuario(total -> {
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            textCarrinhoTotal.setText("Valor Total: " + nf.format(total));
        });

    }

    private void recuperarProdutos() {

        carrinhoController.getProdutos().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produtos.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    produtos.add(ds.getValue(Produto.class));
                }

                adapterCarrinho.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void inicializarComponente() {

        deleteActionButton = findViewById(R.id.deleteActionButton);
        ConfirmButton = findViewById(R.id.ConfirmActionButton);
        textCarrinhoTotal = findViewById(R.id.textViewTotal);
        fButtonBackHome = findViewById(R.id.fbuttonBackHome);

        //RecyclerView
        recyclerPedidos = findViewById(R.id.recyclerPedidos);
        recyclerPedidos.setLayoutManager(new LinearLayoutManager(this));
        recyclerPedidos.setHasFixedSize(true);
        adapterCarrinho = new AdapterCarrinho(produtos, this);
        recyclerPedidos.setAdapter(adapterCarrinho);

    }

}