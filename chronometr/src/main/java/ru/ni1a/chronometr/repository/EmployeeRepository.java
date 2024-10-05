package ru.ni1a.chronometr.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ni1a.chronometr.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {}