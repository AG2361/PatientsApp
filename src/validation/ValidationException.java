package validation; // пакет валидации

/**
 * исключение, которое выбрасывается при некорректных входных данных
 */
public class ValidationException extends Exception { // checked-исключение: его нужно обработать

    // конструктор принимает текст ошибки
    public ValidationException(String message) {
        super(message); // передача текста в родительский класс Exception
    }
}