package com.app.farmaciadelivery.ui.empresa.info;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import com.app.farmaciadelivery.R;

public class HelpEmpresaActivity extends AppCompatActivity {

    private ImageView iconBackEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_empresa);
        getSupportActionBar().hide();

        inicializarComponentes();

        iconBackEmpresa.setOnClickListener( v -> {
            onBackPressed();
        });

    }

    public void inicializarComponentes(){
        iconBackEmpresa = findViewById(R.id.imageBackIconEmpresa);
    }
}