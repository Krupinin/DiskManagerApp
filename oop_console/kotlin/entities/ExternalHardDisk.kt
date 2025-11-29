package org.example.entities

import org.example.interfaces.ProtectionFeature

class ExternalHardDisk(
    name: String,
    capacityGB: Int,
    override val hasDropProtection: Boolean,
    private val descriptionFormatter: ExternalHardDiskDescriptionFormatter = ExternalHardDiskDescriptionFormatter()
) : HardDisk(name, capacityGB), ProtectionFeature {

    override fun getDescription(): String {
        return descriptionFormatter.format(this)
    }
}
