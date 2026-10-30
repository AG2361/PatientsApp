package model; // пакет для хранения класса модели

/**
 * Пациент: класс с тремя полями. Имя, год рождения, номер мед. карты
 */
public class Patient {

    private final String name;       // имя пациента
    private final int birthYear;     // год рождения
    private final String cardNumber; // номер медицинской карты

    // конструктор: сохранение переданных значений в поля объекта
    public Patient(String name, int birthYear, String cardNumber) {
        this.name = name;                 // запись имени
        this.birthYear = birthYear;       // запись года рождения
        this.cardNumber = cardNumber;     // запись номера карты
    }

    // для получения имени
    public String getName() {
        return name; // возврат имени
    }

    // для получения года рождения
    public int getBirthYear() {
        return birthYear; // возврат года рождения
    }

    // для получения номера карты
    public String getCardNumber() {
        return cardNumber; // возврат номера карты
    }

    // строковое представление для вывода в консоль
    @Override
    public String toString() {
        return "Пациент: " + name + " | Год рождения: " + birthYear + " | Карта №: " + cardNumber; // готовая строка
    }
}