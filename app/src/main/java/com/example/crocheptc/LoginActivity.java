package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText usuarioEditText, senhaEditText;
    Button btnEntrar;
    TextView btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuarioEditText = findViewById(R.id.usuarioEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnEntrar.setOnClickListener(v -> {
            String usuario = usuarioEditText.getText().toString().trim();
            String senha = senhaEditText.getText().toString().trim();

            if (usuario.isEmpty() || senha.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                // Aqui poderia fazer verificação real no Firebase/SQLite
                if (usuario.equals("admin") && senha.equals("123")) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegistrar.setOnClickListener(v ->
                startActivity(new Intent(this, Registro.class))
        );
    }
}
