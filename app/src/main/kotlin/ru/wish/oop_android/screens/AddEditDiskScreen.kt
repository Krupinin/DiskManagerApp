package ru.wish.oop_android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

import androidx.navigation.NavController
import ru.wish.oop_android.R
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import ru.wish.oop_android.viewmodels.DiskViewModel

enum class DiskType { EXTERNAL, INTERNAL }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditDiskScreen(
    navController: NavController,
    diskId: String? = null,
    viewModel: DiskViewModel
) {
    val disk = remember { mutableStateOf<HardDisk?>(null) }

    LaunchedEffect(diskId) {
        disk.value = diskId?.toIntOrNull()?.let { id -> viewModel.getDiskById(id) }
    }

    var name by rememberSaveable { mutableStateOf("") }
    var capacity by rememberSaveable { mutableStateOf("") }
    var type by rememberSaveable { mutableStateOf(DiskType.EXTERNAL) }
    var size by rememberSaveable { mutableStateOf("3.5\"") }
    var hasProtection by rememberSaveable { mutableStateOf(false) }

    // Update fields when disk loads
    LaunchedEffect(disk.value) {
        disk.value?.let { d ->
            name = d.name
            capacity = d.capacityGB.toString()
            type = when (d) {
                is ExternalHardDisk -> DiskType.EXTERNAL
                is InternalHardDisk -> DiskType.INTERNAL
                else -> DiskType.EXTERNAL
            }
            size = (d as? InternalHardDisk)?.size ?: "3.5\""
            hasProtection = (d as? ExternalHardDisk)?.hasDropProtection ?: false
        }
    }

    Scaffold(
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(if (disk == null) R.string.add_disk_title else R.string.edit_disk_title),
                style = MaterialTheme.typography.headlineMedium
            )

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.name)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Capacity
            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text(stringResource(R.string.capacity_gb)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Type
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DiskType.entries.forEach { t ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == t,
                            onClick = { type = t }
                        )
                        Text(
                            stringResource(when (t) {
                                DiskType.EXTERNAL -> R.string.external
                                DiskType.INTERNAL -> R.string.internal
                            })
                        )
                    }
                }
            }

            // Additional fields
            when (type) {
                DiskType.INTERNAL -> {
                    val sizes = listOf("2.5\"", "3.5\"")
                    var expanded by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = size,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text(stringResource(R.string.size)) },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            sizes.forEach { s ->
                                DropdownMenuItem(
                                    text = { Text(s) },
                                    onClick = {
                                        size = s
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                DiskType.EXTERNAL -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = hasProtection,
                            onCheckedChange = { hasProtection = it }
                        )
                        Text(stringResource(R.string.drop_protection))
                    }
                }
            }

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    onClick = {
                        val cap = capacity.toIntOrNull() ?: return@Button
                        val newDisk: HardDisk = if (disk.value == null) {
                            when (type) {
                                DiskType.EXTERNAL -> ExternalHardDisk(name, cap, hasProtection)
                                DiskType.INTERNAL -> InternalHardDisk(name, cap, size)
                            }
                        } else {
                            when (type) {
                                DiskType.EXTERNAL -> ExternalHardDisk(name, cap, hasProtection, disk.value!!.id)
                                DiskType.INTERNAL -> InternalHardDisk(name, cap, size, disk.value!!.id)
                            }
                        }
                        if (disk.value == null) {
                            viewModel.addDisk(newDisk)
                        } else {
                            viewModel.updateDisk(newDisk)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.save))
                }
            }
        }
    }
}
