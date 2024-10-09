package ru.ni1a.chronometr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ni1a.chronometr.model.Employee;
import ru.ni1a.chronometr.model.Shift;

public interface ShiftRepository extends JpaRepository<Shift, Long> {
    Shift findLatestShiftByEmployeeAndEndTimeIsNull(Employee employee);
}