package com.example.crocheptc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
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

        // Inicializa SDK do Facebook e Firebase
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this.getApplication());

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bindings
        usuarioEditText = findViewById(R.id.nomeEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        btnEntrar = findViewById(R.id.btnEntrar);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(android.view.View.GONE);

        // Ações dos botões
        btnEntrar.setOnClickListener(v -> loginComEmailSenha());
        btnRegistrar.setOnClickListener(v -> startActivity(new Intent(this, Registro.class)));

        configurarGoogleLogin();
        btnGoogle.setOnClickListener(v -> {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });

        configurarFacebookLogin();
    }

    // ------------------------
    // LOGIN EMAIL/SENHA
    // ------------------------
    private void loginComEmailSenha() {
        String usuario = usuarioEditText.getText().toString().trim();
        String senha = senhaEditText.getText().toString().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(android.view.View.VISIBLE);

        mAuth.signInWithEmailAndPassword(usuario, senha).addOnCompleteListener(task -> {
            progressBar.setVisibility(android.view.View.GONE);
            if (task.isSuccessful()) {
                salvarUsuarioFirestore(mAuth.getCurrentUser());
                redirecionarPosLogin();
            } else {
                Toast.makeText(this, getMensagemErroFirebase(task), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static @NonNull String getMensagemErroFirebase(Task<AuthResult> task) {
        String erroFirebase = task.getException() != null ? task.getException().getMessage() : "";
        if (erroFirebase.contains("badly formatted")) return "O e-mail está em formato inválido";
        else if (erroFirebase.contains("no user record")) return "Usuário não encontrado";
        else if (erroFirebase.contains("password is invalid")) return "Senha incorreta";
        else return "Falha no login";
    }

    // ------------------------
    // LOGIN GOOGLE
    // ------------------------
    private void configurarGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    // ------------------------
    // LOGIN FACEBOOK
    // ------------------------
    private void configurarFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressBar.setVisibility(android.view.View.VISIBLE);
                AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    if (task.isSuccessful() && mAuth.getCurrentUser() != null) {
                        salvarUsuarioFirestore(mAuth.getCurrentUser());
                        redirecionarPosLogin();
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
                progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(LoginActivity.this, "Erro no login Facebook: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        btnFacebook.setOnClickListener(v ->
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email", "public_profile")));
    }

    // ------------------------
    // REDIRECIONAMENTO PÓS LOGIN
    // ------------------------
    private void redirecionarPosLogin() {
        Intent intent = new Intent(LoginActivity.this, TelaBoasVindas.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    // ------------------------
    // RESULTADOS DAS INTENTS
    // ------------------------
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
                    progressBar.setVisibility(android.view.View.GONE);
                    return;
                }
                progressBar.setVisibility(android.view.View.VISIBLE);
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                mAuth.signInWithCredential(credential).addOnCompleteListener(task1 -> {
                    progressBar.setVisibility(android.view.View.GONE);
                    if (task1.isSuccessful()) {
                        salvarUsuarioFirestore(mAuth.getCurrentUser());
                        redirecionarPosLogin();
                    } else {
                        Toast.makeText(this, "Falha no login Google", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (ApiException e) {
                progressBar.setVisibility(android.view.View.GONE);
                Toast.makeText(this, "Erro Google Sign In", Toast.LENGTH_SHORT).show();
            }
        }

        // Facebook
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    // ------------------------
    private void salvarUsuarioFirestore(FirebaseUser user) {
        if (user == null) return;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference usuarioRef = db.collection("usuarios").document(user.getUid());

        usuarioRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().exists()) {
                    Map<String, Object> usuario = new HashMap<>();
                    usuario.put("email", user.getEmail());
                    usuario.put("dataCriacao", new Date());
                    usuario.put("provedorLogin", user.getProviderData().get(1).getProviderId());

                    usuarioRef.set(usuario)
                            .addOnSuccessListener(aVoid -> Log.d("FIRESTORE", "Usuário salvo com sucesso!"))
                            .addOnFailureListener(e -> Log.w("FIRESTORE", "Erro ao salvar usuário", e));
                } else {
                    Log.d("FIRESTORE", "Usuário já existe, não sobrescrito.");
                }
            }
        });
    }

}
