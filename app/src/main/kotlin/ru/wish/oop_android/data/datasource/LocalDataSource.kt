package ru.wish.oop_android.data.datasource

import ru.wish.oop_android.data.dao.DiskDao
import ru.wish.oop_android.data.model.DiskEntity

class LocalDataSource(private val diskDao: DiskDao) {
    suspend fun getAllDisks(): List<DiskEntity> = diskDao.getAllDisks()

    suspend fun getDiskById(id: Int): DiskEntity? = diskDao.getDiskById(id)

    suspend fun insertDisk(disk: DiskEntity) = diskDao.insertDisk(disk)

    suspend fun updateDisk(disk: DiskEntity) = diskDao.updateDisk(disk)

    suspend fun deleteDisk(disk: DiskEntity) = diskDao.deleteDisk(disk)
}
