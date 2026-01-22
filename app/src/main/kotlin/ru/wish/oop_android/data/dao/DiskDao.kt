package ru.wish.oop_android.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import ru.wish.oop_android.data.model.DiskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DiskDao {
    @Query("SELECT * FROM disks")
    suspend fun getAllDisks(): List<DiskEntity>

    @Query("SELECT * FROM disks WHERE id = :id")
    suspend fun getDiskById(id: Int): DiskEntity?

    @Insert
    suspend fun insertDisk(disk: DiskEntity)

    @Update
    suspend fun updateDisk(disk: DiskEntity)

    @Delete
    suspend fun deleteDisk(disk: DiskEntity)
}
