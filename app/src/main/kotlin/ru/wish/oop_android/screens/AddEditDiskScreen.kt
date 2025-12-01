package ru.wish.oop_android.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.navigation.NavController
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
    val disk = diskId?.toIntOrNull()?.let { id -> viewModel.getDiskById(id) }

    var name by rememberSaveable { mutableStateOf(disk?.name ?: "") }
    var capacity by rememberSaveable { mutableStateOf(disk?.capacityGB?.toString() ?: "") }
    var type by rememberSaveable { mutableStateOf(
        if (disk is ExternalHardDisk) DiskType.EXTERNAL
        else if (disk is InternalHardDisk) DiskType.INTERNAL
        else DiskType.EXTERNAL
    ) }
    var size by rememberSaveable { mutableStateOf((disk as? InternalHardDisk)?.size ?: "3.5\"") }
    var hasProtection by rememberSaveable { mutableStateOf((disk as? ExternalHardDisk)?.hasDropProtection ?: false) }

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
                if (disk == null) "Добавить диск" else "Редактировать диск",
                style = MaterialTheme.typography.headlineMedium
            )

            // Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Название") },
                modifier = Modifier.fillMaxWidth()
            )

            // Capacity
            OutlinedTextField(
                value = capacity,
                onValueChange = { capacity = it },
                label = { Text("Ёмкость (ГБ)") },
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
                            when (t) {
                                DiskType.EXTERNAL -> "Внешний"
                                DiskType.INTERNAL -> "Внутренний"
                            }
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
                            label = { Text("Размер") },
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
                        Text("Защита от падения")
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
                    Text("Отмена")
                }
                Button(
                    onClick = {
                        val cap = capacity.toIntOrNull() ?: return@Button
                        val newDisk: HardDisk = if (disk == null) {
                            when (type) {
                                DiskType.EXTERNAL -> ExternalHardDisk(name, cap, hasProtection)
                                DiskType.INTERNAL -> InternalHardDisk(name, cap, size)
                            }
                        } else {
                            when (type) {
                                DiskType.EXTERNAL -> ExternalHardDisk(name, cap, hasProtection, disk.id)
                                DiskType.INTERNAL -> InternalHardDisk(name, cap, size, disk.id)
                            }
                        }
                        if (disk == null) {
                            viewModel.addDisk(newDisk)
                        } else {
                            viewModel.updateDisk(newDisk)
                        }
                        navController.popBackStack()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Сохранить")
                }
            }
        }
    }
}
