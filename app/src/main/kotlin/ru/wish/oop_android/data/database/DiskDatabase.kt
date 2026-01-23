package ru.wish.oop_android.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.wish.oop_android.data.dao.DiskDao
import ru.wish.oop_android.data.model.DiskEntity

@Database(entities = [DiskEntity::class], version = 1, exportSchema = false) // entities: Массив классов-сущностей, которые будут представлены как таблицы в базе данных. version: Версия базы данных, равная 1. При изменениях в схеме версия должна увеличиваться для миграций. exportSchema: Установлено в false, чтобы избежать экспорта схемы базы данных в файл (полезно для production, но отключено для простоты).
abstract class DiskDatabase : RoomDatabase() {
    abstract fun diskDao(): DiskDao
}
