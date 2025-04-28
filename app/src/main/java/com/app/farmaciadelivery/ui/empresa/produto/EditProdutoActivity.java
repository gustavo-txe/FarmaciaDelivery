package com.app.farmaciadelivery.ui.empresa.produto;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.farmaciadelivery.controllers.EditController;
import com.app.farmaciadelivery.R;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class EditProdutoActivity extends AppCompatActivity {

    private EditText editTextDescUpdate, editTextPrecoUpdate, editTextNomeUpdate;
    private Button buttonUpdate, buttonCancel;
    private TextView textViewPrecoAtual;
    private ImageView iconBackEdit;
    private CheckBox checkPromocao, checkRetirarPromocao;
    private EditController editController = new EditController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        getSupportActionBar().hide();

        inicializarComponentes();

        //verifica se o item está em promoção
        checkPromocao.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkRetirarPromocao.setChecked(false);
            }
        });
        checkRetirarPromocao.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkPromocao.setChecked(false);
            }
        });

        NumberFormat nf = NumberFormat.getCurrencyInstance();

        //recupera os dados do produto a ser editado
        editTextNomeUpdate.setText(getIntent().getStringExtra("nomeUpdate"));
        editTextDescUpdate.setText(getIntent().getStringExtra("descUpdate"));
        textViewPrecoAtual.setText(nf.format(getIntent().getDoubleExtra("precoUpdate", 0.00)));

        iconBackEdit.setOnClickListener(v -> {
            onBackPressed();
        });

        buttonUpdate.setOnClickListener(v -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(EditProdutoActivity.this);
            alert.setTitle("Deseja editar este Item ?");

            alert.setPositiveButton("Sim", (dialogInterface, i) -> update());
            alert.setNegativeButton("Não", (dialogInterface, i) -> {
            });
            alert.show();

        });

        buttonCancel.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    public void update() {

        String promocao = "nao";
        String nome = editTextNomeUpdate.getText().toString();
        String preco = editTextPrecoUpdate.getText().toString();
        String descricao = editTextDescUpdate.getText().toString();

        double precoAnterior = getIntent().getDoubleExtra("precoUpdate", 0.00);

        if (checkPromocao.isChecked()) {
            promocao = "sim";
        }

        if (nome.isEmpty()) {
            toastText();
            return;
        }

        if (preco.isEmpty()) {
            toastText();
            return;
        }

        if (descricao.isEmpty()) {
            toastText();
            return;
        }

        if (nome != null && descricao != null && preco != null) {
            Toast.makeText(this, "Produto editado com sucesso!",
                    Toast.LENGTH_SHORT).show();
            finish();

        } else {
            toastText();
        }

        Map<String, Object> updates = new HashMap<>();

        updates.put("nome", nome);
        updates.put("preco", Double.parseDouble(preco));
        updates.put("descricao", descricao);
        updates.put("promocao", promocao);
        updates.put("precoAnterior", precoAnterior);

        if (!checkPromocao.isChecked()) {
            updates.put("precoAnterior", Double.parseDouble(preco));
        }

        editController.atualizarProduto(updates, getIntent().getStringExtra("idUpdate"));

    }

    public void toastText() {
        Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
    }

    public void inicializarComponentes() {

        editTextDescUpdate = findViewById(R.id.editTextDescUpdate);
        editTextPrecoUpdate = findViewById(R.id.editTextPrecoUpdate);
        editTextNomeUpdate = findViewById(R.id.editTextNomeUpdate);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        textViewPrecoAtual = findViewById(R.id.textViewPrecoAtual);
        iconBackEdit = findViewById(R.id.IconEditProduto);
        buttonCancel = findViewById(R.id.buttonUpdateCancel);
        checkPromocao = findViewById(R.id.checkPromocao);
        checkRetirarPromocao = findViewById(R.id.checkPromocaoRetrirar);

    }

}