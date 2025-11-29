package org.example

import org.example.application.DiskManagerApp
import org.example.infrastructure.input.ConsoleDataInput
import org.example.infrastructure.output.ConsoleDataOutput

// Точка входа в программу
fun main() {
    val app = DiskManagerApp(
        dataInput = ConsoleDataInput(),
        dataOutput = ConsoleDataOutput(),
    )
    app.run()
}


//S — Single Responsibility Principle (Принцип единственной ответственности): каждый класс должен иметь только одну зону ответственности.
//O — Open-Closed Principle (Принцип открытости/закрытости): классы должны быть открыты для расширения, но закрыты для модификации.
//L — Liskov Substitution Principle (Принцип подстановки Барбары Лисков): объекты типа-потомка должны быть взаимозаменяемы с объектами базового типа без нарушения работы программы.
//I — Interface Segregation Principle (Принцип разделения интерфейсов): клиенты не должны зависеть от методами интерфейса, которые они не используют.
//D — Dependency Inversion Principle (Принцип инверсии зависимостей): модули верхнего и нижнего уровня должны зависеть от абстракций, а не друг от друга.
