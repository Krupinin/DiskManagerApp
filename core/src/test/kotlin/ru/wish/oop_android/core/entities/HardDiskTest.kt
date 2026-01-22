package ru.wish.oop_android.core.entities

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HardDiskTest {

    @Test
    fun `test isHighCapacity returns true for capacity greater than 200`() {
        val disk = object : HardDisk("Test", 250) {
            override fun getDescription(): String = "Test disk"
        }
        assertTrue(disk.isHighCapacity())
    }

    @Test
    fun `test isHighCapacity returns false for capacity less than or equal to 200`() {
        val disk = object : HardDisk("Test", 200) {
            override fun getDescription(): String = "Test disk"
        }
        assertFalse(disk.isHighCapacity())
    }

    @Test
    fun `test generateId creates unique ids`() {
        val disk1 = object : HardDisk("Test1", 100) {
            override fun getDescription(): String = "Test disk 1"
        }
        val disk2 = object : HardDisk("Test2", 200) {
            override fun getDescription(): String = "Test disk 2"
        }
        assertTrue(disk1.id != disk2.id)
    }
}
