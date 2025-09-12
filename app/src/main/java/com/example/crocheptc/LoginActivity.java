package com.example.crocheptc;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText usuarioEditText, senhaEditText;
    private Button btnEntrar, btnGoogle, btnFacebook;
    private TextView btnRegistrar;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Inicializa o SDK do Facebook uma vez só
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());


        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Binding
        usuarioEditText = findViewById(R.id.usuarioEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnEntrar.setOnClickListener(v -> loginComEmailSenha());
        btnRegistrar.setOnClickListener(v -> startActivity(new Intent(this, Registro.class)));

        configurarGoogleLogin();
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        configurarFacebookLogin();
    }

    private void loginComEmailSenha() {
        String usuario = usuarioEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(usuario, senha)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        salvarUsuario(usuario);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        String mensagemUsuario = getString(task);

                        Toast.makeText(LoginActivity.this, mensagemUsuario, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static @NonNull String getString(Task<AuthResult> task) {
        String erroFirebase = task.getException() != null ? task.getException().getMessage() : "";
        String mensagemUsuario;

        if (erroFirebase.contains("badly formatted")) mensagemUsuario = "O e-mail está em formato inválido";
        else if (erroFirebase.contains("no user record")) mensagemUsuario = "Usuário não encontrado";
        else if (erroFirebase.contains("password is invalid")) mensagemUsuario = "Senha incorreta";
        else mensagemUsuario = "Falha no login";
        return mensagemUsuario;
    }

    private void configurarGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void configurarFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        // Registrar callback UMA vez
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());

                Task<AuthResult> authResultTask = mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                        String uid = mAuth.getCurrentUser().getUid();
                        String email = mAuth.getCurrentUser().getEmail() != null ? mAuth.getCurrentUser().getEmail() : "";

                        db.collection("usuarios").document(uid).get().addOnCompleteListener(checkTask -> {
                            if (checkTask.isSuccessful()) {
                                if (checkTask.getResult().exists()) {
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                } else {
                                    salvarUsuario(email);
                                    startActivity(new Intent(LoginActivity.this, TelaBoasVindas.class));
                                }
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Erro ao verificar usuário", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(LoginActivity.this, "Falha no login Facebook", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Login Facebook cancelado", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull FacebookException error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this, "Erro no login Facebook: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Botão apenas chama login
        btnFacebook.setOnClickListener(v -> LoginManager.getInstance()
                .logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile")));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Google
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account == null) {
                    Toast.makeText(this, "Login Google cancelado", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(task1 -> {
                    progressBar.setVisibility(View.GONE);
                    if (task1.isSuccessful()) {
                        salvarUsuario(account.getEmail());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Falha no login Google", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Erro Google Sign In", Toast.LENGTH_SHORT).show();
            }
        }

        // Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void salvarUsuario(String email) {
        if (email == null) email = "";
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("dataCriacao", new Date());

        if (mAuth.getCurrentUser() != null) {
            db.collection("usuarios").document(mAuth.getCurrentUser().getUid())
                    .set(user)
                    .addOnSuccessListener(aVoid -> Log.d("DB", "Usuário salvo"))
                    .addOnFailureListener(e -> Log.w("DB", "Erro ao salvar", e));
        }
    }
}
