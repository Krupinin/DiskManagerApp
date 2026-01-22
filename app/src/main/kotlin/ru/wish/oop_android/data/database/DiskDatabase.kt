package ru.wish.oop_android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.wish.oop_android.data.dao.DiskDao
import ru.wish.oop_android.data.model.DiskEntity

@Database(entities = [DiskEntity::class], version = 1, exportSchema = false)
abstract class DiskDatabase : RoomDatabase() {
    abstract fun diskDao(): DiskDao
}
