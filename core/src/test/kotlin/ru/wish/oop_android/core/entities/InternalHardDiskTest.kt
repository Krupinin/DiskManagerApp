package ru.wish.oop_android.core.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class InternalHardDiskTest {

    @Test
    fun `test getDescription for 2.5 inch disk`() {
        val disk = InternalHardDisk("Samsung", 500, "2.5\"")
        val expected = "Internal hard disk: Samsung, capacity: 500 GB, size: 2.5\""
        assertEquals(expected, disk.getDescription())
    }

    @Test
    fun `test getDescription for 3.5 inch disk`() {
        val disk = InternalHardDisk("WD", 1000, "3.5\"")
        val expected = "Internal hard disk: WD, capacity: 1000 GB, size: 3.5\""
        assertEquals(expected, disk.getDescription())
    }

    @Test
    fun `test InternalHardDisk with custom id`() {
        val disk = InternalHardDisk("Test", 750, "2.5\"", 99)
        assertEquals(99, disk.id)
        assertEquals("Test", disk.name)
        assertEquals(750, disk.capacityGB)
        assertEquals("2.5\"", disk.size)
    }
}
