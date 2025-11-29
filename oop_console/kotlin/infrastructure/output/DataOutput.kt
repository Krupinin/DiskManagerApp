package org.example.infrastructure.output

import org.example.entities.HardDisk

// Интерфейс для вывода данных
interface DataOutput {
    fun displayHighCapacityDisks(disks: List<HardDisk>)
}