package com.app.farmaciadelivery.controllers.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.farmaciadelivery.ui.clientes.perfil.PerfilUsuarioActivity;
import com.app.farmaciadelivery.ui.adapters.AdapterProduto;
import com.app.farmaciadelivery.ui.clientes.pedidos.CarrinhoActivity;
import com.app.farmaciadelivery.ui.clientes.pedidos.HistoricoPedidosActivity;
import com.app.farmaciadelivery.ui.clientes.pedidos.ConfirmarPedidoActivity;
import com.app.farmaciadelivery.ui.clientes.info.HelpActivity;
import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.models.Pedido;
import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.models.ItemPedido;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.models.Usuario;
import com.app.farmaciadelivery.ui.listener.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutosCardapio;
    private Empresa empresaSelecionada;
    private int qtdItensCarrinho;
    private Double totalCarrinho;
    private TextView textCarrinhoTotal;
    private AdapterProduto adapterProduto;
    private List<Produto> produtos = new ArrayList<>();
    private List<ItemPedido> itensCarrinho = new ArrayList<>();
    private String idEmpresa;
    private Usuario usuario;
    private Pedido pedido;
    private SearchView searchView;
    private View relativeLayout;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private FloatingActionButton fButton, fButton1, fButton2, zapButton;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    private List<Produto> filteredList = new ArrayList<>();
    private HomeController homeController = new HomeController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);
        getSupportActionBar().hide();

        InicializarComponentes();

        zapButton.setOnClickListener(v -> {
            abrirWhatsaap();
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                filterList(text);
                return true;
            }
        });

        //Animações
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);

        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);

        fButton.setOnClickListener(v -> animateFab());

        fButton1.setOnClickListener(v -> {

            animateFab();
            abrirActivity(CarrinhoActivity.class);
        });

        fButton2.setOnClickListener(v -> {

            animateFab();
            abrirActivity(ConfirmarPedidoActivity.class);

        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(HomeActivity.this,
                drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.promocaoMenu:
                    filterPromo();
                    break;

                case R.id.menuCtgAll:
                    filterOff();
                    break;

                case R.id.menuCtgRemedio:
                    filterCtg("remedio");
                    break;

                case R.id.menuCtgHigiene:
                    filterCtg("higiene");
                    break;

                case R.id.menuCtgBeleza:
                    filterCtg("beleza");
                    break;

                case R.id.meu_carrinho:
                    abrirActivity(CarrinhoActivity.class);
                    break;

                case R.id.pedidos_historico:
                    abrirActivity(HistoricoPedidosActivity.class);
                    break;

                case R.id.info:
                    onBackPressed();
                    break;

                case R.id.help:
                    abrirActivity(HelpActivity.class);
                    break;

                case R.id.politicas:
                    abrirPoliticas();
                    break;

                case R.id.sair:
                    onBackPressed();
                    finish();
                    break;

                case R.id.meuPerfil:
                    abrirActivity(PerfilUsuarioActivity.class);
                    break;

            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        recyclerProdutosCardapio.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerProdutosCardapio, new RecyclerItemClickListener.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                confirmarQuantidade(position);
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));

        recuperarProdutos();
        recuperarDadosUsuario();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            empresaSelecionada = (Empresa) bundle.getSerializable("empresas");
            idEmpresa = empresaSelecionada.getIdUsuario();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.setQuery("", false);
        relativeLayout.requestFocus();
    }

    //Recuperar produtos
    private void recuperarProdutos() {

        homeController.getProdutosRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                produtos.clear();
                filteredList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    produtos.add(ds.getValue(Produto.class));
                    filteredList.add(ds.getValue(Produto.class));
                }
                adapterProduto.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    //Filtro de pesquisa
    private void filterList(String text) {
        filteredList.clear();
        for (Produto produto : produtos) {

            if (produto.getNome().toLowerCase().contains(text.toLowerCase())) {

                filteredList.add(produto);

            }
        }
        if (filteredList.isEmpty()) {

        } else {
            adapterProduto.setFilteredList(filteredList);
        }

    }

    private void filterCtg(String text) {

        filteredList.clear();

        for (Produto produto : produtos) {

            if (produto.getCategoria().equals(text)) {

                filteredList.add(produto);

            }
        }

        if (filteredList.isEmpty()) {
            for (Produto produto : produtos) {

                if (produto.getNomeProduto() != " ") {

                    filteredList.add(produto);

                }
            }
            Toast.makeText(this, "Nenhum item encontrado nesta categoria...",
                    Toast.LENGTH_SHORT).show();
        } else {
            adapterProduto.setFilteredList(filteredList);
        }

    }

    private void filterPromo() {

        filteredList.clear();

        for (Produto produto : produtos) {

            if (produto.getPromocao().equals("sim") && produto.getPromocao() != null) {
                filteredList.add(produto);
            }

        }
        if (filteredList.isEmpty()) {

            for (Produto produto : produtos) {

                if (produto.getNomeProduto() != " ") {

                    filteredList.add(produto);

                }
            }
            Toast.makeText(this, "Nenhum item encontrado nesta categoria...",
                    Toast.LENGTH_SHORT).show();

        } else {
            adapterProduto.setFilteredList(filteredList);
        }

    }

    private void filterOff() {
        filteredList.clear();
        for (Produto produto : produtos) {

            if (produto.getNomeProduto() != " ") {

                filteredList.add(produto);

            }
        }
        if (filteredList.isEmpty()) {

        } else {
            adapterProduto.setFilteredList(filteredList);
        }

    }

    //Recuperar todos os pedidos
    public void recuperarPedido() {

        homeController.getPedidoRef(idEmpresa).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                qtdItensCarrinho = 0;
                totalCarrinho = 0.0;
                itensCarrinho = new ArrayList<>();

                if (dataSnapshot.getValue() != null) {

                    pedido = dataSnapshot.getValue(Pedido.class);
                    itensCarrinho = pedido.getItens();

                    for (ItemPedido itemP : itensCarrinho) {

                        int qtde = itemP.getQuantidade();
                        Double preco = itemP.getPreco();

                        totalCarrinho += (qtde * preco);
                        qtdItensCarrinho += qtde;

                    }
                }

                DecimalFormat df = new DecimalFormat("0.00");
                textCarrinhoTotal.setText("R$ " + df.format(totalCarrinho));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }

    public void recuperarDadosUsuario() {

        homeController.getUsuariosRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getValue() != null) {
                    usuario = dataSnapshot.getValue(Usuario.class);
                }
                recuperarPedido();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    //Adição de itens ao carrinho
    private void confirmarQuantidade(int posicao) {

        Produto produtoSelecionado = produtos.get(posicao);

        if (filteredList.size() > 0) {
            produtoSelecionado = filteredList.get(posicao);
        }

        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(100);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja Adicionar '" + produtoSelecionado.getNome() + "' ao Carrinho?");
        builder.setView(numberPicker);

        builder.setPositiveButton("ADICIONAR", (dialog, which) -> {

            homeController.getEmpresaRef().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.getValue() != null) {

                        Produto produtoSelecionado = produtos.get(posicao);
                        Empresa empresa = dataSnapshot.getValue(Empresa.class);

                        if (filteredList.size() > 0) {
                            produtoSelecionado = filteredList.get(posicao);
                        }

                        homeController.setItemPedido(produtoSelecionado, empresa,
                                itensCarrinho, numberPicker);

                        if (pedido == null) {
                            pedido = new Pedido(homeController.getIdUsuarioLogado(), idEmpresa);
                        }

                        homeController.setPedido(pedido, itensCarrinho, usuario);
                        homeController.salvarItensCarrinho(pedido);

                        Toast.makeText(HomeActivity.this, "Produto adicionado ao carrinho!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });

        builder.setNegativeButton("CANCELAR", (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void abrirActivity(Class context) {
        startActivity(new Intent(HomeActivity.this, context));
    }

    public void abrirWhatsaap() {
        Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=5531995598975&text=Ol%C3%A1!%20Gostaria%20de%20tirar%20d%C3%BAvidas%20sobre%20meu%20pedido!");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void abrirPoliticas() {

        String url = "https://docs.google.com/document/d/e/2PACX-1vQx3NkpqWDORfoiOC-" +
                "Vu6ZH3MxA3FhFIxlqqCaiujTPNRl2tZRE84YCxLyDckyT0UpAs87kPkRPvkN3/pub";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //Animações
    private void animateFab() {
        if (isOpen) {

            fButton.startAnimation(rotateBackward);
            fButton1.startAnimation(fabClose);
            fButton2.startAnimation(fabClose);
            zapButton.startAnimation(fabClose);

            fButton1.setClickable(false);
            fButton2.setClickable(false);
            zapButton.setClickable(false);
            isOpen = false;

        } else {

            fButton.startAnimation(rotateForward);
            fButton1.startAnimation(fabOpen);
            fButton2.startAnimation(fabOpen);
            zapButton.startAnimation(fabOpen);

            fButton1.setClickable(true);
            fButton2.setClickable(true);
            zapButton.setClickable(true);
            isOpen = true;

        }

    }

    public void InicializarComponentes() {

        textCarrinhoTotal = findViewById(R.id.textCarrinhoTotal);
        relativeLayout = findViewById(R.id.linearLayout2);
        searchView = findViewById(R.id.searchView);
        fButton = findViewById(R.id.fbuttonBackHome);
        fButton1 = findViewById(R.id.floatingActionButton1);
        fButton2 = findViewById(R.id.floatingActionButton2);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        zapButton = findViewById(R.id.floatingActionZap);

        //Recycler View
        recyclerProdutosCardapio = findViewById(R.id.recyclerProdutosCardapio);
        recyclerProdutosCardapio.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProdutosCardapio.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutosCardapio.setAdapter(adapterProduto);

    }

}

