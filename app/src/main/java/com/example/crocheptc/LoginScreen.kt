package com.example.crocheptc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crocheptc.R

// Fonte customizada (arquivo em res/font/font.ttf)
private val loginFontFamily = FontFamily(
    Font(R.font.fonte)
)

@Composable
fun LoginScreen() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.login), // Certifique-se que esta imagem existe
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(0.1f))

            // Título "Entrar"
            Text(
                fontFamily = loginFontFamily,
                text = "Entrar",
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Input Nome
            var nome by remember { mutableStateOf("") }
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome de Usuario") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.person1),
                        contentDescription = "User Icon"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Input Senha
            var senha by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = senha,
                onValueChange = { senha = it },
                label = { Text("********") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.security),
                        contentDescription = "Password Icon"
                    )
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )

            // Esqueceu a senha
            Text(
                text = "Esqueceu a senha?",
                color = Color.Black,
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 24.dp)
                    .align(Alignment.End)
                    .padding(end = 40.dp) // Ajuste fino para alinhar visualmente
                    .clickable { /* TODO: Implementar ação */ }
            )

            // Botão Entrar
            Button(
                onClick = { /* TODO: Implementar login */ },
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE)) // Cor exemplo
            ) {
                Text("Entrar", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botões Sociais
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { /* TODO: Google Login */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    modifier = Modifier.width(150.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = "Google",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Google", color = Color.Black)
                }

                Button(
                    onClick = { /* TODO: Facebook Login */ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue), // Cor Facebook
                    modifier = Modifier.width(150.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Facebook", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Registrar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = "Ainda não tem uma conta ?",
                    color = Color.White,
                    fontSize = 16.sp
                )
                Text(
                    text = "clique aqui",
                    color = Color.Red,
                    fontSize = 16.sp,
                    modifier = Modifier.clickable { /* TODO: Ir para registro */ }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    MaterialTheme {
        LoginScreen()
    }
}
