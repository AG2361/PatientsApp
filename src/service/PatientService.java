package service; // пакет бизнес-логики

import model.Patient; // класс пациента

import java.time.Year;      // текущий год
import java.util.Comparator;// компаратор для сортировки
import java.util.List;      // интерфейс списка

/**
 * обработка данных пациентов с помощью Stream API.
 */
public class PatientService {

    // ФИЛЬТРАЦИЯ: оставляет только пациентов, родившихся позже указанного года
    public List<Patient> filterBornAfter(List<Patient> patients, int year) {
        return patients.stream()                         // -> список в поток
                .filter(p -> p.getBirthYear() > year)    // оставляем тех, кто моложе заданного года
                .toList();                               // -> результат обратно в список
    }

    // ПРЕОБРАЗОВАНИЕ map(): превращает каждого пациента в строку "ИМЯ (возраст N лет)"
    public List<String> mapToNamesWithAge(List<Patient> patients) {
        int currentYear = Year.now().getValue(); // текущий год для вычисления возраста
        return patients.stream()                 // -> поток
                .map(p -> p.getName().toUpperCase() + " (возраст " + (currentYear - p.getBirthYear()) + ")") // Patient -> String
                .toList();                       // -> строки в список
    }

    // СОРТИРОВКА: упорядочивает пациентов по году рождения (от старших к младшим)
    public List<Patient> sortByBirthYear(List<Patient> patients) {
        return patients.stream()                                       // -> поток
                .sorted(Comparator.comparingInt(Patient::getBirthYear))// сортируем по полю "год рождения"
                .toList();                                             // -> в список
    }
}