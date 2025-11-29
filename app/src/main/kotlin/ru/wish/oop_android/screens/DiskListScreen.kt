package ru.wish.oop_android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import ru.wish.oop_android.viewmodels.DiskViewModel

enum class DiskFilter {
    ALL,
    HIGH_CAPACITY,
    INTERNAL_ONLY,
    EXTERNAL_ONLY
}

fun applyFilter(disks: List<HardDisk>, filter: DiskFilter): List<HardDisk> {
    return when (filter) {
        DiskFilter.ALL -> disks
        DiskFilter.HIGH_CAPACITY -> disks.filter { it.isHighCapacity() }
        DiskFilter.INTERNAL_ONLY -> disks.filter { it is InternalHardDisk }
        DiskFilter.EXTERNAL_ONLY -> disks.filter { it is ExternalHardDisk }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiskListScreen(
    navController: NavController,
    viewModel: DiskViewModel
) {
    val disks by viewModel.disks.collectAsState()
    var currentFilter by remember { mutableStateOf(DiskFilter.ALL) }
    val filteredDisks = remember(disks, currentFilter) { applyFilter(disks, currentFilter) }

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

            // Filter selector
            var expanded by remember { mutableStateOf(false) }
            val filterOptions = mapOf(
                DiskFilter.ALL to "Все диски",
                DiskFilter.HIGH_CAPACITY to "Диски ёмкостью более 200 ГБ",
                DiskFilter.INTERNAL_ONLY to "Внутренние диски",
                DiskFilter.EXTERNAL_ONLY to "Внешние диски"
            )

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = filterOptions[currentFilter] ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Фильтр") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    filterOptions.forEach { (filter, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                currentFilter = filter
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics similar to console app
            val externalCount = disks.count { it is ExternalHardDisk }
            val internalCount = disks.count { it is InternalHardDisk }
            Text(
                "Всего дисков: ${disks.size}, Отобрано: ${filteredDisks.size}, " +
                "Внешних: $externalCount, Внутренних: $internalCount",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(filteredDisks) { disk ->
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
