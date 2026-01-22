package ru.wish.oop_android.core.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class ExternalHardDiskTest {

    @Test
    fun `test getDescription without protection`() {
        val disk = ExternalHardDisk("Seagate", 1000, false)
        val expected = "External hard disk: Seagate, capacity: 1000 GB"
        assertEquals(expected, disk.getDescription())
    }

    @Test
    fun `test getDescription with protection`() {
        val disk = ExternalHardDisk("WD", 2000, true)
        val expected = "External hard disk: WD, capacity: 2000 GB, drop protection"
        assertEquals(expected, disk.getDescription())
    }

    @Test
    fun `test ExternalHardDisk with custom id`() {
        val disk = ExternalHardDisk("Test", 500, true, 42)
        assertEquals(42, disk.id)
        assertEquals("Test", disk.name)
        assertEquals(500, disk.capacityGB)
        assertEquals(true, disk.hasDropProtection)
    }
}
