package ru.wish.oop_android.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.wish.oop_android.data.dao.DiskDao
import ru.wish.oop_android.data.database.DiskDatabase
import ru.wish.oop_android.data.datasource.LocalDataSource
import ru.wish.oop_android.data.datasource.MockDataSource
import ru.wish.oop_android.data.repository.RoomDiskRepository
import ru.wish.oop_android.data.repository.MockDiskRepository
import ru.wish.oop_android.core.data.repository.DiskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDiskDatabase(@ApplicationContext context: Context): DiskDatabase {
        return Room.databaseBuilder(
            context,
            DiskDatabase::class.java,
            "disk_database"
        ).build()
    }

    @Provides
    fun provideDiskDao(database: DiskDatabase): DiskDao {
        return database.diskDao()
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(diskDao: DiskDao): LocalDataSource {
        return LocalDataSource(diskDao)
    }

    @Provides
    @Singleton
    fun provideMockDataSource(): MockDataSource {
        return MockDataSource()
    }

    @Provides
    @Singleton
    fun provideDiskRepository(localDataSource: LocalDataSource): DiskRepository {
        return RoomDiskRepository(localDataSource)
    }

//    @Provides
//    @Singleton
//    fun provideDiskRepository(mockDataSource: MockDataSource): DiskRepository {
//        return MockDiskRepository(mockDataSource)
//    }
}
