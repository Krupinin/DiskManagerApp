package ru.wish.oop_android.core.entities

import ru.wish.oop_android.core.interfaces.SizeFeature

class InternalHardDisk(
    name: String,
    capacityGB: Int,
    override val size: String,
    id: Int = generateId(),
    private val descriptionFormatter: InternalHardDiskDescriptionFormatter = InternalHardDiskDescriptionFormatter()
) : HardDisk(name, capacityGB, id), SizeFeature {

    init {
        require(size == "2.5\"" || size == "3.5\"") { "Размер должен быть 2.5\" или 3.5\"" }
    }

    override fun getDescription(): String {
        return descriptionFormatter.format(this)
    }
}
