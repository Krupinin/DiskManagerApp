package ru.wish.oop_android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.viewmodels.DiskViewModel

@Composable
fun DiskListScreen(
    navController: NavController,
    viewModel: DiskViewModel
) {
    val disks by viewModel.disks.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("add") }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Жёсткие диски", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(disks) { disk ->
                    DiskItem(
                        disk = disk,
                        onEdit = { navController.navigate("edit/${disk.id}") },
                        onDelete = { viewModel.deleteDisk(disk.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun DiskItem(disk: HardDisk, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(disk.name, style = MaterialTheme.typography.bodyLarge)
                Text("${disk.capacityGB} GB", style = MaterialTheme.typography.bodyMedium)
                Text(disk.getDescription(), style = MaterialTheme.typography.bodySmall)
            }
            Row {
                Button(onClick = onEdit) {
                    Text("Редактировать")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text("Удалить")
                }
            }
        }
    }
}
