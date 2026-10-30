package app; // пакет точки входа

import ui.ConsoleMenu; // класс меню

/**
 * точка входа в приложение.
 */
public class Main {

    // метод начала выполнения программы
    public static void main(String[] args) {
        ConsoleMenu menu = new ConsoleMenu(); // создание меню
        menu.run(); // запуск главного цикла
    }
}