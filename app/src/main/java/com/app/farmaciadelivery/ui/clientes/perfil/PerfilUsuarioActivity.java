package com.app.farmaciadelivery.ui.clientes.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.farmaciadelivery.controllers.PerfilUsuarioController;
import com.app.farmaciadelivery.controllers.auth.CadastroActivity;
import com.app.farmaciadelivery.R;

import java.util.HashMap;
import java.util.Map;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private ImageView imageBackUser;
    private TextView textNomeUser, textViewEndereco, textViewContato, textViewEmail;
    private Button buttonEndereco, buttonPhone, deleteButton;
    private PerfilUsuarioController perfilUsuarioController = new PerfilUsuarioController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfilusuario_main);
        getSupportActionBar().hide();

        inicializarComponentes();

        imageBackUser.setOnClickListener(v -> {
            onBackPressed();
        });

        String nomeUser = perfilUsuarioController.getNomeUser();
        String emailUser = perfilUsuarioController.getEmailUser();

        textNomeUser.setText(nomeUser);
        textViewEmail.setText(emailUser);

        recuperarInfomacoesUsuario();
        contaUsuario();

    }

    //Deletar conta do usuário permanentemente
    void contaUsuario() {
        deleteButton.setOnClickListener(v -> {

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("ATENÇÃO\nDeseja excluir sua conta?");
            alert.setMessage("Após a confirmação, todos os seus dados cadastrados no aplicativo " +
                    "serão permanentemente apagados, deseja prosseguir com a exclusão da conta ?");
            alert.setPositiveButton("Confirmar", (dialogInterface, i) ->
                    perfilUsuarioController.getUserRef().setValue(null).addOnSuccessListener(unused ->
                            perfilUsuarioController.getUser().delete().addOnCompleteListener(task -> {

                                if (task.isSuccessful()) {

                                    Intent intent = new Intent(PerfilUsuarioActivity.this, CadastroActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    Toast.makeText(PerfilUsuarioActivity.this, "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show();
                                }
                            })));

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        });

        //Adicionar endereço no perfil do usuário
        buttonEndereco.setOnClickListener(v -> {

            final EditText editText = new EditText(this);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Adicione um endereço de entrega: ");
            alert.setView(editText);
            alert.setPositiveButton("Confirmar", (dialogInterface, i) -> {

                String enderecoAdd = editText.getText().toString();

                Map<String, Object> updates = new HashMap<>();
                updates.put("endereco", enderecoAdd);
                perfilUsuarioController.getUserRef().updateChildren(updates);

                onBackPressed();
                Toast.makeText(PerfilUsuarioActivity.this
                        , "Endereço adicionado com sucesso!", Toast.LENGTH_SHORT).show();

                finish();

            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });

            alert.show();

        });

        //Adicionar número de contato no perfil do usuário
        buttonPhone.setOnClickListener(v -> {

            final EditText editText = new EditText(this);
            editText.setInputType(InputType.TYPE_CLASS_PHONE);
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Adicione um número de contato: ");
            alert.setView(editText);
            alert.setPositiveButton("Confirmar", (dialogInterface, i) -> {

                String phoneAdd = editText.getText().toString();

                Map<String, Object> updates = new HashMap<>();
                updates.put("telefone", phoneAdd);
                perfilUsuarioController.getUserRef().updateChildren(updates);

                Toast.makeText(PerfilUsuarioActivity.this
                        , "Número de contato adicionado com sucesso!", Toast.LENGTH_SHORT).show();

                finish();

            });

            alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            alert.show();

        });
    }

    void recuperarInfomacoesUsuario() {

        perfilUsuarioController.recuperarDadosUsuario(new PerfilUsuarioController.DadosUsuarioCallback() {
            @Override
            public void onEnderecoRecebido(String endereco) {
                textViewEndereco.setText(endereco);
            }

            @Override
            public void onTelefoneRecebido(String telefone) {
                textViewContato.setText(telefone);
            }
        });

    }

    public void inicializarComponentes() {
        imageBackUser = findViewById(R.id.imageBackUserPerfil);
        textNomeUser = findViewById(R.id.textPerfilUserName);
        textViewEndereco = findViewById(R.id.textViewEnderecoGet);
        textViewContato = findViewById(R.id.textViewContatoGet);
        buttonEndereco = findViewById(R.id.buttonAddEndereco);
        buttonPhone = findViewById(R.id.buttonAddPhone);
        textViewEmail = findViewById(R.id.textViewEmailUser);
        deleteButton = findViewById(R.id.buttonDeleteAccount);
    }

}