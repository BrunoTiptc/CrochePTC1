package com.example.crocheptc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Registro extends AppCompatActivity {

    private EditText nomeEditText, emailEditText, senhaEditText, confirmaSenhaEditText;
    private Button btnRegistrar;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2); // Certifique-se que é o XML correto

        // Bindings
        nomeEditText = findViewById(R.id.nomeEditText);
        emailEditText = findViewById(R.id.emailEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        confirmaSenhaEditText = findViewById(R.id.confirmaSenhaEditText);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        // Inicializa Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Listener do botão
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String nome = nomeEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();
        String confirmaSenha = confirmaSenhaEditText.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmaSenha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!senha.equals(confirmaSenha)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Cria usuário no Firebase Auth
        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                FirebaseUser user = mAuth.getCurrentUser();
                salvarUsuarioFirestore(user, nome); // salva no Firestore
                Toast.makeText(this, "Usuário registrado com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // volta para login
            } else {
                String erro = task.getException() != null ? task.getException().getMessage() : "Falha no registro";
                Toast.makeText(this, erro, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void salvarUsuarioFirestore(FirebaseUser user, String nome) {
        if (user == null) return;

        DocumentReference usuarioRef = db.collection("usuarios").document(user.getUid());

        usuarioRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("nome", nome);
                    usuario.put("email", user.getEmail());
                    usuario.put("dataCriacao", new Date());
                    usuario.put("provedorLogin", "email");

                    usuarioRef.set(usuario)
                            .addOnSuccessListener(aVoid -> Log.d("FIRESTORE", "Usuário salvo com sucesso!"))
                            .addOnFailureListener(e -> Log.w("FIRESTORE", "Erro ao salvar usuário", e));
                } else {
                    Log.d("FIRESTORE", "Usuário já existe, não sobrescrito.");
                }
            } else {
                Log.w("FIRESTORE", "Erro ao verificar usuário", task.getException());
            }
        });
    }
}
