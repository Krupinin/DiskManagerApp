package org.example.infrastructure.output

import org.example.entities.ExternalHardDisk
import org.example.entities.HardDisk
import org.example.entities.InternalHardDisk

// Реализация вывода данных
class ConsoleDataOutput : DataOutput {

    fun displayFilteredDisks(
        disks: List<HardDisk>,
        filter: (HardDisk) -> Boolean,
        title: String = "Отфильтрованные диски"
    ) {
        val filteredDisks = disks.filter(filter)

        println("\n=== $title ===")
        if (filteredDisks.isEmpty()) {
            println("Диски не найдены")
        } else {
            filteredDisks.forEach { disk ->
                println("- ${disk.getDescription()}")
            }
        }

        // Общая статистика
        println("\n=== Статистика ===")
        println("Всего дисков: ${disks.size}")
        println("Отобрано дисков: ${filteredDisks.size}")

        val externalCount = disks.count { it is ExternalHardDisk }
        val internalCount = disks.count { it is InternalHardDisk }
        println("Внешних дисков: $externalCount")
        println("Внутренних дисков: $internalCount")
    }

    // Для совместимости — старый метод, использующий стандартный фильтр
    override fun displayHighCapacityDisks(disks: List<HardDisk>) {
        displayFilteredDisks(
            disks,
            filter = { it.isHighCapacity() },
            title = "Диски ёмкостью более 200 ГБ"
        )
    }
}
