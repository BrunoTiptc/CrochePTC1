package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Layout com dois botões: Login e Registrar
    }

    // Abrir tela de login
    public void abrir_login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Abrir tela de registro
    public void abrir_tela_sobre(View v) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}
