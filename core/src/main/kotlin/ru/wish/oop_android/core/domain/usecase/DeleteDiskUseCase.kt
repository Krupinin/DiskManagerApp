package ru.wish.oop_android.core.domain.usecase

import ru.wish.oop_android.core.data.repository.DiskRepository

class DeleteDiskUseCase(private val repository: DiskRepository) {
    suspend fun execute(diskId: Int) = repository.deleteDisk(diskId)
}
