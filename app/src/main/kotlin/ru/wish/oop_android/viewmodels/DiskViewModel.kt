package ru.wish.oop_android.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import ru.wish.oop_android.core.domain.usecase.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

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

@HiltViewModel
class DiskViewModel @Inject constructor(
    private val getDisksUseCase: GetDisksUseCase,
    private val addDiskUseCase: AddDiskUseCase,
    private val updateDiskUseCase: UpdateDiskUseCase,
    private val deleteDiskUseCase: DeleteDiskUseCase,
    private val getDiskByIdUseCase: GetDiskByIdUseCase
) : ViewModel() {

    private val _disks = MutableStateFlow<List<HardDisk>>(emptyList())
    val disks: StateFlow<List<HardDisk>> = _disks

    private val _currentFilter = MutableStateFlow(DiskFilter.ALL)
    val currentFilter: StateFlow<DiskFilter> = _currentFilter

    val filteredDisks = combine(disks, _currentFilter) { disks, filter ->
        applyFilter(disks, filter)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats = combine(disks, filteredDisks) { disks, filtered ->
        DiskStats(
            total = disks.size,
            externalCount = disks.count { it is ExternalHardDisk },
            internalCount = disks.count { it is InternalHardDisk },
            filteredCount = filtered.size
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DiskStats(0,0,0,0))

    init {
        loadDisks()
    }

    private fun loadDisks() {
        viewModelScope.launch {
            try {
                val disks = getDisksUseCase.execute()
                _disks.value = disks
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun setFilter(filter: DiskFilter) {
        _currentFilter.value = filter
    }

    fun addDisk(disk: HardDisk) {
        viewModelScope.launch {
            try {
                addDiskUseCase.execute(disk)
                loadDisks()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun updateDisk(updatedDisk: HardDisk) {
        viewModelScope.launch {
            try {
                updateDiskUseCase.execute(updatedDisk)
                loadDisks()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun deleteDisk(diskId: Int) {
        viewModelScope.launch {
            try {
                deleteDiskUseCase.execute(diskId)
                loadDisks()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    suspend fun getDiskById(diskId: Int): HardDisk? {
        return try {
            getDiskByIdUseCase.execute(diskId)
        } catch (e: Exception) {
            null
        }
    }
}
