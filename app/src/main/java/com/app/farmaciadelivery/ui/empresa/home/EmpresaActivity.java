package com.app.farmaciadelivery.ui.empresa.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.Toast;

import com.app.farmaciadelivery.controllers.EmpresaController;
import com.app.farmaciadelivery.ui.empresa.pedidos.PedidosEmpresaActivity;
import com.app.farmaciadelivery.ui.adapters.AdapterProduto;
import com.app.farmaciadelivery.ui.empresa.info.InfoEmpresaActivity;
import com.app.farmaciadelivery.models.Produto;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.service.MyService;
import com.app.farmaciadelivery.ui.empresa.info.HelpEmpresaActivity;
import com.app.farmaciadelivery.ui.empresa.produto.AddProdutoActivity;
import com.app.farmaciadelivery.ui.empresa.produto.EditProdutoActivity;
import com.app.farmaciadelivery.ui.listener.RecyclerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class EmpresaActivity extends AppCompatActivity {

    private RecyclerView recyclerProdutos;
    private AdapterProduto adapterProduto;
    private FloatingActionButton floatingActionButton;
    private SearchView searchViewEmpresa;
    private NavigationView navigationViewEmpresa;
    private DrawerLayout drawerLayoutEmpresa;
    private Toolbar toolbarEmpresa;
    private List<Produto> produtos = new ArrayList<>();
    private List<Produto> filteredList = new ArrayList<>();
    private EmpresaController empresaController = new EmpresaController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa);
        getSupportActionBar().hide();

        inicializarComponentes();
        recuperarProdutos();

        //Inicia o serviço em background
        Intent intent = new Intent(this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            stopService(intent);
        }

        searchViewEmpresa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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

        floatingActionButton.setOnClickListener(view -> abrirActivity(AddProdutoActivity.class));

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(EmpresaActivity.this,
                drawerLayoutEmpresa, toolbarEmpresa, R.string.open_drawer, R.string.close_drawer);

        drawerLayoutEmpresa.addDrawerListener(toggle);
        toggle.syncState();

        navigationViewEmpresa.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.promocaoMenuE:
                        filterPromo();
                        break;

                    case R.id.menuCtgAllE:
                        filterOff();
                        break;

                    case R.id.menuCtgRemedioE:
                        filterCtg("remedio");
                        break;

                    case R.id.menuCtgHigieneE:
                        filterCtg("higiene");
                        break;

                    case R.id.menuCtgBelezaE:
                        filterCtg("beleza");
                        break;

                    case R.id.add_produtoE:
                        abrirActivity(AddProdutoActivity.class);
                        break;

                    case R.id.pedidos_empresaE:
                        abrirActivity(PedidosEmpresaActivity.class);
                        break;

                    case R.id.infoE:
                        abrirActivity(InfoEmpresaActivity.class);
                        break;

                    case R.id.helpE:
                        abrirActivity(HelpEmpresaActivity.class);
                        break;

                    case R.id.sairE:
                        deslogarUsuario();
                        break;
                }

                drawerLayoutEmpresa.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        clickItemEditDelete();

    }

    //Editar e Deletar items através do onItemClick e onLongItemClick respectivamente
    void clickItemEditDelete(){
        recyclerProdutos.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerProdutos,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Produto produtoSelecionado = produtos.get(position);

                        if (filteredList.size() > 0) {
                            produtoSelecionado = filteredList.get(position);
                        }

                        AlertDialog.Builder alert = new AlertDialog.Builder(EmpresaActivity.this);
                        alert.setTitle("Deseja editar o Item: '" + produtoSelecionado.getNome() + "' ?");
                        alert.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                Produto produtoSelecionado = produtos.get(position);

                                if (filteredList.size() > 0) {
                                    produtoSelecionado = filteredList.get(position);
                                }

                                String id = produtoSelecionado.getIdProduto();
                                String nome = produtoSelecionado.getNome();
                                Double preco = produtoSelecionado.getPreco();
                                String desc = produtoSelecionado.getDescricao();

                                startActivity(new Intent(EmpresaActivity.this,
                                        EditProdutoActivity.class).putExtra("idUpdate", id)
                                        .putExtra("nomeUpdate", nome)
                                        .putExtra("precoUpdate", preco)
                                        .putExtra("descUpdate", desc));
                            }
                        });

                        alert.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        alert.show();

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                        recyclerProdutos.getAdapter().notifyItemChanged(position);

                        Produto produtoSelecionado = produtos.get(position);

                        if (filteredList.size() > 0) {
                            produtoSelecionado = filteredList.get(position);
                        }

                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EmpresaActivity.this);
                        builder.setTitle("Deseja Remover o Item: '" + produtoSelecionado.getNome() + "' ?");

                        builder.setPositiveButton("Sim", (dialogInterface, i) -> {

                            Produto produto = produtos.get(position);

                            if (filteredList.size() > 0) {
                                produto = filteredList.get(position);
                            }

                            empresaController.removerItem(produto);
                            empresaController.removerImagem(produto);

                            Toast.makeText(EmpresaActivity.this, "Item Removido!",
                                    Toast.LENGTH_SHORT).show();

                        });
                        builder.setNegativeButton("Não", (dialogInterface, i) -> {

                        });

                        androidx.appcompat.app.AlertDialog dialog = builder.create();
                        dialog.show();

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }));
    }


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

    private void recuperarProdutos() {

        empresaController.getProdutosRef().addValueEventListener(new ValueEventListener() {
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

    public void deslogarUsuario() {

        try {
            empresaController.getAutenticacao().signOut();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void abrirActivity(Class context) {

        startActivity(new Intent(EmpresaActivity.this, context));

    }

    private void inicializarComponentes() {

        navigationViewEmpresa = findViewById(R.id.navigation_view_empresa);
        drawerLayoutEmpresa = findViewById(R.id.drawer_layout_empresa);
        toolbarEmpresa = findViewById(R.id.toolbarEmpresa);
        floatingActionButton = findViewById(R.id.fbuttonAdd);
        searchViewEmpresa = findViewById(R.id.searchViewEmpresa);

        //Configurações RecyclerView
        recyclerProdutos = findViewById(R.id.recyclerProdutos);
        recyclerProdutos.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerProdutos.setHasFixedSize(true);
        adapterProduto = new AdapterProduto(produtos, this);
        recyclerProdutos.setAdapter(adapterProduto);

    }

}

