package com.example.proiectkotlin.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.proiectkotlin.data.TaskRepository
import com.example.proiectkotlin.data.local.AppDatabase
import com.example.proiectkotlin.ui.screen.LoginScreen
import com.example.proiectkotlin.ui.screen.SocialScreen
import com.example.proiectkotlin.ui.screen.TaskScreen
import com.example.proiectkotlin.ui.theme.ProiectKotlinTheme
import com.example.proiectkotlin.ui.viewmodel.SocialViewModel
import com.example.proiectkotlin.ui.viewmodel.TaskViewModel
import com.example.proiectkotlin.ui.viewmodel.UserViewModel
import com.example.proiectkotlin.worker.NotificationWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val INACTIVITY_WORK_NAME = "inactivity_notification_work"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: Aplicația a pornit")
        
        scheduleInactivityNotification()

        val database = AppDatabase.getDatabase(this)
        val repository = TaskRepository(database.taskDao())
        
        val taskViewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TaskViewModel(repository) as T
            }
        }
        
        val taskViewModel = ViewModelProvider(this, taskViewModelFactory)[TaskViewModel::class.java]
        val userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        val socialViewModel = ViewModelProvider(this)[SocialViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            ProiectKotlinTheme {
                val context = LocalContext.current
                var isLoggedIn by remember { mutableStateOf(false) }
                var showSocialScreen by remember { mutableStateOf(false) }
                val currentUser by taskViewModel.user.collectAsState()

                val launcher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        Log.d(TAG, "Notification permission granted: $isGranted")
                    }
                )

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (!isLoggedIn) {
                        LoginScreen(
                            viewModel = userViewModel,
                            onLoginSuccess = { email, name -> 
                                Log.i(TAG, "LoginSuccess: Utilizatorul $email s-a autentificat")
                                taskViewModel.setCurrentUser(email, name)
                                isLoggedIn = true 
                            }
                        )
                    } else if (showSocialScreen) {
                        SocialScreen(
                            viewModel = socialViewModel,
                            currentUserName = currentUser?.name ?: "",
                            onBack = { 
                                Log.d(TAG, "Revenire la ecranul de task-uri")
                                showSocialScreen = false 
                            }
                        )
                    } else {
                        Scaffold(
                            floatingActionButton = {
                                ExtendedFloatingActionButton(
                                    onClick = { 
                                        Log.d(TAG, "Deschidere ecran Social")
                                        showSocialScreen = true 
                                    },
                                    text = { Text("Social") },
                                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) }
                                )
                            }
                        ) { padding ->
                            Box(modifier = Modifier.padding(padding)) {
                                TaskScreen(
                                    viewModel = taskViewModel,
                                    onLogout = { 
                                        Log.i(TAG, "Logout: Utilizatorul a ieșit din cont")
                                        isLoggedIn = false 
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun scheduleInactivityNotification() {
        Log.d(TAG, "scheduleInactivityNotification: Programare notificare peste 24 ore")
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(24, TimeUnit.HOURS)
            .addTag("inactivity_tag")
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            INACTIVITY_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }
}
