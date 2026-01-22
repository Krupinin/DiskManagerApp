package ru.wish.oop_android.data.datasource

import ru.wish.oop_android.data.model.DiskEntity
import kotlinx.coroutines.flow.MutableStateFlow

class MockDataSource {
    private val _disks = MutableStateFlow<List<DiskEntity>>(emptyList())

    fun getAllDisks(): List<DiskEntity> = _disks.value

    fun getDiskById(id: Int): DiskEntity? = _disks.value.find { it.id == id }

    fun insertDisk(disk: DiskEntity) {
        val newId = (_disks.value.maxOfOrNull { it.id } ?: 0) + 1
        val newDisk = disk.copy(id = newId)
        _disks.value += newDisk
    }

    fun updateDisk(disk: DiskEntity) {
        _disks.value = _disks.value.map { if (it.id == disk.id) disk else it }
    }

    fun deleteDisk(disk: DiskEntity) {
        _disks.value = _disks.value.filter { it.id != disk.id }
    }
}
