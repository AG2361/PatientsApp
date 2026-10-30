package repository; // пакет хранилища

import model.Patient; // класс пациента

import java.util.ArrayList; // реализация списка
import java.util.List;      // интерфейс списка

/**
 * Хранилище пациентов в коллекции List.
 */
public class PatientRepository {

    private final List<Patient> patients = new ArrayList<>(); // коллекция пациентов

    // добавление пациента; возвращает false, если карта с таким номером уже есть
    public boolean add(Patient patient) {
        boolean exists = patients.stream() // создание потока из списка
                .anyMatch(p -> p.getCardNumber().equals(patient.getCardNumber())); // есть ли такой номер карты?
        if (exists) { // если дубликат
            return false; // не добавляется
        }
        patients.add(patient); // добавление в список
        return true; // корректно
    }

    // добавление сразу нескольких пациентов; возвращает, сколько реально добавлено
    public int addAll(List<Patient> newPatients) {
        int added = 0; // счётчик добавленных
        for (Patient patient : newPatients) { // перебор новых пациентов
            if (add(patient)) { // добавление
                added++; // увеличение счётчика при успехе
            }
        }
        return added; // возвращает результат
    }

    // возвращение копии списка, чтобы избежать изменений хранилища внешним кодом напрямую
    public List<Patient> getAll() {
        return List.copyOf(patients); // неизменяемая копия
    }
}