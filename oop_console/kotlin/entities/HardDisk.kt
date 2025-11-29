package org.example.entities

import org.example.interfaces.StorageDevice

// Абстрактный базовый класс
abstract class HardDisk(
    override val name: String,
    override val capacityGB: Int
) : StorageDevice {

    abstract fun getDescription(): String

    // Общая логика проверки ёмкости
    fun isHighCapacity(): Boolean = capacityGB > 200
}