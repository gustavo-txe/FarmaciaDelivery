package com.app.farmaciadelivery.ui.splash;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.app.farmaciadelivery.R;
import com.app.farmaciadelivery.controllers.auth.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_main);
        getSupportActionBar().hide();

        new Handler().postDelayed(() -> abrirAutenticacao(), 3400);

    }

    private void abrirAutenticacao() {

        Intent it = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(it);
        finish();

    }

}