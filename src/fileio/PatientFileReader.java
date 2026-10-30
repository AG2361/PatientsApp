package fileio; // пакет работы с файлами

import model.Patient;                    // класс пациента
import validation.PatientValidator;      // валидатор данных
import validation.ValidationException;   // исключение валидации

import java.io.BufferedReader;           // буферизованное чтение текста
import java.io.IOException;              // исключение ввода-вывода
import java.nio.charset.StandardCharsets;// кодировки
import java.nio.file.Files;              // утилиты работы с файлами
import java.nio.file.Path;               // путь к файлу
import java.util.ArrayList;              // реализация списка
import java.util.List;                   // интерфейс списка

/**
 * чтение пациентов из текстового файла (формат: имя;год;номер_карты).
 */
public class PatientFileReader {

    private static final String SEPARATOR = ";"; // разделитель полей в строке файла

    // чтение файла и возврат списка корректных пациентов
    public List<Patient> read(String filePath) throws IOException {
        List<Patient> result = new ArrayList<>(); // прочитанные пациенты

        // try-with-resources: reader закрывается автоматически при выходе из блока
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            String line;           // текущая строка файла
            int lineNumber = 0;    // номер текущей строки (для сообщений об ошибках)

            while ((line = reader.readLine()) != null) { // чтение строк до конца файла
                lineNumber++; // увеличение номера строки
                if (lineNumber == 1) { // на первой строке
                    line = line.replace("\uFEFF", ""); // удаление невидимого BOM-символа при его наличии
                }
                if (line.isBlank()) { // пустые строки
                    continue; // пропуск
                }
                try {
                    result.add(parseLine(line)); // разбор строк и добавление пациента
                } catch (ValidationException e) { // если данные в строке некорректны сообщение об ошибке
                    System.out.println("Строка " + lineNumber + " пропущена: " + e.getMessage());
                }
            }
        }

        return result; // список прочитанных пациентов
    }

    // разбор одной строки файла в объект Patient
    private Patient parseLine(String line) throws ValidationException {
        String[] parts = line.split(SEPARATOR, -1); // делим строку по ';' (-1 сохраняет пустые поля)
        if (parts.length != 3) { // должно быть ровно три поля
            throw new ValidationException("ожидается 3 поля, разделённых ';'"); // ошибка формата
        }
        String name = PatientValidator.validateName(parts[0]);          // проверка и получение имени
        int birthYear = PatientValidator.parseBirthYear(parts[1]);      // проверка и получение года
        String cardNumber = PatientValidator.validateCardNumber(parts[2]); // проверка номера карты
        return new Patient(name, birthYear, cardNumber); // создание объекта пациент
    }
}