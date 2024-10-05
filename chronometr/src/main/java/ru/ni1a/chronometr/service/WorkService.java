package ru.ni1a.chronometr.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ni1a.chronometr.model.Employee;
import ru.ni1a.chronometr.model.Shift;
import ru.ni1a.chronometr.repository.EmployeeRepository;
import ru.ni1a.chronometr.repository.ShiftRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class WorkService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ShiftRepository shiftRepository;

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public void clockIn(Shift shift) {
        // На практике можно добавить логику для проверки на существующую активную смену
        shift.setStartTime(LocalDateTime.now());
        shift.setHoursWorked(0.0); // Изначально у смены 0 отработанных часов
        shiftRepository.save(shift);
    }

    public void clockOut(Shift shift) {
        shift.setEndTime(LocalDateTime.now());
        double hoursWorked = calculateHoursWorked(shift); // Рассчитываем часы без учета перерыва
        shift.setHoursWorked(hoursWorked); // Устанавливаем отработанные часы за смену
        shiftRepository.save(shift); // Сохраняем смену

        // Теперь добавляем отработанные часы к сотруднику за месяц
        Employee employee = shift.getEmployee();
        double totalHours = employee.getTotalHours() != null ? employee.getTotalHours() : 0.0;
        employee.setTotalHours(totalHours + hoursWorked); // Обновляем общие отработанные часы за месяц
        employeeRepository.save(employee);
    }


    public void startBreak(Shift shift) {
        shift.setBreakStart(LocalDateTime.now());
        shift.setBreakEnd(null); // Начало нового перерыва — сбрасываем конец предыдущего
        shiftRepository.save(shift);
    }

    public void endBreak(Shift shift) {
        shift.setBreakEnd(LocalDateTime.now());
        shiftRepository.save(shift);
    }

    public Shift getCurrentShift(Employee employee) {
        // Предполагается, что у вас есть способ получить смену по работнику
        // Это может быть запрос в базу данных по рабочему ID с условием что endTime == null
        return shiftRepository.findLatestShiftByEmployeeAndEndTimeIsNull(employee);
    }

    public double calculateHoursWorked(Shift shift) {
        if (shift.getEndTime() != null && shift.getStartTime() != null) {
            double totalHours = java.time.Duration.between(shift.getStartTime(), shift.getEndTime()).toMinutes() / 60.0;

            if (shift.getBreakStart() != null && shift.getBreakEnd() != null) {
                double breakDuration = java.time.Duration.between(shift.getBreakStart(), shift.getBreakEnd()).toMinutes() / 60.0;
                totalHours -= breakDuration;
            }
            return Math.round(totalHours * 100.0) / 100.0; // Округляем до 2 знаков после запятой
        }
        return 0.0;
    }
}