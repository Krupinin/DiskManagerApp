package org.example.entities

class InternalHardDiskDescriptionFormatter : DescriptionFormatter<InternalHardDisk> {
    override fun format(entity: InternalHardDisk): String {
        return "Внутренний диск '${entity.name}' (${entity.capacityGB} ГБ) размер ${entity.size}"
    }
}
