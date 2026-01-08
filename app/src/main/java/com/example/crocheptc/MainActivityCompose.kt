
package com.example.crocheptc

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

class MainActivityCompose : ComponentActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                loginViewModel.signInWithCredential(credential)
            } catch (e: Exception) { 
                // Handle error 
            }
        }

        // Facebook Login
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                loginViewModel.signInWithCredential(credential)
            }

            override fun onCancel() { /* Handle cancellation */ }
            override fun onError(error: FacebookException) { /* Handle error */ }
        })


        setContent {
            MaterialTheme {
                Surface {
                    AppNavigation(
                        loginViewModel = loginViewModel, 
                        onGoogleSignIn = { googleSignInLauncher.launch(googleSignInClient.signInIntent) },
                        onFacebookSignIn = { LoginManager.getInstance().logInWithReadPermissions(this@MainActivityCompose, listOf("public_profile", "email")) }
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}

@Composable
fun AppNavigation(loginViewModel: LoginViewModel, onGoogleSignIn: () -> Unit, onFacebookSignIn: () -> Unit) {
    val navController = rememberNavController()
    val user by loginViewModel.user.collectAsState()

    LaunchedEffect(user) {
        if (user != null) {
            navController.navigate("home") { popUpTo("welcome") { inclusive = true } }
        } else {
            // On logout, navigate back to the welcome screen
            navController.navigate("welcome") { popUpTo("home") { inclusive = true } }
        }
    }

    NavHost(navController = navController, startDestination = if (user != null) "home" else "welcome" ) {
        composable("welcome") {
            WelcomeScreen(navController)
        }
        composable("login") {
            LoginScreen(
                onGoogleSignInClick = onGoogleSignIn,
                onFacebookSignInClick = onFacebookSignIn
            )
        }
        composable("home") {
            val currentUser by loginViewModel.user.collectAsState()
            HomeScreen(user = currentUser, onLogout = { 
                loginViewModel.logout()
                // The LaunchedEffect will handle navigation
            })
        }
    }
}
