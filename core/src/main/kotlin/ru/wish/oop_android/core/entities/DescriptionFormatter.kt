package ru.wish.oop_android.core.entities

interface DescriptionFormatter<T> {
    fun format(entity: T): String
}
