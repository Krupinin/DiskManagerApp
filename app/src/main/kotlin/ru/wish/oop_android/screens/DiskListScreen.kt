package ru.wish.oop_android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import ru.wish.oop_android.R
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import ru.wish.oop_android.viewmodels.DiskViewModel
import ru.wish.oop_android.viewmodels.DiskFilter
import ru.wish.oop_android.viewmodels.DiskStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiskListScreen(
    navController: NavController,
    viewModel: DiskViewModel
) {
    val currentFilter by viewModel.currentFilter.collectAsState()
    val filteredDisks by viewModel.filteredDisks.collectAsState(emptyList())
    val stats by viewModel.stats.collectAsState(DiskStats(0, 0, 0, 0))

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
            Text(stringResource(R.string.hard_drives_title), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Filter selector
            var expanded by remember { mutableStateOf(false) }
            val currentFilterRes = when (currentFilter) {
                DiskFilter.ALL -> R.string.all_disks
                DiskFilter.HIGH_CAPACITY -> R.string.high_capacity_disks
                DiskFilter.INTERNAL_ONLY -> R.string.internal_disks
                DiskFilter.EXTERNAL_ONLY -> R.string.external_disks
            }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = stringResource(currentFilterRes),
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.filter)) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    listOf(DiskFilter.ALL, DiskFilter.HIGH_CAPACITY, DiskFilter.INTERNAL_ONLY, DiskFilter.EXTERNAL_ONLY).forEach { filter ->
                        val filterRes = when (filter) {
                            DiskFilter.ALL -> R.string.all_disks
                            DiskFilter.HIGH_CAPACITY -> R.string.high_capacity_disks
                            DiskFilter.INTERNAL_ONLY -> R.string.internal_disks
                            DiskFilter.EXTERNAL_ONLY -> R.string.external_disks
                        }
                        DropdownMenuItem(
                            text = { Text(stringResource(filterRes)) },
                            onClick = {
                                viewModel.setFilter(filter)
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Statistics
            Text(
                stringResource(R.string.stats_format, stats.total, stats.filteredCount, stats.externalCount, stats.internalCount),
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
                    Text(stringResource(R.string.edit))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onDelete, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}
