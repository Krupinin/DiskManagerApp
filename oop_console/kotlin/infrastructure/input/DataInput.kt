package org.example.infrastructure.input

import org.example.entities.HardDisk

// Интерфейс для ввода данных
interface DataInput {
    fun inputHardDisks(): List<HardDisk>
}