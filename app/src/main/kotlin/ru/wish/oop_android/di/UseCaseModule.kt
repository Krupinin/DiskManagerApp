package ru.wish.oop_android.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.wish.oop_android.core.data.repository.DiskRepository
import ru.wish.oop_android.core.domain.usecase.*

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetDisksUseCase(repository: DiskRepository): GetDisksUseCase {
        return GetDisksUseCase(repository)
    }

    @Provides
    fun provideAddDiskUseCase(repository: DiskRepository): AddDiskUseCase {
        return AddDiskUseCase(repository)
    }

    @Provides
    fun provideUpdateDiskUseCase(repository: DiskRepository): UpdateDiskUseCase {
        return UpdateDiskUseCase(repository)
    }

    @Provides
    fun provideDeleteDiskUseCase(repository: DiskRepository): DeleteDiskUseCase {
        return DeleteDiskUseCase(repository)
    }

    @Provides
    fun provideGetDiskByIdUseCase(repository: DiskRepository): GetDiskByIdUseCase {
        return GetDiskByIdUseCase(repository)
    }
}
