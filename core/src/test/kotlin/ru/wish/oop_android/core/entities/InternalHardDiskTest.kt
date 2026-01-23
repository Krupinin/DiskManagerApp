package ru.wish.oop_android.core.entities

import org.junit.Assert.assertEquals
import org.junit.Test

class InternalHardDiskTest {

    @Test
    fun `test InternalHardDisk with custom id`() {
        val disk = InternalHardDisk("Test", 750, "2.5\"", 99)
        assertEquals(99, disk.id)
        assertEquals("Test", disk.name)
        assertEquals(750, disk.capacityGB)
        assertEquals("2.5\"", disk.size)
    }
}
