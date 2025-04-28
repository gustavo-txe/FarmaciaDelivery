package com.app.farmaciadelivery.ui.clientes.pedidos;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.farmaciadelivery.ui.clientes.home.HomeInfoActivity;
import com.app.farmaciadelivery.controllers.ConfirmarPedidoController;
import com.app.farmaciadelivery.repository.UsuarioRepository;
import com.app.farmaciadelivery.R;
import com.google.firebase.database.DatabaseError;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ConfirmarPedidoActivity extends AppCompatActivity {

    private ConfirmarPedidoController confirmarPedidoController = new ConfirmarPedidoController();
    private UsuarioRepository usuarioRepository = new UsuarioRepository();
    private EditText editTextEndereco, editTextContato, editTextObservacao, editTextTroco;
    private String mPagamento;
    private Button buttonConfirm;
    private Button buttonCancel;
    private CheckBox checkDinheiro;
    private TextView textTotalConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmacao);
        getSupportActionBar().hide();

        inicializarComponente();

        //Confirmar/Cancelar pedido do cliente
        buttonCancel.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:

                        Toast.makeText(ConfirmarPedidoActivity.this, "Seu Pedido foi Cancelado!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarPedidoActivity.this);
            builder.setMessage("Deseja Cancelar seu Pedido?").setPositiveButton("Sim", dialogClickListener)
                    .setNegativeButton("Não", dialogClickListener).show();
        });

        buttonConfirm.setOnClickListener(view -> {
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        confirmacaoPedido();
                        finish();
                        startActivity(new Intent(ConfirmarPedidoActivity.this, HomeInfoActivity.class));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(ConfirmarPedidoActivity.this, "Pedido Cancelado!", Toast.LENGTH_SHORT).show();
                        finish();
                        break;

                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmarPedidoActivity.this);
            builder.setTitle("Deseja Confirmar seu Pedido?");
            builder.setMessage("Após a confirmação você poderá conferir os detalhes de seu pedido no Menu 'Meus Pedidos'")
                    .setPositiveButton("Sim", dialogClickListener)
                    .setNegativeButton("Não", dialogClickListener).show();

        });

        recuperarDados();
        onBackPressed();

    }

    //Recuperar dados do usuário na tela de confirmação
    public void recuperarDados() {
        confirmarPedidoController.recuperarDadosUsuario(total -> {
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            textTotalConfirm.setText("Valor Total a pagar: " + nf.format(total));
        });

        usuarioRepository.recuperarContatoEndereco(new UsuarioRepository.DadosUsuarioCallback() {
            @Override
            public void onDadosRecebidos(String telefone, String endereco) {
                if (telefone != null) {
                    editTextContato.setText(telefone);
                }
                if (endereco != null) {
                    editTextEndereco.setText(endereco);
                }
            }

            @Override
            public void onErro(DatabaseError error) {
                Toast.makeText(ConfirmarPedidoActivity.this,
                        "Erro ao carregar dados do usuário", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

    public void confirmacaoPedido() {

        if (confirmarPedidoController.getItensCarrinho().isEmpty()) {
            Toast.makeText(this, "Adicione itens no Carrinho!", Toast.LENGTH_SHORT).show();
            return;
        }

        String troco = editTextTroco.getText().toString();
        String observacao = editTextObservacao.getText().toString();
        String endereco = editTextEndereco.getText().toString();
        String contato = editTextContato.getText().toString();

        if (endereco.isEmpty()) {
            Toast.makeText(ConfirmarPedidoActivity.this, "Por favor, informe o Endereço de entrega!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (contato.isEmpty()) {
            Toast.makeText(ConfirmarPedidoActivity.this, "Por favor, informe o Telefone de contato!",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkDinheiro.isChecked()) {
            mPagamento = "Dinheiro";
        } else {
            mPagamento = "Máquina De Cartão";
        }

        //Confirmar data e hora do pedido
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDateFormat1.format(calendar.getTime());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String dateTime = simpleDateFormat.format(calendar.getTime());

        confirmarPedidoController.confirmarPedido(endereco, contato,
                mPagamento, observacao, date, dateTime, troco);

        if (confirmarPedidoController.getItensCarrinho().isEmpty()) {
            throw new IllegalArgumentException("Adicione itens no carrinho");
        }

        Toast.makeText(ConfirmarPedidoActivity.this, "Seu Pedido foi Confirmado!",
                Toast.LENGTH_SHORT).show();

    }

    public void inicializarComponente() {

        editTextEndereco = findViewById(R.id.editTextEndereco);
        editTextContato = findViewById(R.id.editTextContato);
        editTextObservacao = findViewById(R.id.editTextObservacao);
        editTextTroco = findViewById(R.id.editTextTroco);
        checkDinheiro = findViewById(R.id.checkDinheiro);
        buttonCancel = findViewById(R.id.buttonCancel);
        textTotalConfirm = findViewById(R.id.textTotalConfirm);
        buttonConfirm = findViewById(R.id.buttonConfirm);

    }

}