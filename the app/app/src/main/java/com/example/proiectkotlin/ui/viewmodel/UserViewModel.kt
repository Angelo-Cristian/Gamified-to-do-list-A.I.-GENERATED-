package com.example.proiectkotlin.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proiectkotlin.data.remote.RetrofitClient
import com.example.proiectkotlin.data.remote.UserCredentials
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    companion object {
        private const val TAG = "UserViewModel"
    }

    var responseMessage by mutableStateOf("")
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun registerUser(credentials: UserCredentials, onSuccess: (String, String) -> Unit) {
        Log.d(TAG, "registerUser: Se încearcă înregistrarea pentru ${credentials.email}")
        viewModelScope.launch {
            isLoading = true
            responseMessage = ""
            try {
                val response = RetrofitClient.api.register(credentials)
                Log.d(TAG, "registerUser: Răspuns primit: ${response.mesaj}")
                responseMessage = response.mesaj
                if (response.status == "success" || response.mesaj.contains("creat", ignoreCase = true)) {
                    Log.i(TAG, "registerUser: Înregistrare reușită pentru ${credentials.email}")
                    onSuccess(credentials.email, credentials.nume)
                } else {
                    Log.w(TAG, "registerUser: Înregistrare eșuată: ${response.mesaj}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "registerUser: Eroare la înregistrare", e)
                responseMessage = "Eroare la înregistrare: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }

    fun loginUser(email: String, parola: String, onSuccess: (String, String) -> Unit) {
        Log.d(TAG, "loginUser: Se încearcă autentificarea pentru $email")
        viewModelScope.launch {
            isLoading = true
            responseMessage = ""
            try {
                val credentials = RetrofitClient.api.login("login?email=${email.trim()}")
                Log.d(TAG, "loginUser: Date primite pentru $email")
                
                if (credentials.parola.trim() == parola.trim()) {
                    Log.i(TAG, "loginUser: Autentificare reușită pentru $email")
                    responseMessage = "Autentificare reușită!"
                    onSuccess(credentials.email, credentials.nume)
                } else {
                    Log.w(TAG, "loginUser: Parolă incorectă pentru $email")
                    responseMessage = "Parolă incorectă!"
                }
            } catch (e: Exception) {
                Log.e(TAG, "loginUser: Eroare la autentificare pentru $email", e)
                responseMessage = "Eroare la autentificare: ${e.localizedMessage}"
            } finally {
                isLoading = false
            }
        }
    }
}
