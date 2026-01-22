package ru.wish.oop_android.core.data.repository

import ru.wish.oop_android.core.entities.HardDisk

interface DiskRepository {
    suspend fun getAllDisks(): List<HardDisk>
    suspend fun getDiskById(id: Int): HardDisk?
    suspend fun addDisk(disk: HardDisk)
    suspend fun updateDisk(disk: HardDisk)
    suspend fun deleteDisk(id: Int)
}
