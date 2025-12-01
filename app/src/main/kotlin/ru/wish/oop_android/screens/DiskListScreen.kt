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
            Text(stringResource(R.string.hard_drives_title), style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Filter selector
            var expanded by remember { mutableStateOf(false) }
            val currentFilterRes = remember(currentFilter) {
                when (currentFilter) {
                    DiskFilter.ALL -> R.string.all_disks
                    DiskFilter.HIGH_CAPACITY -> R.string.high_capacity_disks
                    DiskFilter.INTERNAL_ONLY -> R.string.internal_disks
                    DiskFilter.EXTERNAL_ONLY -> R.string.external_disks
                }
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
                stringResource(R.string.stats_format, disks.size, filteredDisks.size, externalCount, internalCount),
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
                Text(
                    if (disk is ExternalHardDisk) {
                        stringResource(
                            R.string.external_disk_description,
                            disk.name,
                            disk.capacityGB,
                            if (disk.hasDropProtection) stringResource(R.string.with_drop_protection) else stringResource(R.string.without_drop_protection)
                        )
                    } else {
                        stringResource(
                            R.string.internal_disk_description,
                            disk.name,
                            disk.capacityGB,
                            (disk as InternalHardDisk).size
                        )
                    },
                    style = MaterialTheme.typography.bodySmall
                )
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
