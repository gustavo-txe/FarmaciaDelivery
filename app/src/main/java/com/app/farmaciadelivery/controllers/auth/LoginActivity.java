package com.app.farmaciadelivery.controllers.auth;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.app.farmaciadelivery.utils.ConfigFirebase;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.helpers.UsuarioFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginActivity extends AppCompatActivity {

    private EditText editLoginEmail,editLoginSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        inicializarComponentes();

    }

    public void abrirTelaCadastro(View view) {

        startActivity( new Intent(this, CadastroActivity.class));

    }

    @Override
    protected void onStart() {
        super.onStart();
        UsuarioFirebase.redirecionaUsuarioLogado(LoginActivity.this);
    }

    public void validarLoginUsuario(View view){

        String textoEmail = editLoginEmail.getText().toString();
        String textoSenha = editLoginSenha.getText().toString();

        if (textoEmail.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor, preencha o campo de E-Mail!",
                    Toast.LENGTH_SHORT).show();
            editLoginEmail.requestFocus();
            return;
        }

        if (textoSenha.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Por favor, preencha o campo de Senha!",
                    Toast.LENGTH_SHORT).show();
            editLoginSenha.requestFocus();
            return;
        }

        autenticacao.signInWithEmailAndPassword(
                textoEmail, textoSenha
        ).addOnCompleteListener(task -> {

            if(task.isSuccessful()){

                UsuarioFirebase.redirecionaUsuarioLogado(LoginActivity.this);
                Toast.makeText(LoginActivity.this, "Autenticando Usuário ...", Toast.LENGTH_SHORT).show();

            }else{

                String excecao = "";
                try {
                    throw task.getException();
                }catch ( FirebaseAuthInvalidUserException e ){
                    excecao = "Usuário não está cadastrado.";
                }catch ( FirebaseAuthInvalidCredentialsException e){
                    excecao = "E-mail e senha não correspondem a um usuário válido";
                }catch ( Exception e ){
                    excecao = "Erro ao cadastrar usuário:" + e.getMessage();
                    e.printStackTrace();
                }

                Toast.makeText(LoginActivity.this,excecao, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void inicializarComponentes(){

        editLoginEmail = findViewById(R.id.editLoginEmail);
        editLoginSenha = findViewById(R.id.editLoginSenha);
        autenticacao = ConfigFirebase.getFirebaseAutenticacao();

    }

}