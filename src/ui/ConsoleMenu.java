package ui; // пакет пользовательского интерфейса

import fileio.PatientFileReader;         // чтение из файла
import model.Patient;                    // класс пациента
import repository.PatientRepository;     // хранилище
import service.PatientService;           // обработка Stream API
import validation.PatientValidator;      // валидатор
import validation.ValidationException;   // исключение валидации

import java.io.IOException;                  // ошибки ввода-вывода
import java.nio.file.InvalidPathException;   // ошибка неверного пути
import java.util.List;                       // интерфейс списка
import java.util.NoSuchElementException;     // ошибка при закрытии ввода (Ctrl+D / Ctrl+Z)
import java.util.Scanner;                    // чтение ввода с консоли

/**
 * консольное меню приложения.
 */
public class ConsoleMenu {

    private final Scanner scanner = new Scanner(System.in);                // чтение с клавиатуры
    private final PatientRepository repository = new PatientRepository();  // хранилище пациентов
    private final PatientService service = new PatientService();           // сервис обработки данных
    private final PatientFileReader fileReader = new PatientFileReader();  // чтение файлов

    // функциональный интерфейс: превращает введённую строку в значение или вывод ValidationException
    @FunctionalInterface
    private interface InputParser<T> {
        T parse(String raw) throws ValidationException; // метод разбора и проверки строки
    }

    // главный цикл программы: работает, пока пользователь не выберет «Выход»
    public void run() {
        try {
            while (true) { // бесконечный цикл меню
                printMenu(); // вывод пунктов меню
                String input = scanner.nextLine(); // чтение выбора пользователя

                int command; // запись номера команды
                try {
                    command = Integer.parseInt(input.trim()); // превращение ввода в число
                } catch (NumberFormatException e) { // если ввеедено не число
                    System.out.println("Ошибка: необходимо ввести число."); // ошибка
                    continue; // возврат в начало цикла
                }

                switch (command) { // выбор действия по номеру
                    case 1 -> addManually();     // добавление вручную
                    case 2 -> loadFromFile();    // загрузка из файла
                    case 3 -> showAll();         // показать всех
                    case 4 -> showFiltered();    // фильтрация (filter)
                    case 5 -> showNamesWithAge();// преобразование (map)
                    case 6 -> showSorted();      // сортировка (sorted)
                    case 0 -> { // выход
                        System.out.println("Завершение работы программы..."); // завершение
                        return; // выход из метода и из цикла
                    }
                    default -> System.out.println("Неизвестная команда."); // неверный номер пункта
                }
            }
        } catch (NoSuchElementException e) { // если поток ввода закрыт
            System.out.println("Ввод завершён. Выход из программы."); // корректно завершаемся
        }
    }

    // печать меню
    private void printMenu() {
        System.out.println("""
                
                ===== МЕНЮ =====
                1. Добавить пациента вручную
                2. Загрузить пациентов из файла
                3. Показать всех пациентов
                4. Фильтр: пациенты, рождённые после года... (filter)
                5. Имена и возраст (map)
                6. Сортировка по году рождения (sorted)
                0. Выход
                """); // текстовый блок с пунктами меню
        System.out.print("Выберите пункт меню: "); // л вводе
    }

    // пункт 1: добавление пациента вручную
    private void addManually() {
        System.out.println("--- Добавление пациента вручную ---"); // заголовок
        String name = readUntilValid("Имя: ", PatientValidator::validateName);                  // имя с проверкой
        int birthYear = readUntilValid("Год рождения: ", PatientValidator::parseBirthYear);     // год с проверкой
        String card = readUntilValid("Номер карты (6 цифр): ", PatientValidator::validateCardNumber); // карта с проверкой

        Patient patient = new Patient(name, birthYear, card); // создание объекта пациента
        if (repository.add(patient)) { // добавление в хранилище
            System.out.println("Пациент добавлен."); // корректно
        } else {
            System.out.println("Ошибка: пациент с такой картой уже существует."); // дубликат
        }
    }

    // пункт 2: загрузка пациентов из файла
    private void loadFromFile() {
        System.out.print("Введите путь к файлу (например, data/patients.txt): "); // запрос пути
        String path = scanner.nextLine().trim(); // чтение пути
        try {
            List<Patient> loaded = fileReader.read(path); // чтение файла
            int added = repository.addAll(loaded);        // добавление в хранилище, получение числа добавленных
            System.out.println("Прочитано корректных записей: " + loaded.size()); // прочитано
            System.out.println("Добавлено новых пациентов: " + added);           // добавлено без дублирования
        } catch (IOException e) { // файл не найден или ошибка чтения
            System.out.println("Ошибка чтения файла: " + e.getMessage()); // причина ошибки
        } catch (InvalidPathException e) { // недопустимые символы пути
            System.out.println("Ошибка: недопустимый путь к файлу."); // ошибка
        }
    }

    // пункт 3: вывод всех пациентов
    private void showAll() {
        System.out.println("--- Все пациенты ---"); // заголовок
        printList(repository.getAll()); // печать списка из хранилища
    }

    // пункт 4: фильтрация по году рождения
    private void showFiltered() {
        int year = readUntilValid("Показать рождённых после года: ", PatientValidator::parseBirthYear); // ввод года
        System.out.println("--- Пациенты, рождённые после " + year + " ---"); // заголовок
        printList(service.filterBornAfter(repository.getAll(), year)); // печать
    }

    // пункт 5: преобразование map()
    private void showNamesWithAge() {
        System.out.println("--- Имена и возраст ---"); // заголовок
        printList(service.mapToNamesWithAge(repository.getAll())); // преобразование и печать
    }

    // пункт 6: сортировка
    private void showSorted() {
        System.out.println("--- Сортировка по году рождения ---"); // заголовок
        printList(service.sortByBirthYear(repository.getAll())); // сортировка и печать
    }

    // универсальная печать списка для Patient и для String
    private void printList(List<?> items) {
        if (items.isEmpty()) { // если список пустой
            System.out.println("Список пуст."); // сообщение
            return;
        }
        items.forEach(System.out::println); // печать элементов с новой строки
    }

    // запрос ввода до ввода корректного значения пользователем
    private <T> T readUntilValid(String prompt, InputParser<T> parser) {
        while (true) { // повтор до корректного ввода
            System.out.print(prompt); // приглашение
            String raw = scanner.nextLine(); // чтение строки
            try {
                return parser.parse(raw); // возврат значения если проверка пройдена
            } catch (ValidationException e) { // если данные некорректны
                System.out.println("Ошибка: " + e.getMessage()); // причина и новый запрос
            }
        }
    }
}