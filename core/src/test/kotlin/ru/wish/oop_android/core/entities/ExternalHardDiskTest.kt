package ru.wish.oop_android.core.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class ExternalHardDiskTest {

    @Test
    fun `test ExternalHardDisk with custom id`() {
        val disk = ExternalHardDisk("Test", 500, true, 42)
        assertEquals(42, disk.id)
        assertEquals("Test", disk.name)
        assertEquals(500, disk.capacityGB)
        assertEquals(true, disk.hasDropProtection)
    }
}
