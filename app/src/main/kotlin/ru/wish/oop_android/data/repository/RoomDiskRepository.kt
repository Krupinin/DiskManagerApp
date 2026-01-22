package ru.wish.oop_android.data.repository

import ru.wish.oop_android.core.data.repository.DiskRepository
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk
import ru.wish.oop_android.data.datasource.LocalDataSource
import ru.wish.oop_android.data.model.DiskEntity

class RoomDiskRepository(private val localDataSource: LocalDataSource) : DiskRepository {

    override suspend fun getAllDisks(): List<HardDisk> {
        return localDataSource.getAllDisks().map { it.toDomain() }
    }

    override suspend fun getDiskById(id: Int): HardDisk? {
        return localDataSource.getDiskById(id)?.toDomain()
    }

    override suspend fun addDisk(disk: HardDisk) {
        localDataSource.insertDisk(disk.toEntity())
    }

    override suspend fun updateDisk(disk: HardDisk) {
        localDataSource.updateDisk(disk.toEntity())
    }

    override suspend fun deleteDisk(id: Int) {
        val entity = localDataSource.getDiskById(id) ?: return
        localDataSource.deleteDisk(entity)
    }
}

private fun DiskEntity.toDomain(): HardDisk {
    return when (type) {
        "external" -> ExternalHardDisk(name, capacityGB, hasDropProtection ?: false, id)
        "internal" -> InternalHardDisk(name, capacityGB, size ?: "3.5\"", id)
        else -> throw IllegalArgumentException("Unknown disk type: $type")
    }
}

private fun HardDisk.toEntity(): DiskEntity {
    return when (this) {
        is ExternalHardDisk -> DiskEntity(id, name, capacityGB, "external", hasDropProtection, null)
        is InternalHardDisk -> DiskEntity(id, name, capacityGB, "internal", null, size)
        else -> throw IllegalArgumentException("Unknown disk type")
    }
}
