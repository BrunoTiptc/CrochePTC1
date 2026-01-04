package com.example.crocheptc;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

public class TelaBoasVindas extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView rvCategorias;
    private RecyclerView rvProdutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_boas_vindas);

        // Firebase
        mAuth = FirebaseAuth.getInstance();


        // Depois a gente conecta os adapters aqui
    }
}
