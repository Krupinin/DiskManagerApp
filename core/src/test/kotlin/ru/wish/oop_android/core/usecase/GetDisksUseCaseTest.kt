package ru.wish.oop_android.core.usecase

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.wish.oop_android.core.data.repository.DiskRepository
import ru.wish.oop_android.core.domain.usecase.GetDisksUseCase
import ru.wish.oop_android.core.entities.ExternalHardDisk
import ru.wish.oop_android.core.entities.HardDisk
import ru.wish.oop_android.core.entities.InternalHardDisk

class GetDisksUseCaseTest {

    private val mockRepository = object : DiskRepository {
        override suspend fun getAllDisks(): List<HardDisk> {
            return listOf(
                ExternalHardDisk("Seagate", 1000, true),
                InternalHardDisk("WD", 500, "2.5\"")
            )
        }

        override suspend fun getDiskById(id: Int): HardDisk? = null
        override suspend fun addDisk(disk: HardDisk) {}
        override suspend fun updateDisk(disk: HardDisk) {}
        override suspend fun deleteDisk(id: Int) {}
    }

    private val useCase = GetDisksUseCase(mockRepository)

    @Test
    fun `test execute returns list of disks from repository`() {
        val result = useCase.execute()
        assertEquals(2, result.size)
        assertTrue(result[0] is ExternalHardDisk)
        assertTrue(result[1] is InternalHardDisk)
    }
}
