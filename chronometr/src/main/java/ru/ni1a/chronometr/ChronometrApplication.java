package ru.ni1a.chronometr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.ni1a.chronometr.controller.ConsoleController;

@SpringBootApplication
public class ChronometrApplication implements CommandLineRunner {

	@Autowired
	private ConsoleController consoleController;

	public static void main(String[] args) {
		SpringApplication.run(ChronometrApplication.class, args);
	}

	@Override
	public void run(String... args) {
		consoleController.start();
	}
}