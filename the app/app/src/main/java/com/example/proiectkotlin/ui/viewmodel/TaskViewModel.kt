package com.example.proiectkotlin.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proiectkotlin.data.Task
import com.example.proiectkotlin.data.TaskRepository
import com.example.proiectkotlin.data.TaskStatus
import com.example.proiectkotlin.data.User
import com.example.proiectkotlin.data.remote.RetrofitClient
import com.example.proiectkotlin.data.remote.UserStats
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    companion object {
        private const val TAG = "TaskViewModel"
    }

    private val _currentUserEmail = MutableStateFlow<String?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val user: StateFlow<User?> = _currentUserEmail
        .filterNotNull()
        .flatMapLatest { email -> repository.getUser(email) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val allTasks: StateFlow<List<Task>> = _currentUserEmail
        .filterNotNull()
        .flatMapLatest { email -> repository.getTasksForUser(email) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCurrentUser(email: String, name: String) {
        Log.d(TAG, "setCurrentUser: email=$email, name=$name")
        _currentUserEmail.value = email
        viewModelScope.launch {
            repository.getUser(email).first()?.let { existingUser ->
                val cleanName = name.trim()
                if (cleanName.isNotBlank() && existingUser.name != cleanName) {
                    Log.d(TAG, "setCurrentUser: Se actualizează numele utilizatorului existent")
                    repository.updateUser(existingUser.copy(name = cleanName))
                }
            } ?: run {
                Log.d(TAG, "setCurrentUser: Se creează un utilizator nou local")
                repository.insertUser(User(email = email, name = name.trim()))
            }
        }
    }

    fun refreshTaskStates() {
        val currentTime = System.currentTimeMillis()
        viewModelScope.launch {
            allTasks.value.forEach { task ->
                if (task.status == TaskStatus.PENDING && currentTime >= task.startTime) {
                    Log.d(TAG, "refreshTaskStates: Task-ul '${task.title}' a devenit ACTIV")
                    repository.updateTask(task.copy(status = TaskStatus.ACTIVE))
                }
            }
        }
    }

    fun addTask(title: String, description: String, startDelayMs: Long, durationMs: Long) {
        val currentUserEmail = _currentUserEmail.value
        if (currentUserEmail == null) {
            Log.e(TAG, "addTask: Nu se poate adăuga task, utilizatorul nu este setat")
            return
        }
        
        Log.d(TAG, "addTask: title=$title, delay=$startDelayMs, duration=$durationMs")
        val currentTime = System.currentTimeMillis()
        val newTask = Task(
            userEmail = currentUserEmail,
            title = title,
            description = description,
            startTime = currentTime + startDelayMs,
            endTime = currentTime + startDelayMs + durationMs,
            status = TaskStatus.PENDING
        )
        viewModelScope.launch { 
            repository.insertTask(newTask)
            Log.i(TAG, "addTask: Task inserat cu succes")
        }
    }

    fun completeTask(task: Task) {
        if (task.status != TaskStatus.ACTIVE) {
            Log.w(TAG, "completeTask: Task-ul '${task.title}' nu este activ, nu poate fi finalizat")
            return
        }
        
        Log.d(TAG, "completeTask: Finalizare task '${task.title}'")
        viewModelScope.launch {
            repository.updateTask(task.copy(status = TaskStatus.COMPLETED))
            val currentUser = user.value ?: return@launch
            val updated = currentUser.copy(
                totalXp = currentUser.totalXp + task.xpReward,
                level = ((currentUser.totalXp + task.xpReward) / 1000) + 1
            )
            repository.updateUser(updated)
            Log.i(TAG, "completeTask: Task finalizat. XP actualizat: ${updated.totalXp}")
            syncWithServer(updated)
        }
    }

    private fun syncWithServer(user: User) {
        Log.d(TAG, "syncWithServer: Sincronizare stats pentru ${user.email}")
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.sendUserStats(UserStats(user.name, user.totalXp, user.level, emptyList()))
                Log.d(TAG, "syncWithServer: Răspuns server: $response")
            } catch (e: Exception) {
                Log.e(TAG, "syncWithServer: Eroare la sincronizarea cu serverul", e)
            }
        }
    }
}
