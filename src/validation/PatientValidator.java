package validation; // пакет валидации

import java.time.Year; // класс для получения текущего года

/**
 * проверка корректности данных пациента.
 */
public final class PatientValidator {

    private static final int MIN_BIRTH_YEAR = 1900; // самый ранний допустимый год рождения

    // приватный конструктор: класс содержит только статические методы, создавать его объекты не нужно
    private PatientValidator() {
    }

    // проверка имени и возврат его без пробелов по краям
    public static String validateName(String name) throws ValidationException {
        if (name == null || name.isBlank()) { // имя не должно быть пустым
            throw new ValidationException("имя не может быть пустым"); // сообщение об ошибке
        }
        String trimmed = name.trim(); // убирает пробелы в начале и конце
        if (!trimmed.matches("[\\p{L} '-]{2,50}")) { // только буквы, пробел, дефис, апостроф; длина 2-50
            throw new ValidationException("имя должно содержать 2-50 символов (буквы, пробел, дефис)"); // ошибка
        }
        return trimmed; // имя корректно
    }

    // преобразование строки в год рождения и проверка диапазона
    public static int parseBirthYear(String raw) throws ValidationException {
        int year; // запись числа
        try {
            year = Integer.parseInt(raw.trim()); // превращает строку в число
        } catch (NumberFormatException e) { // если там не число
            throw new ValidationException("год рождения должен быть целым числом"); // ошибка ввода
        }
        int currentYear = Year.now().getValue(); // текущий год (верхняя граница)
        if (year < MIN_BIRTH_YEAR || year > currentYear) { // проверка диапазона
            throw new ValidationException("год рождения должен быть от " + MIN_BIRTH_YEAR + " до " + currentYear); // ошибка
        }
        return year; // год корректен
    }

    // проверка номера медицинской карты: ровно 6 цифр
    public static String validateCardNumber(String cardNumber) throws ValidationException {
        if (cardNumber == null || !cardNumber.trim().matches("\\d{6}")) { // регулярное выражение: 6 цифр
            throw new ValidationException("номер карты должен состоять ровно из 6 цифр"); // ошибка
        }
        return cardNumber.trim(); // номер корректен
    }
}