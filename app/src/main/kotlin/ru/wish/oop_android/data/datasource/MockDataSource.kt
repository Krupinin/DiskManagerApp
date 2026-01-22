package ru.wish.oop_android.data.datasource

import ru.wish.oop_android.data.model.DiskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockDataSource {
    private val _disks = MutableStateFlow<List<DiskEntity>>(emptyList())
    val disks: Flow<List<DiskEntity>> = _disks.asStateFlow()

    suspend fun getAllDisks(): List<DiskEntity> = _disks.value

    suspend fun getDiskById(id: Int): DiskEntity? = _disks.value.find { it.id == id }

    suspend fun insertDisk(disk: DiskEntity) {
        val newId = (_disks.value.maxOfOrNull { it.id } ?: 0) + 1
        val newDisk = disk.copy(id = newId)
        _disks.value += newDisk
    }

    suspend fun updateDisk(disk: DiskEntity) {
        _disks.value = _disks.value.map { if (it.id == disk.id) disk else it }
    }

    suspend fun deleteDisk(disk: DiskEntity) {
        _disks.value = _disks.value.filter { it.id != disk.id }
    }
}
