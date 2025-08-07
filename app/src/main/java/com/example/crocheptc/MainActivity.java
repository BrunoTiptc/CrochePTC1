package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // Campos de entrada
    private EditText inputUsername, inputPassword, inputEmail, inputConfirmPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    // Ligação com os elementos da interface
        inputUsername = findViewById(R.id.usuarioEditText);
        inputPassword = findViewById(R.id.inputPassword);
        inputEmail = findViewById(R.id.inputEmail);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
    }

    // Método chamado ao clicar no botão de cadastro
    public void cadastrarUsuario(View v) {
        // Captura os dados digitados
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();

        // Validação básica
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aqui você pode integrar com o Firebase
        Toast.makeText(this, "Cadastro válido. Pronto para enviar ao Firebase!", Toast.LENGTH_SHORT).show();

    }
    //abrir login
    public void abrir_login(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Ir para tela sobre/registro
    public void abrir_tela_sobre(View v) {
        Intent intent = new Intent(this, Registro.class);
        startActivity(intent);
    }
}
