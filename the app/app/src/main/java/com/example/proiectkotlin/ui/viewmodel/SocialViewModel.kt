package com.example.proiectkotlin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proiectkotlin.data.remote.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SocialViewModel : ViewModel() {

    companion object {
        private const val TAG = "SocialViewModel"
    }

    private val _pendingRequests = MutableStateFlow<List<String>>(emptyList())
    val pendingRequests: StateFlow<List<String>> = _pendingRequests

    private val _friendsList = MutableStateFlow<List<String>>(emptyList())
    val friendsList: StateFlow<List<String>> = _friendsList

    private val _socialMessage = MutableStateFlow<String?>(null)
    val socialMessage: StateFlow<String?> = _socialMessage

    fun loadSocialData(userName: String) {
        if (userName.isBlank()) {
            Log.w(TAG, "loadSocialData: userName este gol")
            return
        }
        Log.d(TAG, "loadSocialData: Începere încărcare date pentru: $userName")
        
        viewModelScope.launch {
            try {
                Log.d(TAG, "loadSocialData: Preluare cereri de prietenie...")
                val response = RetrofitClient.api.getPendingRequests(userName.trim())
                if (response.isSuccessful) {
                    val requests = response.body() ?: emptyList()
                    _pendingRequests.value = requests
                    Log.d(TAG, "loadSocialData: Cereri primite: $requests")
                } else {
                    Log.e(TAG, "loadSocialData: Eroare server cereri (Code: ${response.code()}): ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadSocialData: Eroare rețea cereri", e)
            }
        }

        viewModelScope.launch {
            try {
                Log.d(TAG, "loadSocialData: Preluare listă de prieteni...")
                val response = RetrofitClient.api.getFriendsList(userName.trim())
                if (response.isSuccessful) {
                    val friends = response.body() ?: emptyList()
                    _friendsList.value = friends
                    Log.d(TAG, "loadSocialData: Prieteni primiți: $friends")
                } else {
                    Log.e(TAG, "loadSocialData: Eroare server prieteni (Code: ${response.code()}): ${response.errorBody()?.string()}")
                    if (response.code() == 404) {
                        Log.e(TAG, "loadSocialData: ALERTA: Ruta /get_friends nu există pe server!")
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "loadSocialData: Eroare rețea prieteni", e)
            }
        }
    }

    fun sendInvitation(fromName: String, toName: String) {
        Log.d(TAG, "sendInvitation: de la $fromName către $toName")
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.sendFriendRequest(fromName.trim(), toName.trim())
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.i(TAG, "sendInvitation: Invitație trimisă cu succes: ${body?.mesaj}")
                    _socialMessage.value = body?.mesaj
                    loadSocialData(fromName)
                } else {
                    Log.e(TAG, "sendInvitation: Eroare trimitere (Code: ${response.code()})")
                    _socialMessage.value = "Eroare server: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "sendInvitation: Eroare rețea trimitere", e)
                _socialMessage.value = "Eroare: ${e.message}"
            }
        }
    }

    fun respondToRequest(userName: String, friendName: String, accept: Boolean) {
        Log.d(TAG, "respondToRequest: $userName răspunde lui $friendName (accept=$accept)")
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.respondToFriendRequest(userName.trim(), friendName.trim(), accept)
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.i(TAG, "respondToRequest: Răspuns procesat: ${body?.mesaj}")
                    _socialMessage.value = body?.mesaj
                    loadSocialData(userName)
                } else {
                    Log.e(TAG, "respondToRequest: Eroare răspuns (Code: ${response.code()})")
                    _socialMessage.value = "Eroare server: ${response.code()}"
                }
            } catch (e: Exception) {
                Log.e(TAG, "respondToRequest: Eroare rețea răspuns", e)
                _socialMessage.value = "Eroare: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _socialMessage.value = null
    }
}
