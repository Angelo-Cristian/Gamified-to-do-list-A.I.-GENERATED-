package com.example.proiectkotlin.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proiectkotlin.data.Task
import com.example.proiectkotlin.data.TaskStatus
import com.example.proiectkotlin.ui.viewmodel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "TaskScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(viewModel: TaskViewModel, onLogout: () -> Unit) {
    val tasks by viewModel.allTasks.collectAsState()
    val user by viewModel.user.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d(TAG, "TaskScreen: Lansare buclă de refresh task-uri")
        while (true) {
            viewModel.refreshTaskStates()
            kotlinx.coroutines.delay(10000)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Task-urile mele", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        user?.let {
                            Text(
                                "Nivel ${it.level} • ${it.totalXp % 1000}/1000 XP",
                                fontSize = 12.sp,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Log.i(TAG, "TaskScreen: Click pe butonul de Logout")
                        onLogout()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { 
                Log.d(TAG, "TaskScreen: Deschidere dialog adăugare task")
                showAddDialog = true 
            }) {
                Icon(Icons.Default.Add, contentDescription = "Adaugă Task")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            user?.let {
                LinearProgressIndicator(
                    progress = { (it.totalXp % 1000) / 1000f },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }

            if (tasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Niciun task momentan. Apasă + pentru a adăuga unul!")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(tasks) { task ->
                        TaskItem(task = task, onComplete = { 
                            Log.d(TAG, "TaskScreen: Cerere finalizare task: ${task.title}")
                            viewModel.completeTask(task) 
                        })
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddTaskDialog(
            onDismiss = { 
                Log.d(TAG, "TaskScreen: Închidere dialog adăugare task")
                showAddDialog = false 
            },
            onConfirm = { title, desc, delay, duration ->
                Log.i(TAG, "TaskScreen: Confirmare adăugare task nou: $title")
                viewModel.addTask(title, desc, delay, duration)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun TaskItem(task: Task, onComplete: () -> Unit) {
    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (task.status) {
                TaskStatus.COMPLETED -> Color.LightGray.copy(alpha = 0.3f)
                TaskStatus.ACTIVE -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                TaskStatus.PENDING -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (task.status == TaskStatus.COMPLETED) 
                        androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Text(text = task.description, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Activare la: ${dateFormat.format(Date(task.startTime))}",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                StatusChip(status = task.status)
                if (task.status == TaskStatus.ACTIVE) {
                    IconButton(onClick = onComplete) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Completează",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: TaskStatus) {
    val (text, color) = when (status) {
        TaskStatus.PENDING -> "În așteptare" to Color.Gray
        TaskStatus.ACTIVE -> "ACTIV" to Color(0xFFE91E63)
        TaskStatus.COMPLETED -> "Finalizat" to Color(0xFF4CAF50)
    }
    Surface(
        color = color.copy(alpha = 0.2f),
        shape = MaterialTheme.shapes.small,
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 10.sp,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, Long) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }
    var delayMinutes by remember { mutableStateOf("1") }
    var durationMinutes by remember { mutableStateOf("10") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Adaugă Task Nou") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Titlu") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descriere") })
                OutlinedTextField(
                    value = delayMinutes,
                    onValueChange = { delayMinutes = it },
                    label = { Text("Peste câte minute se activează?") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )
                OutlinedTextField(
                    value = durationMinutes,
                    onValueChange = { durationMinutes = it },
                    label = { Text("Durată task (minute)") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = {
                val delayMs = (delayMinutes.toLongOrNull() ?: 0L) * 60000L
                val durationMs = (durationMinutes.toLongOrNull() ?: 10L) * 60000L
                onConfirm(title, desc, delayMs, durationMs)
            }) {
                Text("Creează")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anulează") }
        }
    )
}
