package com.app.farmaciadelivery.controllers.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.farmaciadelivery.ui.empresa.home.EmpresaActivity;
import com.app.farmaciadelivery.ui.clientes.home.HomeInfoActivity;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.models.Usuario;
import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.app.farmaciadelivery.ui.termos.PoliticsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha, campoSenhaConfirm;
    private Switch switchTipoUsuario;
    private FirebaseAuth autenticacao;
    private TextView textViewPolitics;
    private CheckBox checkPoliticas;
    private Button cadastrarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        getSupportActionBar().hide();

        inicializar();

        textViewPolitics.setOnClickListener(v -> {
            abrirPoliticas();
        });

        cadastrarButton.setOnClickListener(v -> {
            validarCadastroUsuario();
        });

    }

    public void abrirPoliticas() {

        String url = "https://docs.google.com/document/d/e/2PACX-1vQx3NkpqWDORfoiOC-" +
                "Vu6ZH3MxA3FhFIxlqqCaiujTPNRl2tZRE84YCxLyDckyT0UpAs87kPkRPvkN3/pub";

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void validarCadastroUsuario() {

        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        String textoSenhaConfirm = campoSenhaConfirm.getText().toString();

        if (textoNome.isEmpty()) {
            Toast.makeText(CadastroActivity.this, "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }

        if (textoEmail.isEmpty()) {
            Toast.makeText(CadastroActivity.this, "Preencha o E-mail!",
                    Toast.LENGTH_SHORT).show();
        }

        if (!textoSenha.isEmpty()) {

            if (!textoSenha.equals(textoSenhaConfirm)) {

                Toast.makeText(CadastroActivity.this, "Confirme sua senha corretamente!",
                        Toast.LENGTH_SHORT).show();


            }

        } else {
            Toast.makeText(CadastroActivity.this, "Preencha o campo *Senha!",
                    Toast.LENGTH_SHORT).show();
        }

        if(checkPoliticas.isChecked() &&
                !textoSenha.isEmpty() &&
                !textoSenhaConfirm.isEmpty() &&
                !textoEmail.isEmpty() && !textoNome.isEmpty()){

            Usuario usuario = new Usuario();
            usuario.setNome(textoNome);
            usuario.setTipo(veriricaTipoUsuario());

            cadastrarUsuario(usuario);

        }else{
            Toast.makeText(CadastroActivity.this, "Aceite os Termos e preencha todos os campos!",
                    Toast.LENGTH_SHORT).show();
        }

    }

    public void cadastrarUsuario(Usuario usuario) {
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                campoEmail.getText().toString(),
                campoSenha.getText().toString()).addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {

                try {
                    String idUsuario = task.getResult().getUser().getUid();
                    usuario.setId(idUsuario);
                    usuario.salvar();

                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());

                    if (veriricaTipoUsuario() == "U") {

                        startActivity(new Intent(CadastroActivity.this, HomeInfoActivity.class));
                        finish();

                        Toast.makeText(CadastroActivity.this, "cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();

                    } else {

                        startActivity(new Intent(CadastroActivity.this, EmpresaActivity.class));
                        finish();

                        Toast.makeText(CadastroActivity.this, "cadastrado com Sucesso!", Toast.LENGTH_SHORT).show();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {

                String excecao = "";
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    excecao = "Digite uma senha mais forte";
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    excecao = "Por favor, digite um e-mail válido";
                } catch (FirebaseAuthUserCollisionException e) {
                    excecao = "Esta conta já foi cadastrada";
                } catch (Exception e) {
                    excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();

            }

        });

    }

    public String veriricaTipoUsuario() {
        return switchTipoUsuario.isChecked() ? "E" : "U";

    }

    public void inicializar() {
        campoNome = findViewById(R.id.campoNome);
        campoEmail = findViewById(R.id.campoEmail);
        campoSenha = findViewById(R.id.campoSenha);
        switchTipoUsuario = findViewById(R.id.tipoUsuario);
        campoSenhaConfirm = findViewById(R.id.campoSenhaConfirmacao);
        textViewPolitics = findViewById(R.id.textViewPoliticas);
        checkPoliticas = findViewById(R.id.checkBoxPoliticas);
        cadastrarButton = findViewById(R.id.buttonCadastrar);

    }

}