package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    // Componentes da tela
    private EditText usuarioEditText, senhaEditText;
    private Button btnEntrar, btnGoogle, btnFacebook;
    private TextView btnRegistrar;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // Google
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    // Facebook
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Binding da tela
        usuarioEditText = findViewById(R.id.usuarioEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);

        // Login com email/senha
        btnEntrar.setOnClickListener(v -> loginComEmailSenha());

        // Redirecionar para tela de registro
        btnRegistrar.setOnClickListener(v ->
                startActivity(new Intent(this, Registro.class))
        );

        // Configurar Google Sign In
        configurarGoogleLogin();
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        // Configurar Facebook Login
        configurarFacebookLogin();
    }

    // ---------------- LOGIN EMAIL ----------------
    private void loginComEmailSenha() {
        String usuario = usuarioEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(usuario, senha)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        salvarUsuario(usuario);
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Falha no login", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ---------------- LOGIN GOOGLE ----------------
    private void configurarGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // ---------------- LOGIN FACEBOOK ----------------
    private void configurarFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        btnFacebook.setOnClickListener(v -> {
            // Inicia login do Facebook
            LoginManager.getInstance().logInWithReadPermissions(
                    LoginActivity.this,
                    Arrays.asList("email", "public_profile")
            );

            // Callback do login
            LoginManager.getInstance().registerCallback(callbackManager,
                    new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            AuthCredential credential = FacebookAuthProvider.getCredential(
                                    loginResult.getAccessToken().getToken());
                            mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    salvarUsuario(mAuth.getCurrentUser().getEmail());
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "Falha no login Facebook", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancel() {}

                        @Override
                        public void onError(FacebookException error) {
                            Toast.makeText(LoginActivity.this, "Erro no login Facebook", Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    // ---------------- TRATAR RESULTADOS ----------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()) {
                        salvarUsuario(account.getEmail());
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Falha no login Google", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                Toast.makeText(this, "Erro Google Sign In", Toast.LENGTH_SHORT).show();
            }
        }

        // Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // ---------------- SALVAR NO FIRESTORE ----------------
    private void salvarUsuario(String email) {
        if (email == null) return;

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("dataCriacao", new Date());

        db.collection("usuarios").document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(aVoid -> Log.d("DB", "Usuário salvo"))
                .addOnFailureListener(e -> Log.w("DB", "Erro ao salvar", e));
    }
}
