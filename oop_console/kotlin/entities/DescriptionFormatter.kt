package org.example.entities

interface DescriptionFormatter<T> {
    fun format(entity: T): String
}
