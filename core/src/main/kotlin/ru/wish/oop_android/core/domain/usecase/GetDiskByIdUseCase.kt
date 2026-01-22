package ru.wish.oop_android.core.domain.usecase

import ru.wish.oop_android.core.data.repository.DiskRepository
import ru.wish.oop_android.core.entities.HardDisk

class GetDiskByIdUseCase(private val repository: DiskRepository) {
    suspend fun execute(diskId: Int): HardDisk? = repository.getDiskById(diskId)
}
