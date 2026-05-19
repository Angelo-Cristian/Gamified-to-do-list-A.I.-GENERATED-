package com.example.proiectkotlin.ui.screen

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.proiectkotlin.data.remote.UserCredentials
import com.example.proiectkotlin.ui.viewmodel.UserViewModel

private const val TAG = "LoginScreen"

@Composable
fun LoginScreen(viewModel: UserViewModel, onLoginSuccess: (String, String) -> Unit) {
    var isLoginMode by remember { mutableStateOf(true) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLoginMode) "Autentificare" else "Creare Cont",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (!isLoginMode) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nume Complet") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Parolă") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true
        )

        if (!isLoginMode) {
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirmă Parola") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )
        }

        if (viewModel.responseMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = viewModel.responseMessage,
                color = if (viewModel.responseMessage.contains("Eroare") || viewModel.responseMessage.contains("incorectă")) 
                    MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    Log.d(TAG, "Buton apăsat: ${if (isLoginMode) "Login" else "SignUp"}")
                    
                    if (!emailRegex.matches(email)) {
                        Log.w(TAG, "Validare: Format email invalid: $email")
                        return@Button
                    }

                    if (isLoginMode) {
                        if (email.isNotEmpty() && password.isNotEmpty()) {
                            Log.d(TAG, "Inițiere login pentru $email")
                            viewModel.loginUser(email, password, onLoginSuccess)
                        } else {
                            Log.w(TAG, "Validare: Email sau parola goale")
                        }
                    } else {
                        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                            if (password == confirmPassword) {
                                Log.d(TAG, "Inițiere înregistrare pentru $email")
                                viewModel.registerUser(UserCredentials(email, password, name), onLoginSuccess)
                            } else {
                                Log.w(TAG, "Validare: Parolele nu coincid")
                            }
                        } else {
                            Log.w(TAG, "Validare: Câmpuri obligatorii necompletate la înregistrare")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isLoginMode) "Log In" else "Sign Up")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isLoginMode) "Nu ai cont? Înregistrează-te" else "Ai deja cont? Autentifică-te",
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable {
                Log.d(TAG, "Schimbare mod: ${if (isLoginMode) "la SignUp" else "la Login"}")
                isLoginMode = !isLoginMode
            }
        )
    }
}
