package com.example.proiectkotlin.data

import android.util.Log
import com.example.proiectkotlin.data.local.TaskDao
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    companion object {
        private const val TAG = "TaskRepository"
    }

    fun getTasksForUser(email: String): Flow<List<Task>> {
        Log.d(TAG, "getTasksForUser: Preluare task-uri pentru $email")
        return taskDao.getTasksForUser(email)
    }

    fun getUser(email: String): Flow<User?> {
        Log.d(TAG, "getUser: Preluare date utilizator pentru $email")
        return taskDao.getUser(email)
    }

    suspend fun insertTask(task: Task) {
        Log.d(TAG, "insertTask: Inserare task '${task.title}' pentru ${task.userEmail}")
        taskDao.insertTask(task)
    }

    suspend fun updateTask(task: Task) {
        Log.d(TAG, "updateTask: Actualizare task '${task.title}' (ID: ${task.id})")
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(task: Task) {
        Log.d(TAG, "deleteTask: Ștergere task '${task.title}'")
        taskDao.deleteTask(task)
    }

    suspend fun insertUser(user: User) {
        Log.d(TAG, "insertUser: Inserare utilizator nou: ${user.email}")
        taskDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        Log.d(TAG, "updateUser: Actualizare date utilizator: ${user.email}")
        taskDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        Log.d(TAG, "deleteUser: Ștergere utilizator: ${user.email}")
        taskDao.deleteUser(user)
    }
}
