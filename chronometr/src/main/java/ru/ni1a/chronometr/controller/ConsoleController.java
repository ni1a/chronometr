package ru.ni1a.chronometr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.ni1a.chronometr.model.Employee;
import ru.ni1a.chronometr.model.Shift;
import ru.ni1a.chronometr.service.WorkService;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.Scanner;

@Controller
public class ConsoleController {
    @Autowired
    private WorkService workService;

    private Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            try {
                System.out.println("Добрый день! Введите ваш ID (или 0 для выхода): ");
                Long id = scanner.nextLong();

                if (id == 0) {
                    System.out.println("Выход из программы.");
                    break;
                }

                Employee employee = workService.getEmployeeById(id).orElse(null);

                if (employee == null) {
                    System.out.println("Работник не найден. Попробуйте снова.");
                    continue;
                }

                manageShifts(employee); // Вызов управления сменами пользователя

            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите числовое значение.");
                scanner.nextLine(); // Очищаем буфер сканера после некорректного ввода
            }
        }
    }

    private void manageShifts(Employee employee) {
        Shift shift = workService.getCurrentShift(employee);

        if (shift == null) {
            System.out.println("Выходите на работу?");
            System.out.println("1. Да");
            System.out.println("2. Нет (Отмена)");

            try {
                int choice = scanner.nextInt();
                if (choice == 1) {
                    shift = new Shift();
                    shift.setEmployee(employee);
                    shift.setStartTime(LocalDateTime.now());
                    workService.clockIn(shift);
                    System.out.println("Вы вышли на работу.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, выберите 1 или 2.");
                scanner.nextLine(); // Очищаем буфер сканера
            }
            return;
        }

        while (true) {
            showWorkMenu(shift);
            try {
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1: // Пробиться на перерыв
                        if (shift.getBreakStart() == null || shift.getBreakEnd() != null) {
                            workService.startBreak(shift);
                            System.out.println("Перерыв начат.");
                            return;
                        } else {
                            System.out.println("Вы уже на перерыве. Завершите текущий перерыв.");
                        }
                        break;
                    case 2: // Завершить перерыв
                        if (shift.getBreakStart() != null && shift.getBreakEnd() == null) {
                            workService.endBreak(shift);
                            System.out.println("Перерыв завершен.");
                            return;
                        } else {
                            System.out.println("Вы не на перерыве.");
                        }
                        break;
                    case 3: // Завершить работу
                        workService.clockOut(shift);
                        System.out.printf("Работа завершена. Отработано: %.2f часов%n", shift.getHoursWorked());
                        return;
                    case 4: // Отмена
                        return;
                    default:
                        System.out.println("Неправильный ввод. Попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Некорректный ввод. Пожалуйста, введите числовое значение.");
                scanner.nextLine(); // Очищаем буфер сканера
            }
        }
    }

    private void showWorkMenu(Shift shift) {
        System.out.println("Выберите действие: ");

        if (shift.getBreakStart() == null || shift.getBreakEnd() != null) { // Можно начать новый перерыв, если текущий завершен
            System.out.println("1. Пробиться на перерыв");
        }

        if (shift.getBreakStart() != null && shift.getBreakEnd() == null) { // Можно завершить текущий перерыв
            System.out.println("2. Завершить перерыв");
        }

        System.out.println("3. Завершить работу");
        System.out.println("4. Отмена");
    }
}