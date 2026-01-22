package ru.wish.oop_android.core.domain.usecase

import ru.wish.oop_android.core.data.repository.DiskRepository
import ru.wish.oop_android.core.entities.HardDisk

class UpdateDiskUseCase(private val repository: DiskRepository) {
    suspend fun execute(disk: HardDisk) = repository.updateDisk(disk)
}
