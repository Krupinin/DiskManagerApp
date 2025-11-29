package ru.wish.oop_android.viewmodels

import androidx.lifecycle.ViewModel
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DiskViewModel : ViewModel() {

    private val _disks = MutableStateFlow<List<HardDisk>>(emptyList())
    val disks: StateFlow<List<HardDisk>> = _disks.asStateFlow()

    fun addDisk(disk: HardDisk) {
        _disks.value = _disks.value + disk
    }

    fun updateDisk(updatedDisk: HardDisk) {
        _disks.value = _disks.value.map { if (it.id == updatedDisk.id) updatedDisk else it }
    }

    fun deleteDisk(diskId: Int) {
        _disks.value = _disks.value.filter { it.id != diskId }
    }

    fun getDiskById(diskId: Int): HardDisk? = _disks.value.find { it.id == diskId }
}
