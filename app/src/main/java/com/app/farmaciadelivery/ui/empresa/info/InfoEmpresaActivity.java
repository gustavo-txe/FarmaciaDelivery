package com.app.farmaciadelivery.ui.empresa.info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.farmaciadelivery.controllers.InfoEmpresaController;
import com.app.farmaciadelivery.models.Empresa;
import com.app.farmaciadelivery.R;

public class InfoEmpresaActivity extends AppCompatActivity {

    private EditText editEmpresaNome, editEmpresaTempo, editEmpresaFuncionamento, editEmpresaEndereco, editEmpresaTel, editEmpresaTaxa;
    private ImageView iconBack;
    private InfoEmpresaController infoEmpresaController = new InfoEmpresaController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_empresa);
        getSupportActionBar().hide();

        inicializarComponentes();

        infoEmpresaController.recuperarDadosEmpresa((nome, horarioFuncionamento, tempo,
                                                     telefone, endereco, taxa) -> {
            editEmpresaNome.setText(nome);
            editEmpresaFuncionamento.setText(horarioFuncionamento);
            editEmpresaTempo.setText(tempo);
            editEmpresaTel.setText(telefone);
            editEmpresaEndereco.setText(endereco);
            editEmpresaTaxa.setText(taxa);

        });

        iconBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    public void validarDadosEmpresa(View view) {

        String nome = editEmpresaNome.getText().toString();
        String tempo = editEmpresaTempo.getText().toString();
        String horarioFuncionamento = editEmpresaFuncionamento.getText().toString();
        String endereco = editEmpresaEndereco.getText().toString();
        String telefone = editEmpresaTel.getText().toString();
        String taxa = editEmpresaTaxa.getText().toString();

        if (nome.isEmpty()) {
            exibirMensagem("Digite um nome para a empresa");
            return;

        }
        if (tempo.isEmpty()) {
            exibirMensagem("Digite um prazo de entrega");
            return;

        }
        if (horarioFuncionamento.isEmpty()) {
            exibirMensagem("Digite uma categoria");
            return;

        }
        if (endereco.isEmpty()) {
            exibirMensagem("Digite um endereço");
            return;

        }
        if (telefone.isEmpty()) {
            exibirMensagem("Digite um Telefone");
            return;

        }
        if (taxa.isEmpty()) {
            exibirMensagem("Preencha todos os campos");
            return;

        }

        Empresa empresa = new Empresa();

        infoEmpresaController.setEmpresa(empresa, nome, horarioFuncionamento, tempo, telefone,
                endereco, taxa);
        finish();

    }

    public void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes() {

        editEmpresaFuncionamento = findViewById(R.id.editEmpresaFuncionamento);
        editEmpresaTempo = findViewById(R.id.editEmpresaTempo);
        editEmpresaNome = findViewById(R.id.editEmpresaNome);
        editEmpresaEndereco = findViewById(R.id.editEmpresaEndereco);
        editEmpresaTel = findViewById(R.id.editEmpresaTel);
        editEmpresaTaxa = findViewById(R.id.editEmpresaTaxa);
        iconBack = findViewById(R.id.imageBackIcon);

    }
}