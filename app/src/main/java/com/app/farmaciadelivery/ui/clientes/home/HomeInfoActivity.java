package com.app.farmaciadelivery.ui.clientes.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.app.farmaciadelivery.controllers.home.HomeActivity;
import com.app.farmaciadelivery.controllers.HomeInfoController;
import com.app.farmaciadelivery.ui.adapters.AdapterEmpresa;
import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.ui.listener.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class HomeInfoActivity extends AppCompatActivity {

    private RecyclerView recyclerEmpresa;
    private AdapterEmpresa adapterEmpresa;
    private List<Empresa> empresaList = new ArrayList<>();
    private ImageView iconLogout;
    private HomeInfoController homeInfoController = new HomeInfoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        InicializarComponentes();
        recuperarDadosEmpresa();

        iconLogout.setOnClickListener(v ->
                deslogarUsuario()
        );

    }

    private void recuperarDadosEmpresa() {

        homeInfoController.recuperarEmpresa(empresaList, adapterEmpresa);

        recyclerEmpresa.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        recyclerEmpresa,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Empresa empresaSelecionada = empresaList.get(position);
                                Intent i = new Intent(HomeInfoActivity.this, HomeActivity.class);
                                i.putExtra("empresas", empresaSelecionada);
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        })
        );

    }

    public void deslogarUsuario() {

        try {
            homeInfoController.getAutenticacao().signOut();
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void InicializarComponentes() {

        recyclerEmpresa = findViewById(R.id.recyclerEmpresa);
        recyclerEmpresa.setLayoutManager(new LinearLayoutManager(this));
        recyclerEmpresa.setHasFixedSize(true);
        adapterEmpresa = new AdapterEmpresa(empresaList);
        recyclerEmpresa.setAdapter(adapterEmpresa);

        iconLogout = findViewById(R.id.logOut);

    }

}
