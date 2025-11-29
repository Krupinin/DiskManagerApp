package ru.wish.oop_android.core.entities

import ru.wish.oop_android.core.interfaces.ProtectionFeature

class ExternalHardDisk(
    name: String,
    capacityGB: Int,
    override val hasDropProtection: Boolean,
    id: Int = generateId(),
    private val descriptionFormatter: ExternalHardDiskDescriptionFormatter = ExternalHardDiskDescriptionFormatter()
) : HardDisk(name, capacityGB, id), ProtectionFeature {

    override fun getDescription(): String {
        return descriptionFormatter.format(this)
    }
}
