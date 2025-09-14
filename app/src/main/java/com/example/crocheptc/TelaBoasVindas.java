package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class TelaBoasVindas extends AppCompatActivity {

    private Button btnSair;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_boas_vindas);

        mAuth = FirebaseAuth.getInstance();

        btnSair = findViewById(R.id.btnSair);
        btnSair.setOnClickListener(v -> {
            // Fazer logout
            mAuth.signOut();

            // Voltar para a tela inicial (MainActivity)
            Intent intent = new Intent(TelaBoasVindas.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // limpa o histórico
            startActivity(intent);
            finish();
        });
    }
}
