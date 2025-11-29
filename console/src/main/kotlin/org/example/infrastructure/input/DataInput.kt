package org.example.infrastructure.input

import ru.wish.oop_android.core.entities.HardDisk

// Интерфейс для ввода данных
interface DataInput {
    fun inputHardDisks(): List<HardDisk>
}
