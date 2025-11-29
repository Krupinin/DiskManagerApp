package org.example.infrastructure.input

import org.example.entities.ExternalHardDisk
import org.example.entities.HardDisk
import org.example.entities.InternalHardDisk

// Реализация ввода данных
class ConsoleDataInput : DataInput {
    override fun inputHardDisks(): List<HardDisk> {
        val disks = mutableListOf<HardDisk>()

        while (true) {
            println("\n=== Ввод данных о жёстком диске ===")
            println("1. Внешний диск")
            println("2. Внутренний диск")
            println("0. Завершить ввод")
            print("Выберите тип диска: ")

            when (readlnOrNull()?.toIntOrNull()) {
                1 -> disks.add(inputExternalDisk())
                2 -> disks.add(inputInternalDisk())
                0 -> break
                else -> println("Неверный выбор. Попробуйте снова.")
            }
        }

        return disks
    }

    private fun inputExternalDisk(): ExternalHardDisk {
        print("Введите название диска: ")
        val name = readlnOrNull()?.takeIf { it.isNotBlank() } ?: "Без названия"

        print("Введите ёмкость (ГБ): ")
        val capacity = readlnOrNull()?.toIntOrNull() ?: 0

        print("Есть ли защита от падения? (y/n): ")
        val hasProtection = readlnOrNull()?.equals("y", ignoreCase = true) ?: false

        return ExternalHardDisk(name, capacity, hasProtection)
    }

    private fun inputInternalDisk(): InternalHardDisk {
        print("Введите название диска: ")
        val name = readlnOrNull()?.takeIf { it.isNotBlank() } ?: "Без названия"

        print("Введите ёмкость (ГБ): ")
        val capacity = readlnOrNull()?.toIntOrNull() ?: 0

        print("Введите размер (2.5 или 3.5): ")
        val sizeInput = readlnOrNull() ?: "3.5"
        val size = if (sizeInput == "2.5") "2.5\"" else "3.5\""

        return InternalHardDisk(name, capacity, size)
    }
}