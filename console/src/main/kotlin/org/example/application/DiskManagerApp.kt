package org.example.application

import org.example.infrastructure.input.DataInput
import org.example.infrastructure.output.DataOutput
import ru.wish.oop_android.core.entities.HardDisk

// Главный класс приложения (модуль верхнего уровня)
class DiskManagerApp(
    private val dataInput: DataInput,
    private val dataOutput: DataOutput
) {
    fun run() {
        println("=== Программа управления жёсткими дисками ===")

        // Ввод данных
        val disks = dataInput.inputHardDisks()

        // Вывод результатов
        dataOutput.displayHighCapacityDisks(disks)
    }
}
