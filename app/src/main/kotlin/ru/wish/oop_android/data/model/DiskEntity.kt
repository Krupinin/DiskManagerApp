package ru.wish.oop_android.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "disks")
data class DiskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val capacityGB: Int,
    val type: String, // "external" or "internal"
    val hasDropProtection: Boolean? = null, // only for external
    val size: String? = null // only for internal
)
