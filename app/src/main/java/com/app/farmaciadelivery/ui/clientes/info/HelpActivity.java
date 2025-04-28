package com.app.farmaciadelivery.ui.clientes.info;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.app.farmaciadelivery.R;

public class HelpActivity extends AppCompatActivity {

    private ImageView imageIconBackCliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        getSupportActionBar().hide();

        inicializarComponentes();

        imageIconBackCliente.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void inicializarComponentes() {
        imageIconBackCliente = findViewById(R.id.imageBackHelpCliente);
    }
}