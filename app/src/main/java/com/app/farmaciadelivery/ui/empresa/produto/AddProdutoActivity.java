package com.app.farmaciadelivery.ui.empresa.produto;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.farmaciadelivery.controllers.AddProdutoController;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.models.Produto;
import com.google.firebase.storage.StorageReference;

public class AddProdutoActivity extends AppCompatActivity {

    private EditText editProdutoNome, editProdutoDescricao, editProdutoPreco;
    private Uri imageUri;
    private ImageView imageProduct;
    private ImageView iconBackNovoProduto;
    private CheckBox checkRemedio, checkHigiene, checkBeleza, checkOutros;
    private AddProdutoController addProdutoController = new AddProdutoController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_produto_empresa);
        getSupportActionBar().hide();

        inicializarComponentes();

        imageProduct.setOnClickListener(view -> {

            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(i, 2);

        });

        iconBackNovoProduto.setOnClickListener(v -> {
            onBackPressed();
        });

        checkRemedio.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkHigiene.setChecked(false);
                checkBeleza.setChecked(false);
                checkOutros.setChecked(false);
            }
        });
        checkHigiene.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkRemedio.setChecked(false);
                checkBeleza.setChecked(false);
                checkOutros.setChecked(false);
            }
        });
        checkBeleza.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkRemedio.setChecked(false);
                checkHigiene.setChecked(false);
                checkOutros.setChecked(false);
            }
        });
        checkOutros.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                checkRemedio.setChecked(false);
                checkHigiene.setChecked(false);
                checkBeleza.setChecked(false);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            imageProduct.setImageURI(imageUri);

        }

    }

    private void uploadImagemFirebase(Uri uri) {

        StorageReference fileRef = addProdutoController.getFileRef(getFileExtensions(uri));
        fileRef.putFile(uri).addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uriImg -> {

            Produto produto = new Produto();
            String ctg = "outros";
            String promo = "nao";

            if (checkRemedio.isChecked()) {
                ctg = "remedio";
            }
            if (checkHigiene.isChecked()) {
                ctg = "higiene";
            }
            if (checkBeleza.isChecked()) {
                ctg = "beleza";
            }

            String nome = editProdutoNome.getText().toString();
            String descricao = editProdutoDescricao.getText().toString();
            String preco = editProdutoPreco.getText().toString();

            if (nome.isEmpty()) {
                exibirMensagem("Digite um nome para o Produto");
                return;
            }

            if (descricao.isEmpty()) {
                exibirMensagem("Digite uma descrição para o Produto");
                return;
            }

            addProdutoController.setProduto(produto, preco, nome, descricao, ctg, promo);

            if (nome != null && descricao != null) {

                addProdutoController.setUrl(produto, uriImg.toString());
                addProdutoController.salvarProduto(produto);
                Toast.makeText(AddProdutoActivity.this, "Upload realizado com Sucesso", Toast.LENGTH_SHORT).show();

            } else {

                finish();

            }

        })).addOnProgressListener(snapshot -> {
        }).addOnFailureListener(e -> Toast.makeText(AddProdutoActivity.this, "Erro ao fazer upload da imagem",
                Toast.LENGTH_SHORT).show()).addOnCompleteListener(task -> finish());

    }

    private String getFileExtensions(Uri mUri) {

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

    public void validarDadosProduto(View view) {

        String nome = editProdutoNome.getText().toString();
        String descricao = editProdutoDescricao.getText().toString();

        if (nome.isEmpty()) {
            exibirMensagem("Digite um nome para o Produto");
            return;
        }
        if (descricao.isEmpty()) {
            exibirMensagem("Digite uma descrição para o Produto");
            return;
        }
        if (nome != null && descricao != null) {
            uploadImagemFirebase(imageUri);
            exibirMensagem("Produto Salvo com Sucesso!");
            finish();
        } else {
            exibirMensagem("Preencha todas as informações!");
            finish();
        }

    }

    public void exibirMensagem(String texto) {
        Toast.makeText(this, texto, Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes() {

        editProdutoDescricao = findViewById(R.id.editProdutoDescricao);
        editProdutoNome = findViewById(R.id.editProdutoNome);
        editProdutoPreco = findViewById(R.id.editProdutoPreco);
        imageProduct = findViewById(R.id.imageViewProduct);
        iconBackNovoProduto = findViewById(R.id.IconNovoProduto);

        checkRemedio = findViewById(R.id.checkBoxRemedio);
        checkHigiene = findViewById(R.id.checkBoxHigiene);
        checkBeleza = findViewById(R.id.checkBoxBeleza);
        checkOutros = findViewById(R.id.checkBoxOutros);

    }

}