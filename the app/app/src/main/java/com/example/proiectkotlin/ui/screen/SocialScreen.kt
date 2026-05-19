package com.example.proiectkotlin.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proiectkotlin.ui.viewmodel.SocialViewModel

private const val TAG = "SocialScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SocialScreen(
    viewModel: SocialViewModel,
    currentUserName: String,
    onBack: () -> Unit
) {
    val pendingRequests by viewModel.pendingRequests.collectAsState()
    val friendsList by viewModel.friendsList.collectAsState()
    val message by viewModel.socialMessage.collectAsState()
    
    var inviteName by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(currentUserName) {
        if (currentUserName.isNotBlank()) {
            Log.d(TAG, "SocialScreen: Încărcare date pentru $currentUserName")
            viewModel.loadSocialData(currentUserName)
        }
    }

    LaunchedEffect(message) {
        message?.let {
            Log.d(TAG, "SocialScreen: Afișare Snackbar: $it")
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Social & Prieteni", fontSize = 18.sp)
                        Text("Logat ca: $currentUserName", fontSize = 12.sp, color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f))
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        Log.d(TAG, "SocialScreen: Refresh manual apăsat")
                        viewModel.loadSocialData(currentUserName) 
                    }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Trimite Invitație", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inviteName,
                        onValueChange = { inviteName = it },
                        label = { Text("Nume utilizator") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { 
                            if (inviteName.isNotBlank()) {
                                Log.i(TAG, "SocialScreen: Trimitere invitație către $inviteName")
                                viewModel.sendInvitation(currentUserName, inviteName.trim())
                                inviteName = ""
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = Color.White)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Trimite")
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Cereri în așteptare (${pendingRequests.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (pendingRequests.isEmpty()) {
                item { Text("Nu ai cereri noi.", style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
            } else {
                items(pendingRequests) { requester ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(requester, modifier = Modifier.padding(start = 8.dp))
                            Row {
                                IconButton(onClick = { 
                                    Log.i(TAG, "SocialScreen: Acceptare cerere de la $requester")
                                    viewModel.respondToRequest(currentUserName, requester, true) 
                                }) {
                                    Icon(Icons.Default.Check, contentDescription = "Accept", tint = Color(0xFF4CAF50))
                                }
                                IconButton(onClick = { 
                                    Log.i(TAG, "SocialScreen: Refuzare cerere de la $requester")
                                    viewModel.respondToRequest(currentUserName, requester, false) 
                                }) {
                                    Icon(Icons.Default.Close, contentDescription = "Refuz", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }

            item {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text("Prietenii mei (${friendsList.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }

            if (friendsList.isEmpty()) {
                item { Text("Lista este goală. Adaugă prieteni pentru a-i vedea aici.", style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
            } else {
                items(friendsList) { friend ->
                    ListItem(
                        headlineContent = { Text(friend) },
                        leadingContent = { Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = {
                    Log.d(TAG, "SocialScreen: Revenire la task-uri")
                    onBack()
                }, modifier = Modifier.fillMaxWidth()) {
                    Text("Înapoi la Task-uri")
                }
            }
        }
    }
}
