package com.app.farmaciadelivery.ui.termos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.app.farmaciadelivery.R;

public class PoliticsActivity extends AppCompatActivity {

    private ImageView imageIconBackPolitics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politics);
        getSupportActionBar().hide();

        inicializarComponentes();

        imageIconBackPolitics.setOnClickListener(v -> {
            onBackPressed();
        });

    }

    private void inicializarComponentes() {
        imageIconBackPolitics = findViewById(R.id.imageBackHelpPolitics);
    }

}