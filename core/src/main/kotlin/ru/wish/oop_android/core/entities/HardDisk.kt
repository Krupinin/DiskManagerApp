package ru.wish.oop_android.core.entities

import ru.wish.oop_android.core.interfaces.StorageDevice
import java.util.concurrent.atomic.AtomicInteger

// Абстрактный базовый класс
abstract class HardDisk(
    override val name: String,
    override val capacityGB: Int,
    val id: Int = generateId()
) : StorageDevice {

    companion object {
        private val currentId = AtomicInteger(0)
        internal fun generateId() = currentId.incrementAndGet()
    }

    abstract fun getDescription(): String

    // Общая логика проверки ёмкости
    fun isHighCapacity(): Boolean = capacityGB > 200
}
