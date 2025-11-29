package ru.wish.oop_android.core.entities

import ru.wish.oop_android.core.interfaces.StorageDevice

// Абстрактный базовый класс
abstract class HardDisk(
    override val name: String,
    override val capacityGB: Int,
    val id: Int = generateId()
) : StorageDevice {

    companion object {
        private var currentId = 0
        internal fun generateId() = ++currentId
    }

    abstract fun getDescription(): String

    // Общая логика проверки ёмкости
    fun isHighCapacity(): Boolean = capacityGB > 200
}
