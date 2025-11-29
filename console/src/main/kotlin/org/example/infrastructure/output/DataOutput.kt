package org.example.infrastructure.output

import ru.wish.oop_android.core.entities.HardDisk

// Интерфейс для вывода данных
interface DataOutput {
    fun displayHighCapacityDisks(disks: List<HardDisk>)
}
