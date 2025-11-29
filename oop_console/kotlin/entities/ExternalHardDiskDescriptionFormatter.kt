package org.example.entities

class ExternalHardDiskDescriptionFormatter : DescriptionFormatter<ExternalHardDisk> {
    override fun format(entity: ExternalHardDisk): String {
        val protectionStatus = if (entity.hasDropProtection) "с защитой от падения" else "без защиты от падения"
        return "Внешний диск '${entity.name}' (${entity.capacityGB} ГБ) $protectionStatus"
    }
}
