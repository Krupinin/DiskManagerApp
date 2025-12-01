package ru.wish.oop_android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

enum class DiskFilter {
    ALL,
    HIGH_CAPACITY,
    INTERNAL_ONLY,
    EXTERNAL_ONLY
}

data class DiskStats(
    val total: Int,
    val externalCount: Int,
    val internalCount: Int,
    val filteredCount: Int = total // default to total if not filtered
)

fun applyFilter(disks: List<HardDisk>, filter: DiskFilter): List<HardDisk> {
    return when (filter) {
        DiskFilter.ALL -> disks
        DiskFilter.HIGH_CAPACITY -> disks.filter { it.isHighCapacity() }
        DiskFilter.INTERNAL_ONLY -> disks.filter { it is InternalHardDisk }
        DiskFilter.EXTERNAL_ONLY -> disks.filter { it is ExternalHardDisk }
    }
}

class DiskViewModel : ViewModel() {

    private val _disks = MutableStateFlow<List<HardDisk>>(emptyList())
    val disks: StateFlow<List<HardDisk>> = _disks

    private val _currentFilter = MutableStateFlow(DiskFilter.ALL)
    val currentFilter: StateFlow<DiskFilter> = _currentFilter

    val filteredDisks = combine(_disks, _currentFilter) { disks, filter ->
        applyFilter(disks, filter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats = combine(_disks, filteredDisks) { disks, filtered ->
        DiskStats(
            total = disks.size,
            externalCount = disks.count { it is ExternalHardDisk },
            internalCount = disks.count { it is InternalHardDisk },
            filteredCount = filtered.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DiskStats(0,0,0,0))

    fun setFilter(filter: DiskFilter) {
        _currentFilter.value = filter
    }


    fun addDisk(disk: HardDisk) {
        _disks.value += disk
    }

    fun updateDisk(updatedDisk: HardDisk) {
        _disks.value = _disks.value.map { if (it.id == updatedDisk.id) updatedDisk else it }
    }

    fun deleteDisk(diskId: Int) {
        _disks.value = _disks.value.filter { it.id != diskId }
    }

    fun getDiskById(diskId: Int): HardDisk? = _disks.value.find { it.id == diskId }
}
